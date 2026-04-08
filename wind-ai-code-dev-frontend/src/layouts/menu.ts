export interface HeaderMenuItemConfig {
  key: string
  label: string
  path: string
}

export const headerMenuItems: HeaderMenuItemConfig[] = [
  {
    key: 'home',
    label: '首页',
    path: '/',
  },
  {
    key: 'about',
    label: '关于',
    path: '/about',
  },
]

