/**
 * 全局权限校验
 * 1. 首先判断页面是否需要登录权限，如果不需要直接放行
 * 2. 如果页面需要登录权限：
 * 3. 如果用户未登录，跳转到登录页面
 * 4. 如果已登录，判断登录用户权限是否符合要求，不符合不跳转并提示用户没有权限
 */
import router from '@/router'
import ACCESS_ENUM from './accessEnum'
import { useLoginUserStore } from '@/stores/loginUser'
import checkHasAccess from './checkAccess'
import { message } from 'ant-design-vue'

router.beforeEach(async (to, from, next) => {
  const loginUserStore = useLoginUserStore()
  let loginUser = loginUserStore.loginUser
  if (!loginUser || !loginUser.userRole) {
    // 自动登录
    await loginUserStore.fetchLoginUser()
    console.log('fetch user')
    loginUser = loginUserStore.loginUser
  }
  const needAccesss = (to.meta?.access as string) ?? ACCESS_ENUM.NOT_LOGIN
  // 要跳转的页面必须要登录
  if (needAccesss !== ACCESS_ENUM.NOT_LOGIN) {
    // 如果用户未登录，跳转到登录页面
    if (!loginUser || !loginUser.userRole) {
      next(`/user/login?redirect=${to.fullPath}`)
      return
    }
    // 如果用户已登录，判断登录用户权限是否符合要求
    if (!checkHasAccess(loginUser, needAccesss)) {
      message.error('无权限访问')
      next(false)
      return
    }
  }
  // 放行
  next()
})
