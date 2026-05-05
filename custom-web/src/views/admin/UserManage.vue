<template>
  <div>
    <h2 style="margin-bottom: 20px; font-family: var(--f-heading)">用户管理</h2>

    <el-table :data="users" v-loading="loading" border stripe>
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="username" label="用户名" width="120" />
      <el-table-column prop="nickname" label="昵称" width="120" />
      <el-table-column prop="phone" label="手机号" width="140" />
      <el-table-column prop="email" label="邮箱" min-width="160" />
      <el-table-column prop="role" label="角色" width="90">
        <template #default="{ row }">
          <el-tag :type="row.role === 'admin' ? 'danger' : ''" size="small">
            {{ row.role === 'admin' ? '管理员' : '普通用户' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="注册时间" width="170" />
    </el-table>

    <div style="display:flex;justify-content:center;margin-top:20px">
      <el-pagination background layout="prev, pager, next" :total="total" :page-size="pageSize" v-model:current-page="currentPage" @current-change="loadUsers" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '../../api/request'

const loading = ref(false)
const users = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

async function loadUsers() {
  loading.value = true
  try {
    const res = await request.get('/admin/user/list', {
      params: { current: currentPage.value, size: pageSize.value }
    })
    users.value = res.data.records
    total.value = res.data.total
  } catch (e) {} finally {
    loading.value = false
  }
}

onMounted(() => loadUsers())
</script>
