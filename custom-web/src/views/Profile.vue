<template>
  <div class="page-container">
    <h1 class="page-title">个人中心</h1>

    <el-card style="border-radius: var(--r-lg)">
      <div class="profile-header">
        <div class="profile-avatar">
          {{ form.nickname?.[0] || 'U' }}
        </div>
        <div>
          <h3 style="font-family: var(--f-heading); margin-bottom: 4px">{{ form.nickname || form.username }}</h3>
          <span style="font-size: 13px; color: var(--c-text-muted)">@{{ form.username }}</span>
        </div>
      </div>

      <el-divider />

      <el-form :model="form" label-width="80px" style="max-width: 500px">
        <el-form-item label="用户名">
          <el-input v-model="form.username" disabled />
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="form.nickname" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="form.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSave">保存修改</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../store'

const userStore = useUserStore()

const form = reactive({
  username: userStore.userInfo.username || '',
  nickname: userStore.userInfo.nickname || '',
  phone: userStore.userInfo.phone || '',
  email: userStore.userInfo.email || ''
})

function handleSave() {
  // TODO: 调用更新接口
  ElMessage.success('保存成功')
}
</script>

<style scoped>
.page-title {
  font-family: var(--f-display);
  font-size: 28px;
  color: var(--c-brown);
  margin: 20px 0;
}

.profile-header {
  display: flex;
  align-items: center;
  gap: 16px;
}

.profile-avatar {
  width: 64px;
  height: 64px;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--c-orange), var(--c-orange-light));
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  font-weight: 600;
}
</style>
