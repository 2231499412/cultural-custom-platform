<template>
  <div class="page-container">
    <h1 class="page-title">个人中心</h1>

    <el-card>
      <el-form :model="form" label-width="80px" style="max-width: 500px">
        <el-form-item label="头像">
          <el-avatar :size="64" :src="form.avatar || ''">
            {{ form.nickname?.[0] || 'U' }}
          </el-avatar>
        </el-form-item>
        <el-form-item label="用户名">
          <el-input v-model="form.username" disabled />
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="form.nickname" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" />
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
  email: userStore.userInfo.email || '',
  avatar: userStore.userInfo.avatar || ''
})

function handleSave() {
  // TODO: 接入接口
  ElMessage.success('保存成功')
}
</script>

<style scoped>
.page-title {
  font-size: 28px;
  margin: 20px 0;
}
</style>
