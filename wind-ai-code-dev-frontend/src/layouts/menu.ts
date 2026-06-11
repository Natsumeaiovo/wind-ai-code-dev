import ACCESS_ENUM from '@/access/accessEnum'

export interface HeaderMenuItemConfig {
  key: string
  label: string
  path: string
  meta: {
    access: string
  }
}

export const headerMenuItems: HeaderMenuItemConfig[] = [
  {
    key: 'home',
    label: '首页',
    path: '/',
    meta: { access: ACCESS_ENUM.NOT_LOGIN },
  },
  {
    key: 'userManage',
    label: '用户管理',
    path: '/admin/userManage',
    meta: { access: ACCESS_ENUM.ADMIN },
  },
  {
    key: 'about',
    label: '关于',
    path: '/about',
    meta: { access: ACCESS_ENUM.NOT_LOGIN },
  },
]
