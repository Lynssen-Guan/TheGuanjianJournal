<template>
  <div class="login-shell">
    <div class="login-page">
      <header class="masthead">
        <div class="masthead__eyebrow">The Guanjian Journal</div>
        <h1>管见录</h1>
        <p>A modest record of code, books, and thoughts.</p>
      </header>

      <nav class="login-nav" aria-label="登录页导航">
        <span>读书 · 代码 · 思考</span>
        <span>Editor Access · No. 001</span>
      </nav>

      <main class="login-content">
        <section class="login-intro">
          <div class="section-label">EDITORIAL DESK</div>
          <p class="issue">THE PRIVATE EDITION</p>
          <h2>归来，继续<br />整理你的思考。</h2>
          <p class="intro-copy">
            登录管见录，在代码、书页与日常之间，继续保存那些值得反复推敲的理解。
          </p>

          <blockquote>
            <span>“</span>
            所见或有限，记录使理解逐渐清晰。
          </blockquote>

          <div class="edition-note">
            <span>EST. 2026</span>
            <span>GUANJIAN · CHINA</span>
          </div>
        </section>

        <section class="login-panel" aria-labelledby="login-title">
          <div class="panel-heading">
            <div>
              <span class="panel-heading__number">01</span>
              <p>MEMBER SIGN IN</p>
            </div>
            <span class="panel-heading__mark">GJ</span>
          </div>

          <div class="panel-title">
            <p>欢迎回来</p>
            <h3 id="login-title">登录你的账户</h3>
          </div>

          <form class="login-form" @submit.prevent="submitLogin">
            <label class="field">
              <span>用户名 / USERNAME</span>
              <input
                v-model="form.username"
                type="text"
                name="username"
                placeholder="请输入用户名"
                autocomplete="username"
                :disabled="submitting"
              />
            </label>

            <label class="field">
              <span>密码 / PASSWORD</span>
              <input
                v-model="form.password"
                type="password"
                name="password"
                placeholder="请输入密码"
                autocomplete="current-password"
                :disabled="submitting"
              />
            </label>

            <div class="form-options">
              <label class="remember">
                <input v-model="form.remember" type="checkbox" name="remember" />
                <span>记住登录状态</span>
              </label>
              <span class="text-action">忘记密码？</span>
            </div>

            <p v-if="message" class="form-message" role="alert">{{ message }}</p>

            <button type="submit" class="submit-button" :disabled="submitting">
              <span>{{ submitting ? '正在登录…' : '进入管见录' }}</span>
              <span aria-hidden="true">→</span>
            </button>
          </form>

          <div class="register-note">
            <span>还没有账户？</span>
            <button type="button" @click="goToRegister">申请加入</button>
          </div>
        </section>
      </main>

      <footer class="site-footer">
        <span>© 2026 管见录 · The Guanjian Journal</span>
        <span>FOR THOUGHTFUL RECORDS</span>
      </footer>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { saveAuth, type LoginData } from '../../utils/auth'

interface LoginResponse {
  code: number
  data: LoginData | null
  message?: string
}

const form = reactive({
  username: '',
  password: '',
  remember: false
})

const submitting = ref(false)
const message = ref('')

const goToRegister = () => {
  window.location.assign('/register')
}

const submitLogin = async () => {
  const username = form.username.trim()

  if (!username || !form.password.trim()) {
    message.value = '请填写用户名和密码'
    return
  }

  submitting.value = true
  message.value = ''

  try {
    const response = await fetch('/api/auth/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        username,
        password: form.password
      })
    })
    const result = (await response.json()) as LoginResponse

    if (result.code === 200 && result.data?.user) {
      saveAuth(result.data, form.remember)
      window.location.assign('/')
      return
    }

    if (result.code === 40001) {
      message.value = '该用户不存在，请检查用户名'
    } else if (result.code === 40010) {
      message.value = '密码错误，请重新输入'
    } else {
      message.value = result.message || '登录失败，请稍后重试'
    }
  } catch {
    message.value = '登录失败，请检查网络或后端服务'
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.login-shell {
  min-height: 100vh;
  background:
    radial-gradient(circle at 10% 12%, rgba(163, 61, 45, 0.045), transparent 27%),
    #fbfbfb;
}

.login-page {
  width: 100%;
  min-height: 100vh;
  padding: 30px 96px 36px;
  color: #151515;
  font-family: "Times New Roman", "Noto Serif SC", "Songti SC", SimSun, serif;
}

.masthead {
  border-top: 4px solid #191919;
  border-bottom: 1px solid #191919;
  padding: 18px 0 14px;
  text-align: center;
}

.masthead__eyebrow {
  margin-bottom: 6px;
  color: #6c6c6c;
  font-size: 11px;
  letter-spacing: 0.22em;
  text-transform: uppercase;
}

.masthead h1 {
  margin: 0;
  font-size: 46px;
  font-weight: 500;
  letter-spacing: 0.16em;
}

.masthead p {
  margin: 7px 0 0;
  color: #6c6c6c;
  font-family:
    -apple-system,
    BlinkMacSystemFont,
    "Segoe UI",
    "PingFang SC",
    "Microsoft YaHei",
    sans-serif;
  font-size: 12px;
}

.login-nav {
  display: flex;
  justify-content: space-between;
  padding: 10px 0;
  border-bottom: 3px double #191919;
  color: #777;
  font-size: 10px;
  letter-spacing: 0.16em;
  font-family:
    -apple-system,
    BlinkMacSystemFont,
    "Segoe UI",
    "PingFang SC",
    "Microsoft YaHei",
    sans-serif;
}

.login-content {
  display: grid;
  grid-template-columns: minmax(0, 1.08fr) minmax(400px, 0.92fr);
  min-height: calc(100vh - 260px);
  border-bottom: 1px solid #191919;
}

.login-intro {
  position: relative;
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 54px 9% 54px 2%;
  border-right: 1px solid #dedede;
}

.section-label {
  margin-bottom: 18px;
  color: #a33d2d;
  font-family:
    -apple-system,
    BlinkMacSystemFont,
    "Segoe UI",
    "PingFang SC",
    "Microsoft YaHei",
    sans-serif;
  font-size: 12px;
  letter-spacing: 0.18em;
}

.issue {
  margin: 0 0 12px;
  color: #777;
  font-size: 11px;
  letter-spacing: 0.2em;
}

.login-intro h2 {
  max-width: 660px;
  margin: 0;
  font-size: clamp(42px, 5vw, 72px);
  font-weight: 500;
  line-height: 1.12;
  letter-spacing: -0.045em;
}

.intro-copy {
  max-width: 570px;
  margin: 24px 0 0;
  color: #6c6c6c;
  font-family:
    -apple-system,
    BlinkMacSystemFont,
    "Segoe UI",
    "PingFang SC",
    "Microsoft YaHei",
    sans-serif;
  font-size: 15px;
  line-height: 1.9;
}

.login-intro blockquote {
  position: relative;
  max-width: 560px;
  margin: 38px 0 0;
  padding: 16px 0 16px 28px;
  border-left: 3px solid #a33d2d;
  font-size: 20px;
  line-height: 1.6;
}

.login-intro blockquote span {
  position: absolute;
  top: -20px;
  left: 18px;
  color: rgba(163, 61, 45, 0.14);
  font-size: 72px;
  line-height: 1;
}

.edition-note {
  display: flex;
  gap: 28px;
  margin-top: 36px;
  color: #999;
  font-size: 9px;
  letter-spacing: 0.18em;
}

.login-panel {
  align-self: center;
  width: min(100%, 520px);
  margin: 42px auto;
  padding: 34px 42px 36px;
  border: 1px solid #191919;
  background: rgba(251, 251, 251, 0.86);
  box-shadow: 12px 12px 0 #eee8df;
}

.panel-heading {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  padding-bottom: 15px;
  border-bottom: 1px solid #191919;
}

.panel-heading > div {
  display: flex;
  align-items: center;
  gap: 12px;
}

.panel-heading__number {
  color: #a33d2d;
  font-size: 28px;
  line-height: 1;
}

.panel-heading p {
  margin: 0;
  color: #777;
  font-size: 10px;
  letter-spacing: 0.16em;
}

.panel-heading__mark {
  display: grid;
  width: 34px;
  height: 34px;
  place-items: center;
  border: 1px solid #a33d2d;
  color: #a33d2d;
  font-size: 12px;
  letter-spacing: 0.08em;
}

.panel-title {
  margin: 28px 0 30px;
}

.panel-title p {
  margin: 0 0 5px;
  color: #a33d2d;
  font-size: 13px;
  letter-spacing: 0.12em;
}

.panel-title h3 {
  margin: 0;
  font-size: 32px;
  font-weight: 500;
}

.login-form {
  font-family:
    -apple-system,
    BlinkMacSystemFont,
    "Segoe UI",
    "PingFang SC",
    "Microsoft YaHei",
    sans-serif;
}

.field {
  display: block;
  margin-bottom: 22px;
}

.field > span {
  display: block;
  margin-bottom: 9px;
  color: #555;
  font-size: 11px;
  letter-spacing: 0.1em;
}

.field input {
  width: 100%;
  padding: 13px 2px 12px;
  border: 0;
  border-bottom: 1px solid #9a9a9a;
  border-radius: 0;
  outline: none;
  background: transparent;
  color: #151515;
  font-size: 14px;
  transition: border-color 160ms ease;
}

.field input::placeholder {
  color: #aaa;
}

.field input:focus {
  border-bottom-color: #a33d2d;
}

.form-options {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  margin: 4px 0 26px;
  color: #777;
  font-size: 12px;
}

.remember {
  display: inline-flex;
  align-items: center;
  gap: 8px;
}

.remember input {
  width: 14px;
  height: 14px;
  margin: 0;
  accent-color: #a33d2d;
}

.text-action {
  color: #a33d2d;
}

.form-message {
  margin: -12px 0 18px;
  color: #a33d2d;
  font-size: 13px;
  line-height: 1.5;
}

.submit-button {
  display: flex;
  width: 100%;
  align-items: center;
  justify-content: space-between;
  padding: 14px 18px;
  border: 1px solid #151515;
  background: #151515;
  color: #fff;
  cursor: pointer;
  font-size: 14px;
  letter-spacing: 0.08em;
}

.submit-button:disabled {
  cursor: wait;
  opacity: 0.68;
}

.submit-button span:last-child {
  color: #c96a59;
  font-family: "Times New Roman", serif;
  font-size: 20px;
}

.register-note {
  display: flex;
  justify-content: center;
  gap: 6px;
  margin-top: 22px;
  color: #777;
  font-family:
    -apple-system,
    BlinkMacSystemFont,
    "Segoe UI",
    "PingFang SC",
    "Microsoft YaHei",
    sans-serif;
  font-size: 12px;
}

.register-note button {
  padding: 0;
  border: 0;
  background: transparent;
  color: #a33d2d;
  font-weight: 500;
  cursor: pointer;
}

.site-footer {
  display: flex;
  justify-content: space-between;
  padding-top: 13px;
  color: #777;
  font-family:
    -apple-system,
    BlinkMacSystemFont,
    "Segoe UI",
    "PingFang SC",
    "Microsoft YaHei",
    sans-serif;
  font-size: 10px;
  letter-spacing: 0.08em;
}

@media (max-width: 980px) {
  .login-page {
    padding: 24px 40px 32px;
  }

  .login-content {
    grid-template-columns: 1fr 420px;
  }

  .login-intro {
    padding-right: 7%;
  }

  .login-panel {
    padding: 30px 32px 32px;
    box-shadow: 8px 8px 0 #eee8df;
  }
}

@media (max-width: 780px) {
  .login-page {
    padding: 22px 28px 30px;
  }

  .login-content {
    grid-template-columns: 1fr;
  }

  .login-intro {
    padding: 45px 0 38px;
    border-right: 0;
    border-bottom: 1px solid #dedede;
  }

  .login-intro h2 {
    font-size: 46px;
  }

  .login-intro blockquote,
  .edition-note {
    display: none;
  }

  .login-panel {
    width: calc(100% - 16px);
    margin: 42px 8px 50px 0;
  }
}

@media (max-width: 520px) {
  .login-page {
    padding: 18px 18px 26px;
  }

  .masthead h1 {
    font-size: 36px;
    letter-spacing: 0.12em;
  }

  .login-nav span:last-child {
    display: none;
  }

  .login-intro {
    padding: 36px 0 30px;
  }

  .login-intro h2 {
    font-size: 36px;
  }

  .intro-copy {
    font-size: 14px;
  }

  .login-panel {
    padding: 26px 22px 28px;
  }

  .panel-title h3 {
    font-size: 28px;
  }

  .site-footer {
    display: block;
    text-align: center;
  }

  .site-footer span:last-child {
    display: none;
  }
}
</style>
