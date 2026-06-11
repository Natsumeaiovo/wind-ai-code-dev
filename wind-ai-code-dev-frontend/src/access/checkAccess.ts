import ACCESS_ENUM from '@/access/accessEnum'

/**
 * 权限校验
 */
const checkHasAccess = (loginUser: any, needAccesss = ACCESS_ENUM.NOT_LOGIN) => {
  // 游客即可访问
  if (needAccesss === ACCESS_ENUM.NOT_LOGIN) {
    return true
  }
  // 获取当前登录用户具有的权限
  const loginUserAccess = loginUser?.userRole ?? ACCESS_ENUM.NOT_LOGIN

  // 如果需要用户登录
  if (needAccesss === ACCESS_ENUM.USER && loginUserAccess === ACCESS_ENUM.NOT_LOGIN) {
    return false
  }
  // 如果需要管理员权限
  if (needAccesss === ACCESS_ENUM.ADMIN && loginUserAccess !== ACCESS_ENUM.ADMIN) {
    return false
  }
  return true
}

export default checkHasAccess
