<script setup lang="ts">
import { computed } from 'vue'
import type { MenuProps } from 'ant-design-vue'
import { message } from 'ant-design-vue'
import { RouterLink, useRoute, useRouter } from 'vue-router'

import logoUrl from '@/assets/logo.png'
import { logout } from '@/api/userController'
import { headerMenuItems } from '@/layouts/menu'
import { useLoginUserStore } from '@/stores/loginUser'
import ACCESS_ENUM from '@/access/accessEnum'
import checkHasAccess from '@/access/checkAccess'

// 登录用户状态
const loginUserStore = useLoginUserStore()
const route = useRoute()
const router = useRouter()

// 根据当前用户角色过滤可见菜单
const filteredHeaderMenuItems = computed(() => {
  return headerMenuItems.filter((item) =>
    checkHasAccess(loginUserStore.loginUser, item.meta.access ?? ACCESS_ENUM.NOT_LOGIN),
  )
})

// 导航菜单项
const menuItems = computed<MenuProps['items']>(() =>
  filteredHeaderMenuItems.value.map((item) => ({
    key: item.key,
    label: item.label,
  })),
)

// 创建菜单键到路径的映射
const keyToPathMap = computed(
  () => new Map(filteredHeaderMenuItems.value.map((item) => [item.key, item.path])),
)
// 创建路径到菜单键的映射
const pathToKeyMap = computed(
  () => new Map(filteredHeaderMenuItems.value.map((item) => [item.path, item.key])),
)

// 计算选中的菜单键
const selectedKeys = computed(() => {
  const directMatch = pathToKeyMap.value.get(route.path)
  if (directMatch) {
    return [directMatch]
  }
  // 查找嵌套菜单匹配
  const nestedMatch = filteredHeaderMenuItems.value.find(
    (item) => item.path !== '/' && route.path.startsWith(`${item.path}/`),
  )
  return nestedMatch ? [nestedMatch.key] : []
})

// 处理菜单点击事件
const handleMenuClick: MenuProps['onClick'] = ({ key }) => {
  const targetPath = keyToPathMap.value.get(String(key))
  if (targetPath && targetPath !== route.path) {
    router.push(targetPath)
  }
}

// 处理退出登录事件
const handleLogout = async () => {
  try {
    const res = await logout()
    if (res.data.code === 0) {
      loginUserStore.setLoginUser({
        userName: '未登录',
      })
      message.success('已退出登录')
      router.push('/user/login')
      return
    }
    message.error(res.data.message || '退出登录失败')
  } catch (error) {
    message.error('退出登录失败，' + error)
  }
}
</script>

<template>
  <a-layout-header class="global-header">
    <!-- 左侧内容 -->
    <div class="global-header__left">
      <div class="global-header__brand">
        <img class="global-header__logo" :src="logoUrl" alt="CodeSail Logo" />
        <span class="global-header__title">CodeSail</span>
      </div>
      <!-- 导航菜单 -->
      <a-menu class="global-header__menu" theme="light" mode="horizontal" :items="menuItems"
        :selected-keys="selectedKeys" @click="handleMenuClick" />
    </div>

    <!-- 右侧内容 -->
    <div class="global-header__right">
      <!-- 用户信息 -->
      <div class="global-header__user">
        <div v-if="loginUserStore.loginUser.id" class="global-header__profile">
          <RouterLink class="global-header__profile-trigger" to="/user/center">
            <a-avatar class="global-header__avatar" :size="45" :src="loginUserStore.loginUser.userAvatar" />
            <span class="global-header__user-name">
              {{ loginUserStore.loginUser.userName ?? '无名' }}
            </span>
          </RouterLink>
          <!-- 用户信息面板 -->
          <div class="global-header__profile-panel">
            <!-- 用户信息 -->
            <RouterLink class="global-header__profile-info" to="/user/center">
              <span class="global-header__profile-name">
                {{ loginUserStore.loginUser.userName ?? '无名' }}
              </span>
            </RouterLink>
            <!-- 面板菜单 -->
            <div class="global-header__profile-menu">
              <RouterLink class="global-header__profile-menu-item" to="/user/center">
                <span class="global-header__profile-menu-icon" aria-hidden="true">
                  <svg viewBox="0 0 24 24" fill="none">
                    <path d="M20 21a8 8 0 0 0-16 0" />
                    <circle cx="12" cy="7" r="4" />
                  </svg>
                </span>
                <span class="global-header__profile-menu-label">个人中心</span>
                <span class="global-header__profile-menu-arrow" aria-hidden="true"></span>
              </RouterLink>
              <RouterLink class="global-header__profile-menu-item" to="/app/manage">
                <span class="global-header__profile-menu-icon" aria-hidden="true">
                  <svg viewBox="0 0 24 24" fill="none">
                    <rect x="4" y="4" width="7" height="7" rx="1.5" />
                    <rect x="13" y="4" width="7" height="7" rx="1.5" />
                    <rect x="4" y="13" width="7" height="7" rx="1.5" />
                    <path d="M16.5 14v5" />
                    <path d="M14 16.5h5" />
                  </svg>
                </span>
                <span class="global-header__profile-menu-label">应用管理</span>
                <span class="global-header__profile-menu-arrow" aria-hidden="true"></span>
              </RouterLink>
              <button type="button" class="global-header__profile-menu-item global-header__profile-menu-item--danger"
                @click="handleLogout">
                <span class="global-header__profile-menu-icon" aria-hidden="true">
                  <svg viewBox="0 0 24 24" fill="none">
                    <path d="M10 17l5-5-5-5" />
                    <path d="M15 12H3" />
                    <path d="M14 4h5a2 2 0 0 1 2 2v12a2 2 0 0 1-2 2h-5" />
                  </svg>
                </span>
                <span class="global-header__profile-menu-label">退出登录</span>
                <span class="global-header__profile-menu-arrow" aria-hidden="true"></span>
              </button>
            </div>
          </div>
        </div>
        <div v-else>
          <a-button type="primary" href="/user/login">登录</a-button>
        </div>
      </div>
    </div>
  </a-layout-header>
</template>

<style scoped>
.global-header {
  position: sticky;
  top: 0;
  z-index: 20;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 64px;
  padding-inline: 24px;
  background: #ffffff;
  border-bottom: 1px solid #f0f0f0;
}

.global-header__left {
  display: flex;
  flex: 1;
  align-items: center;
  min-width: 0;
  gap: 24px;
}

.global-header__right {
  display: flex;
  align-items: center;
  flex-shrink: 0;
  margin-left: 24px;
}

.global-header__brand {
  display: flex;
  align-items: center;
  flex-shrink: 0;
  gap: 10px;
}

.global-header__logo {
  width: 40px;
  height: 40px;
}

.global-header__title {
  color: #1f1f1f;
  font-size: 22px;
  font-weight: 600;
  line-height: 1;
}

.global-header__menu {
  min-width: 0;
  flex: 1;
  background: transparent;
  border-bottom: 0;
  color: #1f1f1f;
}

.global-header__menu :deep(.ant-menu-title-content) {
  font-size: 16px;
}

.global-header__user {
  display: flex;
  align-items: center;
  height: 64px;
  line-height: 1;
}

.global-header__profile {
  --global-header-avatar-size: 45px;
  position: relative;
  display: flex;
  align-items: center;
  height: 64px;
  margin-right: 42px;
}

.global-header__profile-trigger {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 0;
  border: 0;
  background: transparent;
  color: #1f1f1f;
  cursor: pointer;
  text-decoration: none;
}

.global-header__avatar {
  position: relative;
  z-index: 3;
  flex-shrink: 0;
  transform-origin: top center;
  transition:
    transform 0.2s ease,
    box-shadow 0.2s ease;
}

.global-header__profile:hover .global-header__avatar {
  transform: scale(2);
  box-shadow: 0 12px 28px rgb(24 61 121 / 22%);
}

.global-header__user-name {
  max-width: 120px;
  overflow: hidden;
  font-size: 14px;
  line-height: 20px;
  text-overflow: ellipsis;
  white-space: nowrap;
  transition: opacity 0.18s ease;
}

.global-header__profile:hover .global-header__user-name {
  opacity: 0;
}

.global-header__profile-panel {
  position: absolute;
  z-index: 2;
  top: calc(100% - 2px);
  left: calc(var(--global-header-avatar-size) / 2);
  width: 260px;
  padding: 50px 14px 12px;
  border: 1px solid #eef0f4;
  border-radius: 8px;
  background: #ffffff;
  box-shadow: 0 12px 36px rgb(18 38 63 / 16%);
  opacity: 0;
  pointer-events: none;
  transform: translate(-50%, 8px);
  visibility: hidden;
  transition:
    opacity 0.18s ease,
    transform 0.18s ease,
    visibility 0.18s ease;
}

.global-header__profile-panel::before {
  position: absolute;
  top: -12px;
  right: 0;
  left: 0;
  height: 12px;
  content: '';
}

.global-header__profile:hover .global-header__profile-panel,
.global-header__profile-trigger:focus-visible+.global-header__profile-panel {
  opacity: 1;
  pointer-events: auto;
  transform: translate(-50%, 0);
  visibility: visible;
}

.global-header__profile-info {
  display: flex;
  width: 100%;
  flex-direction: column;
  align-items: center;
  padding: 0 0 12px;
  border: 0;
  border-bottom: 1px solid #f0f0f0;
  background: transparent;
  color: #1f1f1f;
  cursor: pointer;
}

.global-header__profile-name {
  max-width: 200px;
  overflow: hidden;
  font-size: 18px;
  font-weight: 600;
  line-height: 22px;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.global-header__profile-menu {
  display: flex;
  flex-direction: column;
  gap: 2px;
  padding-top: 8px;
}

.global-header__profile-menu-item {
  display: flex;
  align-items: center;
  gap: 12px;
  width: 100%;
  padding: 10px 12px;
  border: 0;
  border-radius: 6px;
  background: transparent;
  color: #1f1f1f;
  cursor: pointer;
  font-size: 14px;
  line-height: 20px;
  text-align: left;
  text-decoration: none;
  transition:
    background 0.18s ease,
    color 0.18s ease;
}

.global-header__profile-menu-icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 22px;
  height: 22px;
  flex-shrink: 0;
  color: #5f6b7a;
}

.global-header__profile-menu-icon svg {
  width: 20px;
  height: 20px;
  stroke: currentcolor;
  stroke-linecap: round;
  stroke-linejoin: round;
  stroke-width: 2;
}

.global-header__profile-menu-label {
  min-width: 0;
  flex: 1;
}

.global-header__profile-menu-arrow {
  width: 8px;
  height: 8px;
  flex-shrink: 0;
  border-top: 2px solid currentcolor;
  border-right: 2px solid currentcolor;
  opacity: 0.65;
  transform: rotate(45deg);
}

.global-header__profile-menu-item:hover {
  background: #f5f7fb;
  color: #1677ff;
}

.global-header__profile-menu-item--danger:hover {
  background: #fff1f0;
  color: #ff4d4f;
}

@media (max-width: 768px) {
  .global-header {
    height: auto;
    align-items: flex-start;
    flex-direction: column;
    gap: 8px;
    padding-block: 8px;
  }

  .global-header__left {
    width: 100%;
    align-items: flex-start;
    flex-direction: column;
    gap: 8px;
  }

  .global-header__right {
    width: 100%;
    margin-left: 0;
  }

  .global-header__profile {
    width: max-content;
  }
}
</style>
