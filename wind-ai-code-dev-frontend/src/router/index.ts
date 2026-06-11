import { createRouter, createWebHistory } from 'vue-router'
import HomePage from '@/pages/HomePage.vue'
import AboutPage from '@/pages/AboutPage.vue'
import UserLoginPage from '@/pages/user/UserLoginPage.vue'
import UserRegisterPage from '@/pages/user/UserRegisterPage.vue'
import UserCenterPage from '@/pages/user/UserCenterPage.vue'
import UserManagePage from '@/pages/admin/UserManagePage.vue'
import UserAppManagePage from '@/pages/user/UserAppManagePage.vue'
import ACCESS_ENUM from '@/access/accessEnum'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      // 首页
      path: '/',
      name: 'home',
      component: HomePage,
    },
    {
      // 关于页
      path: '/about',
      name: 'about',
      component: AboutPage,
    },
    {
      // 用户登录页
      path: '/user/login',
      name: 'userLogin',
      component: UserLoginPage,
    },
    {
      // 用户注册页
      path: '/user/register',
      name: 'userRegister',
      component: UserRegisterPage,
    },
    {
      // 用户中心页
      path: '/user/center',
      name: 'userCenter',
      component: UserCenterPage,
      meta: {
        access: ACCESS_ENUM.USER,
      },
    },
    {
      // 应用管理页
      path: '/app/manage',
      name: 'appManage',
      component: UserAppManagePage,
      meta: {
        access: ACCESS_ENUM.USER,
      },
    },
    {
      // 用户管理页
      path: '/admin/userManage',
      name: 'userManage',
      component: UserManagePage,
      meta: {
        access: ACCESS_ENUM.ADMIN,
      },
    },
  ],
})

export default router
