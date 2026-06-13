<template>
  <div class="register-shell">
    <main class="register-page">
      <header class="masthead">
        <div class="masthead__eyebrow">The Guanjian Journal</div>
        <h1>管见录</h1>
        <p>A modest record of code, books, and thoughts.</p>
      </header>

      <nav class="register-nav" aria-label="注册页导航">
        <span>读书 · 代码 · 思考</span>
        <button type="button" @click="goToLogin">返回登录</button>
      </nav>

      <section class="register-content">
        <div class="register-intro">
          <p class="issue">MEMBERSHIP · NO. 001</p>
          <h2>从一个名字开始，<br />收藏你的代码与书页。</h2>
          <p>创建账户，在代码、书页与日常之间，保存那些值得反复推敲的理解。</p>
        </div>

        <section class="register-panel" aria-labelledby="register-title">
          <div class="panel-heading">
            <div>
              <span>02</span>
              <p>MEMBER REGISTER</p>
            </div>
            <b>GJ</b>
          </div>

          <div class="panel-title">
            <p>申请加入</p>
            <h3 id="register-title">创建你的账户</h3>
          </div>

          <form class="register-form" @submit.prevent="submitRegister">
            <label class="field">
              <span>用户名 / USERNAME <i>*</i></span>
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
              <span>密码 / PASSWORD <i>*</i></span>
              <input
                v-model="form.password"
                type="password"
                name="password"
                placeholder="请输入密码"
                autocomplete="new-password"
                :disabled="submitting"
              />
            </label>

            <label class="field">
              <span>昵称 / NICKNAME</span>
              <input
                v-model="form.nickname"
                type="text"
                name="nickname"
                placeholder="不填写时默认与用户名相同"
                autocomplete="nickname"
                :disabled="submitting"
              />
            </label>

            <p v-if="message" class="form-message" :class="{ error: isError }" role="alert">
              {{ message }}
            </p>

            <button class="submit-button" type="submit" :disabled="submitting">
              <span>{{ submitting ? '正在提交…' : '提交申请' }}</span>
              <span aria-hidden="true">→</span>
            </button>
          </form>

          <div class="login-note">
            <span>已经拥有账户？</span>
            <button type="button" @click="goToLogin">返回登录</button>
          </div>
        </section>
      </section>

      <footer>
        <span>© 2026 管见录 · The Guanjian Journal</span>
        <span>FOR THOUGHTFUL RECORDS</span>
      </footer>
    </main>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'

interface RegisterResponse {
  code: number
}

const form = reactive({
  username: '',
  password: '',
  nickname: ''
})

const submitting = ref(false)
const message = ref('')
const isError = ref(false)

const goToLogin = () => {
  window.location.assign('/login')
}

const submitRegister = async () => {
  const username = form.username.trim()
  const password = form.password

  if (!username || !password.trim()) {
    isError.value = true
    message.value = '请填写用户名和密码'
    return
  }

  submitting.value = true
  message.value = ''

  try {
    const response = await fetch('/api/auth/register', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        username,
        password,
        nickname: form.nickname.trim() || username
      })
    })
    const result = (await response.json()) as RegisterResponse

    // The requested API contract uses 0, while the current backend ResultCode uses 200.
    if (result.code === 200) {
      isError.value = false
      message.value = '注册成功，正在返回登录页面'
      window.setTimeout(goToLogin, 800)
      return
    }

    isError.value = true
    message.value = result.code === 40000
      ? '该用户名已存在，请更换用户名'
      : '注册失败，请稍后重新注册'
  } catch {
    isError.value = true
    message.value = '注册失败，请检查网络后重新注册'
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.register-shell {
  min-height: 100vh;
  background:
    radial-gradient(circle at 10% 12%, rgba(163, 61, 45, 0.045), transparent 27%),
    #fbfbfb;
}

.register-page {
  width: 100%;
  min-height: 100vh;
  padding: 30px 96px 36px;
  color: #151515;
  font-family: "Times New Roman", "Noto Serif SC", "Songti SC", SimSun, serif;
}

.masthead {
  padding: 4px 0 20px;
  text-align: center;
  border-bottom: 1px solid #191919;
}

.masthead__eyebrow,
.masthead p,
.register-nav,
.issue,
.panel-heading,
.field,
.form-message,
.login-note,
footer {
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", "PingFang SC", sans-serif;
}

.masthead__eyebrow {
  color: #a33d2d;
  font-size: 11px;
  font-weight: 700;
  letter-spacing: 0.28em;
  text-transform: uppercase;
}

.masthead h1 {
  margin: 4px 0 0;
  font-size: 44px;
  font-weight: 500;
  letter-spacing: 0.12em;
}

.masthead p {
  margin: 4px 0 0;
  color: #777;
  font-size: 11px;
  letter-spacing: 0.1em;
}

.register-nav {
  display: flex;
  justify-content: space-between;
  padding: 10px 0;
  border-bottom: 3px double #191919;
  color: #777;
  font-size: 11px;
  letter-spacing: 0.12em;
}

.register-nav button,
.login-note button {
  padding: 0;
  border: 0;
  background: transparent;
  color: #a33d2d;
  font: inherit;
  font-weight: 700;
  cursor: pointer;
}

.register-content {
  display: grid;
  grid-template-columns: minmax(0, 1.08fr) minmax(400px, 0.92fr);
  min-height: calc(100vh - 260px);
  border-bottom: 1px solid #191919;
}

.register-intro {
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 54px 9% 54px 2%;
  border-right: 1px solid #dedede;
}

.issue {
  margin: 0 0 24px;
  color: #a33d2d;
  font-size: 11px;
  font-weight: 700;
  letter-spacing: 0.22em;
}

.register-intro h2 {
  margin: 0;
  font-size: clamp(42px, 5vw, 72px);
  font-weight: 500;
  line-height: 1.12;
}

.register-intro > p:last-child {
  max-width: 580px;
  margin: 30px 0 0;
  color: #555;
  font-size: 17px;
  line-height: 2;
}

.register-panel {
  align-self: center;
  width: min(100%, 520px);
  margin: 42px auto;
  padding: 34px 42px 36px;
  border: 1px solid #191919;
  box-shadow: 11px 11px 0 #eee8df;
}

.panel-heading {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  padding-bottom: 18px;
  border-bottom: 1px solid #cfcfcf;
}

.panel-heading > div {
  display: flex;
  align-items: baseline;
  gap: 12px;
}

.panel-heading span {
  color: #a33d2d;
  font-size: 25px;
  font-weight: 700;
}

.panel-heading p,
.panel-heading b {
  margin: 0;
  font-size: 10px;
  letter-spacing: 0.18em;
}

.panel-title {
  margin: 28px 0 26px;
}

.panel-title p {
  margin: 0 0 6px;
  color: #a33d2d;
  font-size: 15px;
}

.panel-title h3 {
  margin: 0;
  font-size: 32px;
  font-weight: 500;
}

.register-form {
  display: grid;
  gap: 20px;
}

.field {
  display: grid;
  gap: 8px;
  color: #555;
  font-size: 11px;
  font-weight: 700;
  letter-spacing: 0.08em;
}

.field i {
  color: #a33d2d;
  font-style: normal;
}

.field input {
  width: 100%;
  padding: 12px 2px;
  border: 0;
  border-bottom: 1px solid #999;
  border-radius: 0;
  outline: none;
  background: transparent;
  color: #151515;
  font-size: 15px;
}

.field input:focus {
  border-color: #a33d2d;
}

.form-message {
  margin: -4px 0 0;
  color: #39734f;
  font-size: 13px;
}

.form-message.error {
  color: #a33d2d;
}

.submit-button {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  margin-top: 4px;
  padding: 14px 18px;
  border: 1px solid #151515;
  background: #151515;
  color: #fff;
  cursor: pointer;
}

.submit-button:disabled {
  cursor: wait;
  opacity: 0.65;
}

.login-note {
  display: flex;
  justify-content: center;
  gap: 8px;
  margin-top: 24px;
  color: #777;
  font-size: 13px;
}

footer {
  display: flex;
  justify-content: space-between;
  padding-top: 18px;
  color: #777;
  font-size: 10px;
  letter-spacing: 0.1em;
}

@media (max-width: 980px) {
  .register-page {
    padding: 24px 40px 32px;
  }

  .register-content {
    grid-template-columns: 1fr 420px;
  }

  .register-panel {
    padding: 30px 32px 32px;
  }
}

@media (max-width: 780px) {
  .register-page {
    padding: 22px 28px 30px;
  }

  .register-content {
    grid-template-columns: 1fr;
  }

  .register-intro {
    padding: 45px 0 38px;
    border-right: 0;
    border-bottom: 1px solid #dedede;
  }

  .register-panel {
    width: calc(100% - 16px);
    margin: 42px 8px 50px 0;
  }
}

@media (max-width: 520px) {
  .register-page {
    padding: 18px 18px 26px;
  }

  .masthead h1 {
    font-size: 36px;
  }

  .register-intro h2 {
    font-size: 36px;
  }

  .register-panel {
    padding: 26px 22px 28px;
  }

  footer span:last-child {
    display: none;
  }
}
</style>
