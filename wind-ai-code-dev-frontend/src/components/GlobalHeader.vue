<script setup lang="ts">
import { computed } from 'vue'
import type { MenuProps } from 'ant-design-vue'
import { useRoute, useRouter } from 'vue-router'

import { headerMenuItems } from '@/layouts/menu'

const route = useRoute()
const router = useRouter()

const menuItems = computed<MenuProps['items']>(() =>
  headerMenuItems.map((item) => ({
    key: item.key,
    label: item.label,
  })),
)

const selectedKeys = computed(() => {
  const matched = headerMenuItems.find((item) => route.path === item.path)
  return matched ? [matched.key] : []
})

// 处理菜单点击
const handleMenuClick: MenuProps['onClick'] = ({ key }) => {
  const target = headerMenuItems.find((item) => item.key === key)

  // 跳转到对应页面
  if (target && target.path !== route.path) {
    router.push(target.path)
  }
}
</script>

<template>
  <a-layout-header class="global-header">
    <div class="global-header__left">
      <!-- logo 和标题 -->
      <div class="global-header__brand">
        <img class="global-header__logo" src="/src/assets/logo.png" alt="CodeSail Logo" />
        <span class="global-header__title">CodeSail</span>
      </div>

      <!-- 导航菜单 -->
      <a-menu
        class="global-header__menu"
        theme="light"
        mode="horizontal"
        :items="menuItems"
        :selected-keys="selectedKeys"
        @click="handleMenuClick"
      />
    </div>

    <a-button type="primary">登录</a-button>
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
  align-items: center;
  min-width: 0;
  gap: 24px;
}

.global-header__brand {
  display: flex;
  align-items: center;
  flex-shrink: 0;
  gap: 10px;
}

.global-header__logo {
  width: 32px;
  height: 32px;
}

.global-header__title {
  color: #1f1f1f;
  font-size: 18px;
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
}
</style>
