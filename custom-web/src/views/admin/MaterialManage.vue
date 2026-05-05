<template>
  <div>
    <div style="display: flex; justify-content: space-between; margin-bottom: 20px">
      <h2 style="font-family: var(--f-heading)">素材管理</h2>
      <el-button type="primary" @click="openAdd">新增素材</el-button>
    </div>

    <div style="display: flex; gap: 12px; margin-bottom: 16px">
      <el-select v-model="filterType" placeholder="类型筛选" clearable @change="loadMaterials" style="width: 140px">
        <el-option label="背景" value="background" />
        <el-option label="边框" value="border" />
        <el-option label="装饰" value="decoration" />
        <el-option label="文字" value="text" />
      </el-select>
      <el-select v-model="filterCategory" placeholder="分类筛选" clearable @change="loadMaterials" style="width: 140px">
        <el-option label="票根" value="ticket" />
        <el-option label="亚克力" value="acrylic" />
        <el-option label="钥匙扣" value="keychain" />
        <el-option label="徽章" value="badge" />
      </el-select>
    </div>

    <el-table :data="materials" v-loading="loading" border stripe>
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column label="预览" width="80">
        <template #default="{ row }">
          <img :src="row.url" style="width: 50px; height: 50px; object-fit: cover; border-radius: 4px" />
        </template>
      </el-table-column>
      <el-table-column prop="name" label="名称" min-width="120" />
      <el-table-column prop="type" label="类型" width="80">
        <template #default="{ row }">
          <el-tag size="small">{{ typeMap[row.type] || row.type }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="category" label="分类" width="80">
        <template #default="{ row }">
          {{ categoryMap[row.category] || '通用' }}
        </template>
      </el-table-column>
      <el-table-column prop="sortOrder" label="排序" width="70" />
      <el-table-column label="操作" width="150">
        <template #default="{ row }">
          <el-button link type="primary" size="small" @click="openEdit(row)">编辑</el-button>
          <el-button link type="danger" size="small" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div style="display:flex;justify-content:center;margin-top:20px">
      <el-pagination background layout="prev, pager, next" :total="total" :page-size="pageSize" v-model:current-page="currentPage" @current-change="loadMaterials" />
    </div>

    <!-- 新增/编辑对话框 -->
    <el-dialog v-model="showDialog" :title="editId ? '编辑素材' : '新增素材'" width="500">
      <el-form :model="form" label-width="80px">
        <el-form-item label="名称">
          <el-input v-model="form.name" placeholder="素材名称" />
        </el-form-item>
        <el-form-item label="类型">
          <el-select v-model="form.type">
            <el-option label="背景" value="background" />
            <el-option label="边框" value="border" />
            <el-option label="装饰" value="decoration" />
            <el-option label="文字" value="text" />
          </el-select>
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="form.category" clearable>
            <el-option label="通用" value="" />
            <el-option label="票根" value="ticket" />
            <el-option label="亚克力" value="acrylic" />
            <el-option label="钥匙扣" value="keychain" />
            <el-option label="徽章" value="badge" />
          </el-select>
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.sortOrder" :min="0" />
        </el-form-item>
        <el-form-item label="素材图片">
          <el-upload
            :action="'/api/file/upload'"
            :headers="uploadHeaders"
            :show-file-list="false"
            :on-success="handleUploadSuccess"
            accept="image/*"
          >
            <img v-if="form.url" :src="form.url" style="width: 120px; height: 120px; object-fit: cover; border-radius: 4px" />
            <div v-else style="width: 120px; height: 120px; border: 1px dashed #ccc; border-radius: 4px; display: flex; align-items: center; justify-content: center; color: #999; font-size: 13px">
              点击上传
            </div>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showDialog = false">取消</el-button>
        <el-button type="primary" @click="handleSave">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { adminGetMaterials, adminSaveMaterial, adminDeleteMaterial } from '../../api/modules/admin'

const loading = ref(false)
const showDialog = ref(false)
const editId = ref(null)
const materials = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const filterType = ref('')
const filterCategory = ref('')

const typeMap = { background: '背景', border: '边框', decoration: '装饰', text: '文字' }
const categoryMap = { ticket: '票根', acrylic: '亚克力', keychain: '钥匙扣', badge: '徽章' }

const uploadHeaders = computed(() => ({
  Authorization: localStorage.getItem('token') ? `Bearer ${localStorage.getItem('token')}` : ''
}))

const form = reactive({
  name: '',
  type: 'background',
  category: '',
  url: '',
  sortOrder: 0
})

function openAdd() {
  editId.value = null
  form.name = ''
  form.type = 'background'
  form.category = ''
  form.url = ''
  form.sortOrder = 0
  showDialog.value = true
}

function openEdit(row) {
  editId.value = row.id
  form.name = row.name
  form.type = row.type
  form.category = row.category || ''
  form.url = row.url
  form.sortOrder = row.sortOrder
  showDialog.value = true
}

function handleUploadSuccess(res) {
  form.url = res.data
  ElMessage.success('上传成功')
}

async function loadMaterials() {
  loading.value = true
  try {
    const params = { current: currentPage.value, size: pageSize.value }
    if (filterType.value) params.type = filterType.value
    if (filterCategory.value) params.category = filterCategory.value
    const res = await adminGetMaterials(params)
    materials.value = res.data.records
    total.value = res.data.total
  } catch (e) {} finally {
    loading.value = false
  }
}

async function handleSave() {
  if (!form.name) { ElMessage.warning('请输入素材名称'); return }
  if (!form.url) { ElMessage.warning('请上传素材图片'); return }
  try {
    const data = { ...form }
    if (editId.value) data.id = editId.value
    await adminSaveMaterial(data)
    ElMessage.success(editId.value ? '更新成功' : '添加成功')
    showDialog.value = false
    loadMaterials()
  } catch (e) {}
}

async function handleDelete(row) {
  await ElMessageBox.confirm('确定删除该素材？', '提示', { type: 'warning' })
  try {
    await adminDeleteMaterial(row.id)
    ElMessage.success('删除成功')
    loadMaterials()
  } catch (e) {}
}

onMounted(() => loadMaterials())
</script>
