<template>
  <div id="userLoginPage">
    <section class="login-shell">
      <div class="login-hero">
        <div class="login-hero__eyebrow">CodeSail Studio</div>
        <h1 class="login-hero__title">把想法变成可以运行的应用。</h1>
        <p class="login-hero__desc">
          保持专注，剩下的交给 AI。
          <br>登录以继续构建你的下一个作品。
        </p>
        <div class="login-hero__preview">
          <div class="login-hero__preview-glow"></div>
          <div class="login-hero__preview-card login-hero__preview-card--large"></div>
          <div class="login-hero__preview-card login-hero__preview-card--small"></div>
        </div>
      </div>

      <div class="login-card">
        <div class="login-card__header">
          <div class="login-card__badge">Welcome back</div>
          <h2 class="login-card__title">欢迎回来</h2>
          <p class="login-card__desc">登录账号，继续创建完整应用。</p>
        </div>

        <a-form :model="formState" name="normal_login" layout="vertical" class="login-form" @finish="handleSubmit">
          <a-form-item label="账号" name="userAccount" :rules="[{ required: true, message: '请输入账号!' }]">
            <a-input v-model:value="formState.userAccount" placeholder="请输入账号" size="large">
              <template #prefix>
                <UserOutlined class="site-form-item-icon" />
              </template>
            </a-input>
          </a-form-item>

          <a-form-item label="密码" name="userPassword" :rules="[
            { required: true, message: '请输入密码!' },
            { min: 8, message: '密码长度必须至少8位字符' },
          ]">
            <a-input-password v-model:value="formState.userPassword" placeholder="请输入密码" size="large">
              <template #prefix>
                <LockOutlined class="site-form-item-icon" />
              </template>
            </a-input-password>
          </a-form-item>

          <a-form-item class="login-form__helper">
            <a class="login-form-forgot" href="">忘记密码？</a>
          </a-form-item>

          <a-form-item>
            <a-button :disabled="disabled" type="primary" html-type="submit" class="login-form-button" size="large">
              登录
            </a-button>
            <div class="login-form__extra">
              <span>还没有账号？</span>
              <router-link to="/user/register">立即注册</router-link>
            </div>
          </a-form-item>
        </a-form>
      </div>
    </section>
  </div>
</template>

<script lang="ts" setup>
import { computed, onMounted, reactive } from 'vue'
import { UserOutlined, LockOutlined } from '@ant-design/icons-vue'
import { login } from '@/api/userController'
import { useLoginUserStore } from '@/stores/loginUser'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'

const REMEMBERED_USER_ACCOUNT = 'remembered-user-account'

const formState = reactive<API.UserLoginRequest>({
  userAccount: '',
  userPassword: '',
})

onMounted(() => {
  const rememberedUserAccount = localStorage.getItem(REMEMBERED_USER_ACCOUNT)

  if (rememberedUserAccount) {
    formState.userAccount = rememberedUserAccount
  }
})

const router = useRouter()
const loginUserStore = useLoginUserStore()

const handleSubmit = async (values: API.UserLoginRequest) => {
  const res = await login(values)
  // 登录成功
  if (res.data.code === 0 && res.data.data) {
    await loginUserStore.fetchLoginUser()
    message.success('登录成功')
    router.push({
      path: '/',
      replace: true,
    })
    if (values.userAccount) {
      localStorage.setItem(REMEMBERED_USER_ACCOUNT, values.userAccount)
    }
  } else {
    message.error('登录失败，' + res.data.message)
  }
  // if () {
  //   localStorage.setItem(REMEMBERED_USER_ACCOUNT, values.userAccount);
  // }
}

const disabled = computed(() => {
  return !(formState.userAccount && formState.userPassword)
})
</script>
<style scoped>
#userLoginPage {
  min-height: 100%;
  box-sizing: border-box;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 24px;
  margin: 0 auto;
}

.login-shell {
  position: relative;
  display: grid;
  grid-template-columns: minmax(0, 1fr) 430px;
  gap: 56px;
  width: 100%;
  max-width: 1120px;
  min-height: 640px;
  overflow: hidden;
  padding: 64px;
  border: 1px solid rgba(255, 255, 255, 0.72);
  border-radius: 36px;
  background:
    radial-gradient(circle at 16% 18%, rgba(174, 203, 255, 0.62), transparent 30%),
    radial-gradient(circle at 88% 24%, rgba(255, 231, 199, 0.78), transparent 28%),
    linear-gradient(135deg, rgba(255, 255, 255, 0.95), rgba(244, 247, 251, 0.86));
  box-shadow: 0 28px 80px rgba(15, 23, 42, 0.12);
}

.login-shell::before {
  position: absolute;
  inset: 18px;
  pointer-events: none;
  content: '';
  border: 1px solid rgba(255, 255, 255, 0.82);
  border-radius: 28px;
}

.login-hero {
  position: relative;
  z-index: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  min-width: 0;
}

.login-hero__eyebrow,
.login-card__badge {
  width: fit-content;
  border: 1px solid rgba(15, 23, 42, 0.08);
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.7);
  color: #536173;
  font-size: 13px;
  font-weight: 600;
  letter-spacing: 0.08em;
  line-height: 1;
  text-transform: uppercase;
}

.login-hero__eyebrow {
  padding: 9px 13px;
  margin-bottom: 24px;
}

.login-hero__title {
  max-width: 560px;
  margin: 0;
  color: #111827;
  font-size: clamp(40px, 6vw, 68px);
  font-weight: 700;
  letter-spacing: -0.07em;
  line-height: 0.98;
}

.login-hero__desc {
  max-width: 470px;
  margin: 24px 0 0;
  color: #637083;
  font-size: 18px;
  line-height: 1.8;
}

.login-hero__preview {
  position: relative;
  width: min(440px, 100%);
  height: 190px;
  margin-top: 54px;
}

.login-hero__preview-glow {
  position: absolute;
  inset: 32px 24px 0;
  border-radius: 999px;
  background: linear-gradient(90deg, rgba(54, 98, 227, 0.2), rgba(255, 191, 105, 0.28));
  filter: blur(28px);
}

.login-hero__preview-card {
  position: absolute;
  border: 1px solid rgba(255, 255, 255, 0.74);
  border-radius: 26px;
  background: rgba(255, 255, 255, 0.66);
  box-shadow: 0 24px 54px rgba(52, 64, 84, 0.14);
  backdrop-filter: blur(18px);
}

.login-hero__preview-card--large {
  top: 0;
  left: 0;
  width: 74%;
  height: 150px;
}

.login-hero__preview-card--large::before,
.login-hero__preview-card--large::after,
.login-hero__preview-card--small::before {
  position: absolute;
  content: '';
  border-radius: 999px;
}

.login-hero__preview-card--large::before {
  top: 24px;
  left: 24px;
  width: 58px;
  height: 58px;
  background: linear-gradient(135deg, #111827, #7c8798);
}

.login-hero__preview-card--large::after {
  right: 28px;
  bottom: 28px;
  width: 44%;
  height: 12px;
  background: rgba(17, 24, 39, 0.12);
  box-shadow: 0 -24px 0 rgba(17, 24, 39, 0.08);
}

.login-hero__preview-card--small {
  right: 0;
  bottom: 0;
  width: 42%;
  height: 96px;
}

.login-hero__preview-card--small::before {
  inset: 24px;
  background: linear-gradient(135deg, #f5f7fb, #cbd5e1);
}

.login-card {
  position: relative;
  z-index: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-self: center;
  width: 100%;
  padding: 40px;
  border: 1px solid rgba(255, 255, 255, 0.76);
  border-radius: 30px;
  background: rgba(255, 255, 255, 0.78);
  box-shadow: 0 24px 70px rgba(15, 23, 42, 0.11);
  backdrop-filter: blur(22px);
}

.login-card__header {
  margin-bottom: 28px;
}

.login-card__badge {
  padding: 8px 12px;
  margin-bottom: 18px;
}

.login-card__title {
  margin: 0;
  color: #111827;
  font-size: 34px;
  font-weight: 700;
  letter-spacing: -0.045em;
  line-height: 1.08;
}

.login-card__desc {
  margin: 10px 0 0;
  color: #6b7280;
  font-size: 15px;
  line-height: 1.7;
}

.login-form {
  width: 100%;
}

.login-form :deep(.ant-form-item) {
  margin-bottom: 18px;
}

.login-form :deep(.ant-form-item-label > label) {
  color: #253041;
  font-size: 14px;
  font-weight: 600;
}

.login-form :deep(.ant-input-affix-wrapper) {
  min-height: 48px;
  border: 1px solid rgba(17, 24, 39, 0.1);
  border-radius: 16px;
  background: rgba(255, 255, 255, 0.82);
  box-shadow: none;
  transition:
    border-color 0.2s ease,
    box-shadow 0.2s ease,
    background 0.2s ease;
}

.login-form :deep(.ant-input-affix-wrapper:hover),
.login-form :deep(.ant-input-affix-wrapper-focused) {
  border-color: rgba(17, 24, 39, 0.22);
  background: #fff;
  box-shadow: 0 0 0 4px rgba(17, 24, 39, 0.05);
}

.site-form-item-icon {
  color: #9ca3af;
}

.login-form__helper {
  margin-bottom: 12px;
}

.login-form-forgot {
  display: block;
  color: #4b5563;
  font-size: 14px;
  text-align: right;
  transition: color 0.2s ease;
}

.login-form-forgot:hover {
  color: #111827;
}

.login-form-button {
  width: 100%;
  height: 48px;
  border: 0;
  border-radius: 999px;
  background: #111827;
  color: #fff;
  font-weight: 600;
  box-shadow: 0 16px 34px rgba(17, 24, 39, 0.22);
  transition:
    transform 0.2s ease,
    box-shadow 0.2s ease,
    background 0.2s ease;
}

.login-form-button:not(:disabled):hover {
  background: #020617;
  transform: translateY(-1px);
  box-shadow: 0 20px 42px rgba(17, 24, 39, 0.28);
}

.login-form-button:disabled {
  background: #d1d5db;
  color: #fff;
  box-shadow: none;
}

.login-form__extra {
  margin-top: 18px;
  color: #6b7280;
  font-size: 14px;
  text-align: center;
}

.login-form__extra a {
  margin-left: 6px;
  color: #111827;
  font-weight: 600;
}

@media (max-width: 960px) {
  .login-shell {
    grid-template-columns: 1fr;
    gap: 36px;
    padding: 40px;
  }

  .login-hero {
    text-align: center;
    align-items: center;
  }

  .login-hero__preview {
    display: none;
  }
}

@media (max-width: 640px) {
  #userLoginPage {
    padding: 8px;
  }

  .login-shell {
    min-height: auto;
    padding: 28px 18px;
    border-radius: 26px;
  }

  .login-shell::before {
    display: none;
  }

  .login-hero__title {
    font-size: 38px;
  }

  .login-hero__desc {
    font-size: 16px;
  }

  .login-card {
    padding: 28px 20px;
    border-radius: 24px;
  }
}
</style>
