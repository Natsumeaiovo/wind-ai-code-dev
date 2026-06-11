import { getLoginUser } from '@/api/userController'
import { defineStore } from 'pinia'
import { ref } from 'vue'

/**
 * 登录用户信息 状态管理
 */
export const useLoginUserStore = defineStore('loginUser', () => {
  // 登录用户信息, 默认值为未登录
  const loginUser = ref<API.LoginUserVO>({
    userName: '未登录',
  })

  // 从后端获取登录用户信息
  async function fetchLoginUser() {
    const res = await getLoginUser()
    if (res.data.code === 0 && res.data.data) {
      loginUser.value = res.data.data
    }
  }

  // 设置登录用户信息
  function setLoginUser(newLoginUser: API.LoginUserVO) {
    loginUser.value = newLoginUser
  }

  return { loginUser, fetchLoginUser, setLoginUser }
})
