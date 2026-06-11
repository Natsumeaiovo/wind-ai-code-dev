<template>
  <div id="userRegisterPage">
    <section class="register-shell">
      <div class="register-hero">
        <div class="register-hero__eyebrow">Start building</div>
        <h1 class="register-hero__title">开启你的第一个 AI 应用工作台。</h1>
        <p class="register-hero__desc">
          创建账号后，你可以保存并管理应用，
          <br>把灵感稳定地推进到可交付版本。
        </p>
      </div>

      <div class="register-card">
        <div class="register-card__header">
          <div class="register-card__badge">New account</div>
          <h2 class="register-card__title">创建账号</h2>
          <p class="register-card__desc">只需要一个账号，即可开始生成完整应用。</p>
        </div>

        <a-form :model="formState" name="normal_register" layout="vertical" class="register-form"
          @finish="handleSubmit">
          <a-form-item label="账号" name="userAccount" :rules="[
            { required: true, message: '请输入账号!' },
            { min: 4, message: '账号长度不能少于 4 位' },
          ]">
            <a-input v-model:value="formState.userAccount" placeholder="至少 4 位字符" size="large">
              <template #prefix>
                <UserOutlined class="site-form-item-icon" />
              </template>
            </a-input>
          </a-form-item>

          <a-form-item label="密码" name="userPassword" :rules="[
            { required: true, message: '请输入密码!' },
            { min: 8, message: '密码长度必须至少8位字符' },
          ]">
            <a-input-password v-model:value="formState.userPassword" placeholder="至少 8 位字符" size="large">
              <template #prefix>
                <LockOutlined class="site-form-item-icon" />
              </template>
            </a-input-password>
          </a-form-item>

          <a-form-item label="确认密码" name="checkPassword" :rules="[
            { required: true, message: '请再次输入密码!' },
            { min: 8, message: '确认密码长度必须至少8位字符' },
            { validator: validateCheckPassword },
          ]">
            <a-input-password v-model:value="formState.checkPassword" placeholder="再次输入密码" size="large">
              <template #prefix>
                <LockOutlined class="site-form-item-icon" />
              </template>
            </a-input-password>
          </a-form-item>

          <a-form-item>
            <a-button :disabled="disabled" type="primary" html-type="submit" class="register-form-button" size="large">
              注册
            </a-button>
            <div class="register-form__extra">
              <span>已有账号？</span>
              <router-link to="/user/login">立即登录</router-link>
            </div>
          </a-form-item>
        </a-form>
      </div>
    </section>
  </div>
</template>

<script lang="ts" setup>
import { computed, reactive } from 'vue'
import { UserOutlined, LockOutlined } from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'
import { useRouter } from 'vue-router'
import { register } from '@/api/userController'

type RegisterFormValues = Required<
  Pick<API.UserRegisterRequest, 'userAccount' | 'userPassword' | 'checkPassword'>
>

const formState = reactive<RegisterFormValues>({
  userAccount: '',
  userPassword: '',
  checkPassword: '',
})

const router = useRouter()

const validateCheckPassword = async () => {
  if (formState.checkPassword !== formState.userPassword) {
    return Promise.reject('两次输入的密码不一致')
  }
  return Promise.resolve()
}

const handleSubmit = async (values: RegisterFormValues) => {
  const res = await register(values)
  if (res.data.code === 0) {
    message.success('注册成功，请登录')
    router.push({
      path: '/user/login',
      replace: true,
    })
  } else {
    message.error('注册失败，' + res.data.message)
  }
}

const disabled = computed(() => {
  return !(formState.userAccount && formState.userPassword && formState.checkPassword)
})
</script>

<style scoped>
#userRegisterPage {
  min-height: 100%;
  box-sizing: border-box;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 24px;
  margin: 0 auto;
}

.register-shell {
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
    radial-gradient(circle at 14% 16%, rgba(211, 232, 255, 0.82), transparent 30%),
    radial-gradient(circle at 82% 18%, rgba(232, 244, 226, 0.86), transparent 30%),
    linear-gradient(135deg, rgba(255, 255, 255, 0.96), rgba(245, 247, 250, 0.88));
  box-shadow: 0 28px 80px rgba(15, 23, 42, 0.12);
}

.register-shell::before {
  position: absolute;
  inset: 18px;
  pointer-events: none;
  content: '';
  border: 1px solid rgba(255, 255, 255, 0.84);
  border-radius: 28px;
}

.register-hero {
  position: relative;
  z-index: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  min-width: 0;
}

.register-hero__eyebrow,
.register-card__badge {
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

.register-hero__eyebrow {
  padding: 9px 13px;
  margin-bottom: 24px;
}

.register-hero__title {
  max-width: 590px;
  margin: 0;
  color: #111827;
  font-size: clamp(40px, 6vw, 66px);
  font-weight: 700;
  letter-spacing: -0.07em;
  line-height: 0.98;
}

.register-hero__desc {
  max-width: 500px;
  margin: 24px 0 0;
  color: #637083;
  font-size: 18px;
  line-height: 1.8;
}

.register-card {
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

.register-card__header {
  margin-bottom: 28px;
}

.register-card__badge {
  padding: 8px 12px;
  margin-bottom: 18px;
}

.register-card__title {
  margin: 0;
  color: #111827;
  font-size: 34px;
  font-weight: 700;
  letter-spacing: -0.045em;
  line-height: 1.08;
}

.register-card__desc {
  margin: 10px 0 0;
  color: #6b7280;
  font-size: 15px;
  line-height: 1.7;
}

.register-form {
  width: 100%;
}

.register-form :deep(.ant-form-item) {
  margin-bottom: 18px;
}

.register-form :deep(.ant-form-item-label > label) {
  color: #253041;
  font-size: 14px;
  font-weight: 600;
}

.register-form :deep(.ant-input-affix-wrapper) {
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

.register-form :deep(.ant-input-affix-wrapper:hover),
.register-form :deep(.ant-input-affix-wrapper-focused) {
  border-color: rgba(17, 24, 39, 0.22);
  background: #fff;
  box-shadow: 0 0 0 4px rgba(17, 24, 39, 0.05);
}

.site-form-item-icon {
  color: #9ca3af;
}

.register-form-button {
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

.register-form-button:not(:disabled):hover {
  background: #020617;
  transform: translateY(-1px);
  box-shadow: 0 20px 42px rgba(17, 24, 39, 0.28);
}

.register-form-button:disabled {
  background: #d1d5db;
  color: #fff;
  box-shadow: none;
}

.register-form__extra {
  margin-top: 18px;
  color: #6b7280;
  font-size: 14px;
  text-align: center;
}

.register-form__extra a {
  margin-left: 6px;
  color: #111827;
  font-weight: 600;
}

@media (max-width: 960px) {
  .register-shell {
    grid-template-columns: 1fr;
    gap: 36px;
    padding: 40px;
  }

  .register-hero {
    text-align: center;
    align-items: center;
  }
}

@media (max-width: 640px) {
  #userRegisterPage {
    padding: 8px;
  }

  .register-shell {
    min-height: auto;
    padding: 28px 18px;
    border-radius: 26px;
  }

  .register-shell::before {
    display: none;
  }

  .register-hero__title {
    font-size: 38px;
  }

  .register-hero__desc {
    font-size: 16px;
  }

  .register-card {
    padding: 28px 20px;
    border-radius: 24px;
  }
}
</style>
