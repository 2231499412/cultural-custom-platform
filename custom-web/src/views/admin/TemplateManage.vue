<template>
  <div>
    <div style="display: flex; justify-content: space-between; margin-bottom: 20px">
      <h2>模板管理</h2>
      <el-button type="primary" @click="showAdd = true">新增模板</el-button>
    </div>

    <el-table :data="templates" v-loading="loading" border>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="模板名称" />
      <el-table-column prop="category" label="分类" width="120">
        <template #default="{ row }">
          {{ categoryMap[row.category] || row.category }}
        </template>
      </el-table-column>
      <el-table-column prop="price" label="价格" width="100" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'">
            {{ row.status === 1 ? '上架' : '下架' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template #default="{ row }">
          <el-button link type="primary" size="small">编辑</el-button>
          <el-button link type="danger" size="small">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 新增模板对话框 -->
    <el-dialog v-model="showAdd" title="新增模板" width="500">
      <el-form :model="addForm" label-width="80px">
        <el-form-item label="名称">
          <el-input v-model="addForm.name" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="addForm.category">
            <el-option label="票根" value="ticket" />
            <el-option label="亚克力立牌" value="acrylic" />
            <el-option label="钥匙扣" value="keychain" />
            <el-option label="徽章" value="badge" />
          </el-select>
        </el-form-item>
        <el-form-item label="价格">
          <el-input-number v-model="addForm.price" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="addForm.description" type="textarea" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAdd = false">取消</el-button>
        <el-button type="primary" @click="handleAdd">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'

const loading = ref(false)
const showAdd = ref(false)
const templates = ref([])

const categoryMap = {
  ticket: '票根',
  acrylic: '亚克力立牌',
  keychain: '钥匙扣',
  badge: '徽章'
}

const addForm = reactive({
  name: '',
  category: 'ticket',
  price: 0,
  description: ''
})

function handleAdd() {
  // TODO: 接口
  showAdd.value = false
}

onMounted(() => {
  // TODO: 加载模板列表
})
</script>
