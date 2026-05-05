<template>
  <div class="template-page">
    <div class="page-container">
      <div class="page-header">
        <h1 class="page-title">模板中心</h1>
        <p class="page-desc">找到你喜欢的风格，开始创作专属纪念品</p>
      </div>

      <!-- 分类筛选 -->
      <div class="filter-bar">
        <button
          v-for="cat in categoryOptions"
          :key="cat.value"
          class="filter-btn"
          :class="{ active: selectedCategory === cat.value }"
          @click="selectCategory(cat.value)"
        >
          <span class="filter-icon" v-if="cat.icon">{{ cat.icon }}</span>
          {{ cat.label }}
        </button>
      </div>

      <!-- 模板列表 -->
      <div class="template-grid" v-loading="loading">
        <div
          v-for="(item, i) in templates"
          :key="item.id"
          class="template-card"
          :style="{ animationDelay: `${i * 0.06}s` }"
          @click="$router.push(`/template/${item.id}`)"
        >
          <div class="template-preview">
            <div class="template-preview-inner">
              <svg width="48" height="48" viewBox="0 0 48 48" fill="none">
                <rect x="4" y="8" width="40" height="32" rx="4" stroke="var(--c-text-muted)" stroke-width="2" opacity="0.3" />
                <path d="M4 16h40" stroke="var(--c-text-muted)" stroke-width="2" opacity="0.2" />
                <circle cx="16" cy="12" r="2" fill="var(--c-text-muted)" opacity="0.2" />
                <circle cx="24" cy="12" r="2" fill="var(--c-text-muted)" opacity="0.2" />
              </svg>
              <span class="preview-label">{{ categoryMap[item.category] }}</span>
            </div>
            <div class="template-tag" v-if="item.sortOrder <= 3">热门</div>
          </div>
          <div class="template-info">
            <div class="template-meta">
              <h4>{{ item.name }}</h4>
              <span class="template-cat">{{ categoryMap[item.category] }}</span>
            </div>
            <span class="price">¥{{ item.price }}</span>
          </div>
        </div>
      </div>

      <!-- 分页 -->
      <div class="pagination-wrap" v-if="total > pageSize">
        <el-pagination
          background
          layout="prev, pager, next"
          :total="total"
          :page-size="pageSize"
          :current-page="currentPage"
          @current-change="handlePageChange"
        />
      </div>

      <!-- 空状态 -->
      <el-empty v-if="!loading && templates.length === 0" description="暂无模板" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getTemplateList } from '../api/modules/template'

const route = useRoute()
const loading = ref(false)
const selectedCategory = ref(route.query.category || '')
const templates = ref([])
const currentPage = ref(1)
const pageSize = ref(12)
const total = ref(0)

const categoryMap = {
  ticket: '票根',
  acrylic: '亚克力立牌',
  keychain: '钥匙扣',
  badge: '徽章'
}

const categoryOptions = [
  { value: '', label: '全部', icon: '' },
  { value: 'ticket', label: '票根', icon: '' },
  { value: 'acrylic', label: '亚克力立牌', icon: '' },
  { value: 'keychain', label: '钥匙扣', icon: '' },
  { value: 'badge', label: '徽章', icon: '' }
]

function selectCategory(val) {
  selectedCategory.value = val
  currentPage.value = 1
  loadTemplates()
}

function handlePageChange(page) {
  currentPage.value = page
  loadTemplates()
}

async function loadTemplates() {
  loading.value = true
  try {
    const res = await getTemplateList({
      category: selectedCategory.value || undefined,
      current: currentPage.value,
      size: pageSize.value
    })
    templates.value = res.data.records
    total.value = res.data.total
  } catch (e) {
    // error handled by interceptor
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadTemplates()
})
</script>

<style scoped>
.template-page {
  padding-bottom: 60px;
}

.page-header {
  padding: 32px 0 8px;
}

.page-title {
  font-family: var(--f-display);
  font-size: 32px;
  color: var(--c-brown);
  margin-bottom: 6px;
}

.page-desc {
  font-size: 15px;
  color: var(--c-text-muted);
}

.filter-bar {
  display: flex;
  gap: 10px;
  margin: 24px 0 32px;
  flex-wrap: wrap;
}

.filter-btn {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 10px 22px;
  font-size: 14px;
  font-weight: 500;
  color: var(--c-text-secondary);
  background: var(--c-card);
  border: 1px solid var(--c-border);
  border-radius: 40px;
  cursor: pointer;
  transition: all 0.25s var(--ease-smooth);
  font-family: var(--f-body);
}

.filter-btn:hover {
  color: var(--c-orange);
  border-color: var(--c-orange-light);
  background: rgba(232, 114, 42, 0.04);
}

.filter-btn.active {
  color: #fff;
  background: var(--c-orange);
  border-color: var(--c-orange);
  box-shadow: 0 4px 12px rgba(232, 114, 42, 0.25);
}

.template-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  min-height: 300px;
}

.template-card {
  background: var(--c-card);
  border-radius: var(--r-lg);
  overflow: hidden;
  cursor: pointer;
  transition: all 0.35s var(--ease-bounce);
  border: 1px solid rgba(45, 24, 16, 0.04);
  animation: fadeUp 0.5s var(--ease-smooth) both;
}

.template-card:hover {
  transform: translateY(-6px);
  box-shadow: var(--shadow-lg);
  border-color: transparent;
}

.template-preview {
  height: 220px;
  background: linear-gradient(135deg, var(--c-cream-dark), var(--c-cream));
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}

.template-preview-inner {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
}

.preview-label {
  font-size: 13px;
  color: var(--c-text-muted);
  font-weight: 500;
}

.template-tag {
  position: absolute;
  top: 12px;
  right: 12px;
  font-size: 11px;
  font-weight: 600;
  color: #fff;
  background: var(--c-orange);
  padding: 4px 12px;
  border-radius: 20px;
}

.template-info {
  padding: 16px 18px;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}

.template-meta h4 {
  font-size: 14px;
  font-weight: 600;
  color: var(--c-brown);
  margin-bottom: 4px;
}

.template-cat {
  font-size: 12px;
  color: var(--c-text-muted);
}

.price {
  font-family: var(--f-heading);
  font-size: 18px;
  font-weight: 700;
  color: var(--c-orange);
  flex-shrink: 0;
}

.pagination-wrap {
  display: flex;
  justify-content: center;
  margin-top: 40px;
}
</style>
