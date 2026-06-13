<template>
  <div class="profile-shell">
    <main class="profile-page">
      <header class="masthead">
        <div>
          <span>The Guanjian Journal</span>
          <h1>个人资料</h1>
        </div>
        <button type="button" @click="goHome">返回首页</button>
      </header>

      <template v-if="user">
        <section class="identity">
          <img v-if="user.avatar" :src="user.avatar" :alt="`${displayName}的头像`" />
          <div v-else class="avatar-fallback">{{ avatarText }}</div>

          <div class="identity-copy">
            <div class="name-row">
              <h2>{{ displayName }}</h2>
              <span>@{{ user.username }}</span>
            </div>
            <p :class="{ placeholder: !profile.headline }">
              {{ profile.headline || '这个人很低调，还没有写下一句自我介绍。' }}
            </p>
            <div class="identity-meta">
              <span>{{ profile.occupation || '职业身份待补充' }}</span>
              <span>{{ profile.location || '所在地待补充' }}</span>
            </div>
            <div class="tag-list">
              <template v-if="profile.technicalDirections.length">
                <span v-for="item in profile.technicalDirections" :key="item">{{ item }}</span>
              </template>
              <span v-else class="placeholder-tag">技术方向待补充</span>
            </div>
          </div>

          <div class="actions">
            <button type="button" class="primary-button" @click="openProfileEditor">编辑资料</button>
            <button type="button" @click="openPasswordEditor">修改密码</button>
          </div>
        </section>

        <section class="statistics">
          <div v-for="stat in displayStatistics" :key="stat.label">
            <strong>{{ stat.value || '待统计' }}</strong>
            <span>{{ stat.label }}</span>
          </div>
        </section>

        <div class="content-grid">
          <div class="main-column">
            <section class="content-section">
              <h3>个人介绍</h3>
              <p v-if="profile.about" class="long-copy">{{ profile.about }}</p>
              <button v-else type="button" class="empty-copy" @click="openProfileEditor">
                还没有填写个人介绍。可以写下你的经历、兴趣与正在关注的事情。
                <span>去补充 →</span>
              </button>
            </section>

            <section class="content-section">
              <h3>最近文章</h3>
              <template v-if="recentArticles.length">
                <a v-for="article in recentArticles" :key="article.title" class="list-item" href="#">
                  <div>
                    <strong>{{ article.title }}</strong>
                    <span>{{ article.category }} · {{ article.date }}</span>
                  </div>
                  <b>→</b>
                </a>
              </template>
              <p v-else class="empty-copy static">暂时还没有发布文章，新的思考会在这里出现。</p>
            </section>

            <section class="content-section">
              <h3>项目经历</h3>
              <template v-if="projects.length">
                <article v-for="project in projects" :key="project.name" class="project-item">
                  <div>
                    <strong>{{ project.name }}</strong>
                    <span>{{ project.period }}</span>
                  </div>
                  <p>{{ project.description }}</p>
                  <div v-if="project.technologies.length" class="tag-list small">
                    <span v-for="tech in project.technologies" :key="tech">{{ tech }}</span>
                  </div>
                </article>
              </template>
              <p v-else class="empty-copy static">项目经历尚未整理，完成的作品与实践会展示在这里。</p>
            </section>
          </div>

          <aside class="side-column">
            <section class="compact-section">
              <h3>职业身份</h3>
              <p :class="{ placeholder: !profile.occupation }">
                {{ profile.occupation || '尚未填写职业身份' }}
              </p>
            </section>

            <section class="compact-section">
              <h3>学习方向</h3>
              <ul v-if="profile.learningDirections.length">
                <li v-for="item in profile.learningDirections" :key="item">{{ item }}</li>
              </ul>
              <p v-else class="placeholder">尚未记录正在学习的方向</p>
            </section>

            <section class="compact-section">
              <h3>联系方式</h3>
              <dl v-if="visibleContacts.length">
                <div v-for="item in visibleContacts" :key="item.label">
                  <dt>{{ item.label }}</dt>
                  <dd>{{ item.value }}</dd>
                </div>
              </dl>
              <p v-else class="placeholder">暂未公开联系方式</p>
            </section>

            <section class="compact-section">
              <h3>社交链接</h3>
              <template v-if="visibleSocialLinks.length">
                <a
                  v-for="link in visibleSocialLinks"
                  :key="link.name"
                  :href="link.url"
                  target="_blank"
                  rel="noreferrer"
                >
                  {{ link.name }} <span>↗</span>
                </a>
              </template>
              <p v-else class="placeholder">尚未添加社交主页</p>
            </section>
          </aside>
        </div>

        <footer class="profile-footer">
          <button type="button" @click="logout">退出登录</button>
        </footer>
      </template>

      <section v-else class="empty-state">
        <h2>尚未登录</h2>
        <p>登录后即可查看和修改个人资料。</p>
        <button type="button" @click="goToLogin">前往登录</button>
      </section>
    </main>

    <div v-if="activeEditor" class="editor-backdrop" @click.self="closeEditor">
      <section class="editor-panel" role="dialog" aria-modal="true">
        <header>
          <div>
            <span>{{ activeEditor === 'profile' ? 'PROFILE SETTINGS' : 'SECURITY SETTINGS' }}</span>
            <h2>{{ activeEditor === 'profile' ? '编辑个人资料' : '修改密码' }}</h2>
          </div>
          <button type="button" aria-label="关闭" @click="closeEditor">×</button>
        </header>

        <form v-if="activeEditor === 'profile'" class="editor-form" @submit.prevent="saveProfile">
          <div class="form-grid">
            <label>
              <span>昵称</span>
              <input v-model="profileForm.nickname" type="text" placeholder="你的公开昵称" />
            </label>
            <label>
              <span>头像地址</span>
              <input v-model="profileForm.avatar" type="url" placeholder="https://..." />
            </label>
          </div>

          <label>
            <span>一句简介</span>
            <input v-model="profileForm.headline" type="text" placeholder="用一句话介绍自己" />
          </label>

          <div class="form-grid">
            <label>
              <span>职业身份</span>
              <input v-model="profileForm.occupation" type="text" placeholder="例如：Java 后端开发者" />
            </label>
            <label>
              <span>所在地</span>
              <input v-model="profileForm.location" type="text" placeholder="例如：上海" />
            </label>
          </div>

          <label>
            <span>技术方向</span>
            <input
              v-model="profileForm.technicalDirections"
              type="text"
              placeholder="多个方向请用逗号分隔"
            />
          </label>

          <label>
            <span>个人介绍</span>
            <textarea v-model="profileForm.about" rows="4" placeholder="介绍你的经历、兴趣或关注方向"></textarea>
          </label>

          <label>
            <span>学习方向</span>
            <input
              v-model="profileForm.learningDirections"
              type="text"
              placeholder="多个方向请用逗号分隔"
            />
          </label>

          <div class="form-grid">
            <label>
              <span>联系邮箱</span>
              <input v-model="profileForm.email" type="email" placeholder="name@example.com" />
            </label>
            <label>
              <span>GitHub</span>
              <input v-model="profileForm.github" type="url" placeholder="https://github.com/..." />
            </label>
          </div>

          <p v-if="profileMessage" class="form-message success">{{ profileMessage }}</p>
          <div class="form-actions">
            <button type="button" @click="closeEditor">取消</button>
            <button type="submit" class="primary-button">保存资料</button>
          </div>
        </form>

        <form v-else class="editor-form" @submit.prevent="submitPassword">
          <label>
            <span>当前密码</span>
            <input v-model="passwordForm.oldPassword" type="password" autocomplete="current-password" />
          </label>
          <label>
            <span>新密码</span>
            <input v-model="passwordForm.newPassword" type="password" autocomplete="new-password" />
          </label>
          <label>
            <span>确认新密码</span>
            <input v-model="passwordForm.confirmPassword" type="password" autocomplete="new-password" />
          </label>
          <p v-if="passwordMessage" class="form-message">{{ passwordMessage }}</p>
          <div class="form-actions">
            <button type="button" @click="closeEditor">取消</button>
            <button type="submit" class="primary-button">确认修改</button>
          </div>
        </form>
      </section>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, reactive, ref } from 'vue'
import {
  clearAuth,
  getAuth,
  getAvatarText,
  getDisplayName,
  updateStoredUser
} from '../../utils/auth'

interface ProfileData {
  headline: string
  occupation: string
  location: string
  technicalDirections: string[]
  about: string
  learningDirections: string[]
  email: string
  github: string
}

const PROFILE_STORAGE_KEY = 'guanjian-journal-profile'
const auth = getAuth()
const user = auth?.user ? reactive({ ...auth.user }) : null

const defaultProfile: ProfileData = {
  headline: '',
  occupation: '',
  location: '',
  technicalDirections: [],
  about: '',
  learningDirections: [],
  email: '',
  github: ''
}

const loadProfile = (): ProfileData => {
  try {
    const stored = localStorage.getItem(PROFILE_STORAGE_KEY)
    return stored ? { ...defaultProfile, ...JSON.parse(stored) } : defaultProfile
  } catch {
    return defaultProfile
  }
}

const profile = reactive(loadProfile())
const activeEditor = ref<'profile' | 'password' | null>(null)
const profileMessage = ref('')
const passwordMessage = ref('')

const profileForm = reactive({
  nickname: '',
  avatar: '',
  headline: '',
  occupation: '',
  location: '',
  technicalDirections: '',
  about: '',
  learningDirections: '',
  email: '',
  github: ''
})

const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

// These collections stay hidden until their corresponding backend APIs return data.
const blogStatistics: Array<{ label: string; value: string }> = []
const recentArticles: Array<{
  category: string
  date: string
  title: string
}> = []
const projects: Array<{
  name: string
  period: string
  description: string
  technologies: string[]
}> = []

const displayName = computed(() => user ? getDisplayName(user) : '')
const avatarText = computed(() => user ? getAvatarText(user) : '')
const displayStatistics = computed(() => blogStatistics.length
  ? blogStatistics
  : [
      { label: '文章', value: '' },
      { label: '分类', value: '' },
      { label: '标签', value: '' }
    ])
const visibleContacts = computed(() => [
  { label: '邮箱', value: profile.email },
  { label: '所在地', value: profile.location }
].filter((item) => item.value))
const visibleSocialLinks = computed(() => [
  { name: 'GitHub', url: profile.github }
].filter((item) => item.url))
const splitValues = (value: string) =>
  value.split(/[,，]/).map((item) => item.trim()).filter(Boolean)

const openProfileEditor = () => {
  if (!user) return

  Object.assign(profileForm, {
    nickname: user.nickname || '',
    avatar: user.avatar || '',
    headline: profile.headline,
    occupation: profile.occupation,
    location: profile.location,
    technicalDirections: profile.technicalDirections.join('，'),
    about: profile.about,
    learningDirections: profile.learningDirections.join('，'),
    email: profile.email,
    github: profile.github
  })
  profileMessage.value = ''
  activeEditor.value = 'profile'
}

const openPasswordEditor = () => {
  Object.assign(passwordForm, { oldPassword: '', newPassword: '', confirmPassword: '' })
  passwordMessage.value = ''
  activeEditor.value = 'password'
}

const closeEditor = () => {
  activeEditor.value = null
}

const saveProfile = () => {
  if (!user) return

  Object.assign(profile, {
    headline: profileForm.headline.trim(),
    occupation: profileForm.occupation.trim(),
    location: profileForm.location.trim(),
    technicalDirections: splitValues(profileForm.technicalDirections),
    about: profileForm.about.trim(),
    learningDirections: splitValues(profileForm.learningDirections),
    email: profileForm.email.trim(),
    github: profileForm.github.trim()
  })

  user.nickname = profileForm.nickname.trim() || user.username
  user.avatar = profileForm.avatar.trim() || null
  localStorage.setItem(PROFILE_STORAGE_KEY, JSON.stringify(profile))
  updateStoredUser({ ...user })
  profileMessage.value = '资料已保存到本地，后端接口完成后可改为同步保存。'
}

const submitPassword = () => {
  if (!passwordForm.oldPassword || !passwordForm.newPassword) {
    passwordMessage.value = '请填写当前密码和新密码'
  } else if (passwordForm.newPassword.length < 6) {
    passwordMessage.value = '新密码至少需要 6 位'
  } else if (passwordForm.newPassword !== passwordForm.confirmPassword) {
    passwordMessage.value = '两次输入的新密码不一致'
  } else {
    passwordMessage.value = '后端修改密码接口尚未实现，当前未提交密码。'
  }
}

const goHome = () => window.location.assign('/')
const goToLogin = () => window.location.assign('/login')
const logout = () => {
  clearAuth()
  goToLogin()
}
</script>

<style scoped>
.profile-shell {
  min-height: 100vh;
  background: #f8f7f4;
}

.profile-page {
  max-width: 1120px;
  min-height: 100vh;
  margin: 0 auto;
  padding: 28px 36px 46px;
  color: #191919;
}

.masthead,
.identity,
.name-row,
.actions,
.statistics,
.list-item,
.project-item > div:first-child,
.profile-footer,
.editor-panel header,
.form-actions {
  display: flex;
  align-items: center;
}

.masthead {
  justify-content: space-between;
  padding: 12px 0 18px;
  border-bottom: 1px solid #202020;
}

.masthead span {
  color: #a33d2d;
  font-size: 10px;
  letter-spacing: 0.18em;
  text-transform: uppercase;
}

.masthead h1 {
  margin: 3px 0 0;
  font-family: "Times New Roman", "Noto Serif SC", serif;
  font-size: 28px;
  font-weight: 500;
}

button {
  padding: 8px 13px;
  border: 1px solid #c9c6c0;
  background: transparent;
  color: #333;
  cursor: pointer;
}

.primary-button {
  border-color: #191919;
  background: #191919;
  color: #fff;
}

.identity {
  gap: 22px;
  padding: 28px 0 24px;
  border-bottom: 1px solid #dedbd5;
}

.identity img,
.avatar-fallback {
  display: grid;
  flex: 0 0 auto;
  width: 86px;
  height: 86px;
  place-items: center;
  border: 1px solid #191919;
  border-radius: 50%;
  object-fit: cover;
}

.avatar-fallback {
  background: #a33d2d;
  color: #fff;
  font-family: "Times New Roman", serif;
  font-size: 30px;
}

.identity-copy {
  min-width: 0;
  flex: 1;
}

.name-row {
  gap: 10px;
}

.name-row h2 {
  margin: 0;
  font-family: "Times New Roman", "Noto Serif SC", serif;
  font-size: 30px;
  font-weight: 500;
}

.name-row span,
.identity-copy p,
.identity-meta,
.content-section,
.side-column,
.statistics,
.editor-panel {
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", "PingFang SC", sans-serif;
}

.name-row span {
  color: #999;
  font-size: 12px;
}

.identity-copy p {
  margin: 7px 0;
  color: #555;
  font-size: 14px;
}

.placeholder {
  color: #aaa !important;
}

.identity-meta {
  display: flex;
  gap: 15px;
  color: #888;
  font-size: 12px;
}

.tag-list {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  margin-top: 11px;
}

.tag-list span {
  padding: 3px 8px;
  border: 1px solid #ddd8d0;
  color: #666;
  font-size: 11px;
}

.tag-list .placeholder-tag {
  border-style: dashed;
  color: #aaa;
}

.tag-list.small {
  margin-top: 8px;
}

.actions {
  gap: 8px;
}

.statistics {
  gap: 30px;
  padding: 14px 0;
  border-bottom: 1px solid #dedbd5;
}

.statistics div {
  display: flex;
  align-items: baseline;
  gap: 6px;
}

.statistics strong {
  font-family: "Times New Roman", serif;
  font-size: 22px;
  font-weight: 500;
}

.statistics strong:has(+ span) {
  white-space: nowrap;
}

.statistics span {
  color: #888;
  font-size: 11px;
}

.content-grid {
  display: grid;
  grid-template-columns: minmax(0, 1fr) 260px;
  gap: 42px;
  padding-top: 28px;
}

.content-section {
  margin-bottom: 30px;
}

.content-section h3,
.compact-section h3 {
  margin: 0 0 12px;
  font-family: "Times New Roman", "Noto Serif SC", serif;
  font-size: 17px;
  font-weight: 600;
}

.long-copy {
  margin: 0;
  color: #555;
  font-size: 13px;
  line-height: 1.9;
}

.empty-copy {
  display: block;
  width: 100%;
  padding: 14px 16px;
  border: 1px dashed #d6d1ca;
  background: #fbfaf8;
  color: #888;
  text-align: left;
  font-size: 12px;
  line-height: 1.7;
}

.empty-copy span {
  margin-left: 5px;
  color: #a33d2d;
}

.empty-copy.static {
  margin: 0;
  cursor: default;
}

.list-item {
  justify-content: space-between;
  padding: 13px 0;
  border-top: 1px solid #e1ded8;
}

.list-item strong,
.list-item span {
  display: block;
}

.list-item strong {
  margin-bottom: 4px;
  color: #333;
  font-size: 14px;
  font-weight: 500;
}

.list-item span {
  color: #999;
  font-size: 10px;
}

.list-item b {
  color: #a33d2d;
  font-weight: 400;
}

.project-item {
  padding: 14px 0;
  border-top: 1px solid #e1ded8;
}

.project-item > div:first-child {
  justify-content: space-between;
}

.project-item strong {
  font-size: 14px;
}

.project-item > div:first-child span {
  color: #999;
  font-size: 10px;
}

.project-item p {
  margin: 7px 0 0;
  color: #666;
  font-size: 12px;
  line-height: 1.7;
}

.side-column {
  padding-left: 24px;
  border-left: 1px solid #dedbd5;
}

.compact-section {
  padding-bottom: 18px;
  margin-bottom: 18px;
  border-bottom: 1px solid #e1ded8;
}

.compact-section p {
  margin: 0;
  color: #555;
  font-size: 13px;
}

.compact-section ul {
  margin: 0;
  padding-left: 17px;
  color: #666;
  font-size: 12px;
  line-height: 1.9;
}

.compact-section dl {
  margin: 0;
}

.compact-section dl div {
  display: grid;
  grid-template-columns: 55px 1fr;
  gap: 8px;
  padding: 5px 0;
  font-size: 12px;
}

.compact-section dt {
  color: #999;
}

.compact-section dd {
  margin: 0;
  color: #555;
  overflow-wrap: anywhere;
}

.compact-section > a {
  display: flex;
  justify-content: space-between;
  padding: 6px 0;
  color: #555;
  font-size: 12px;
}

.compact-section > a span {
  color: #a33d2d;
}

.profile-footer {
  justify-content: flex-end;
  padding-top: 20px;
  border-top: 1px solid #202020;
}

.empty-state {
  padding: 100px 0;
  text-align: center;
}

.empty-state h2 {
  margin: 0 0 8px;
}

.empty-state p {
  margin: 0 0 22px;
  color: #777;
}

.editor-backdrop {
  position: fixed;
  inset: 0;
  z-index: 20;
  display: grid;
  place-items: center;
  padding: 20px;
  background: rgba(20, 20, 20, 0.45);
}

.editor-panel {
  width: min(100%, 650px);
  max-height: calc(100vh - 40px);
  overflow-y: auto;
  padding: 24px;
  background: #fbfbfb;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.18);
}

.editor-panel header {
  justify-content: space-between;
  padding-bottom: 16px;
  margin-bottom: 20px;
  border-bottom: 1px solid #191919;
}

.editor-panel header span {
  color: #a33d2d;
  font-size: 9px;
  letter-spacing: 0.16em;
}

.editor-panel header h2 {
  margin: 3px 0 0;
  font-family: "Times New Roman", "Noto Serif SC", serif;
  font-size: 24px;
  font-weight: 500;
}

.editor-panel header button {
  padding: 0;
  border: 0;
  font-size: 24px;
}

.editor-form {
  display: grid;
  gap: 15px;
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 15px;
}

.editor-form label {
  display: grid;
  gap: 6px;
}

.editor-form label span {
  color: #666;
  font-size: 11px;
}

.editor-form input,
.editor-form textarea {
  width: 100%;
  padding: 10px 11px;
  border: 1px solid #d1cdc6;
  outline: none;
  background: #fff;
  color: #222;
  resize: vertical;
}

.editor-form input:focus,
.editor-form textarea:focus {
  border-color: #a33d2d;
}

.form-message {
  margin: 0;
  color: #a33d2d;
  font-size: 12px;
}

.form-message.success {
  color: #39734f;
}

.form-actions {
  justify-content: flex-end;
  gap: 8px;
  padding-top: 5px;
}

@media (max-width: 720px) {
  .profile-page {
    padding: 20px;
  }

  .identity {
    align-items: flex-start;
    flex-wrap: wrap;
  }

  .actions {
    width: 100%;
    padding-left: 108px;
  }

  .content-grid {
    grid-template-columns: 1fr;
    gap: 5px;
  }

  .side-column {
    padding-left: 0;
    border-left: 0;
  }
}

@media (max-width: 520px) {
  .identity img,
  .avatar-fallback {
    width: 68px;
    height: 68px;
  }

  .name-row {
    align-items: flex-start;
    flex-direction: column;
    gap: 2px;
  }

  .name-row h2 {
    font-size: 25px;
  }

  .actions {
    padding-left: 0;
  }

  .statistics {
    gap: 18px;
  }

  .form-grid {
    grid-template-columns: 1fr;
  }
}
</style>
