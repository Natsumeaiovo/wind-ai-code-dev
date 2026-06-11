<script setup lang="ts">
import { useLoginUserStore } from '@/stores/loginUser'

const loginUserStore = useLoginUserStore()
</script>

<template>
  <main class="user-center-page">
    <a-card title="个人中心">
      <a-descriptions :column="1" bordered>
        <a-descriptions-item label="用户名">
          {{ loginUserStore.loginUser.userName ?? '无名' }}
        </a-descriptions-item>
        <a-descriptions-item label="用户 ID">
          {{ loginUserStore.loginUser.id ?? '-' }}
        </a-descriptions-item>
        <a-descriptions-item label="角色">
          {{ loginUserStore.loginUser.userRole ?? '-' }}
        </a-descriptions-item>
      </a-descriptions>
    </a-card>
  </main>
</template>

<style scoped>
.user-center-page {
  width: 100%;
}
</style>
