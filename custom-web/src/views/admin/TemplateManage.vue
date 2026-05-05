<template>
  <div>
    <div style="display: flex; justify-content: space-between; margin-bottom: 20px">
      <h2 style="font-family: var(--f-heading)">模板管理</h2>
      <el-button type="primary" @click="openAdd">新增模板</el-button>
    </div>

    <el-table :data="templates" v-loading="loading" border stripe>
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="name" label="模板名称" min-width="150" />
      <el-table-column prop="category" label="分类" width="100">
        <template #default="{ row }">
          <el-tag size="small">{{ categoryMap[row.category] || row.category }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="price" label="价格" width="80">
        <template #default="{ row }">¥{{ row.price }}</template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">
            {{ row.status === 1 ? '上架' : '下架' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="220">
        <template #default="{ row }">
          <el-button link type="primary" size="small" @click="handleToggle(row)">
            {{ row.status === 1 ? '下架' : '上架' }}
          </el-button>
          <el-button link type="danger" size="small" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div style="display:flex;justify-content:center;margin-top:20px">
      <el-pagination background layout="prev, pager, next" :total="total" :page-size="pageSize" v-model:current-page="currentPage" @current-change="loadTemplates" />
    </div>

    <!-- 新增模板对话框 -->
    <el-dialog v-model="showAdd" title="新增模板" width="500">
      <el-form :model="addForm" label-width="80px">
        <el-form-item label="名称">
          <el-input v-model="addForm.name" placeholder="如：演唱会票根" />
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
          <el-input v-model="addForm.description" type="textarea" :rows="3" />
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
import { ElMessage, ElMessageBox } from 'element-plus'
import { adminGetTemplates, adminSaveTemplate, adminDeleteTemplate, adminUpdateTemplateStatus } from '../../api/modules/admin'

const loading = ref(false)
const showAdd = ref(false)
const templates = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const categoryMap = { ticket: '票根', acrylic: '亚克力', keychain: '钥匙扣', badge: '徽章' }

const addForm = reactive({
  name: '',
  category: 'ticket',
  price: 0,
  description: '',
  previewUrl: '/uploads/templates/default.png',
  structureJson: '{}'
})

function openAdd() {
  addForm.name = ''
  addForm.category = 'ticket'
  addForm.price = 0
  addForm.description = ''
  showAdd.value = true
}

async function loadTemplates() {
  loading.value = true
  try {
    const res = await adminGetTemplates({ current: currentPage.value, size: pageSize.value })
    templates.value = res.data.records
    total.value = res.data.total
  } catch (e) {} finally {
    loading.value = false
  }
}

async function handleAdd() {
  if (!addForm.name) { ElMessage.warning('请输入模板名称'); return }
  try {
    await adminSaveTemplate(addForm)
    ElMessage.success('添加成功')
    showAdd.value = false
    loadTemplates()
  } catch (e) {}
}

async function handleToggle(row) {
  try {
    await adminUpdateTemplateStatus(row.id, row.status === 1 ? 0 : 1)
    ElMessage.success('操作成功')
    loadTemplates()
  } catch (e) {}
}

async function handleDelete(row) {
  await ElMessageBox.confirm('确定删除该模板？', '提示', { type: 'warning' })
  try {
    await adminDeleteTemplate(row.id)
    ElMessage.success('删除成功')
    loadTemplates()
  } catch (e) {}
}

onMounted(() => loadTemplates())
</script>
