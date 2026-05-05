<template>
  <div class="detail-page">
    <div class="page-container">
      <div class="breadcrumb">
        <span @click="$router.push('/templates')">模板中心</span>
        <svg width="12" height="12" viewBox="0 0 12 12" fill="none">
          <path d="M4 2l4 4-4 4" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" />
        </svg>
        <span>{{ categoryMap[template.category] || '' }}</span>
      </div>

      <div class="detail-content" v-loading="loading">
        <div class="preview-section">
          <div class="preview-card">
            <div class="preview-inner">
              <svg width="80" height="80" viewBox="0 0 80 80" fill="none">
                <rect x="8" y="14" width="64" height="52" rx="6" stroke="var(--c-text-muted)" stroke-width="2" opacity="0.2" />
                <path d="M8 26h64" stroke="var(--c-text-muted)" stroke-width="2" opacity="0.15" />
                <circle cx="22" cy="20" r="3" fill="var(--c-text-muted)" opacity="0.15" />
                <circle cx="34" cy="20" r="3" fill="var(--c-text-muted)" opacity="0.15" />
              </svg>
              <span>{{ categoryMap[template.category] }}预览</span>
            </div>
          </div>
        </div>

        <div class="info-section">
          <div class="info-tag">{{ categoryMap[template.category] }}</div>
          <h1 class="detail-title">{{ template.name }}</h1>
          <p class="detail-desc">{{ template.description }}</p>

          <div class="detail-price-row">
            <span class="detail-price">¥{{ template.price }}</span>
            <span class="detail-unit">/ 件</span>
          </div>

          <div class="detail-features">
            <div class="feature-item">
              <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
                <circle cx="8" cy="8" r="7" stroke="var(--c-sage)" stroke-width="1.5" />
                <path d="M5 8l2 2 4-4" stroke="var(--c-sage)" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
              </svg>
              <span>支持自定义图片和文字</span>
            </div>
            <div class="feature-item">
              <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
                <circle cx="8" cy="8" r="7" stroke="var(--c-sage)" stroke-width="1.5" />
                <path d="M5 8l2 2 4-4" stroke="var(--c-sage)" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
              </svg>
              <span>实时预览定制效果</span>
            </div>
            <div class="feature-item">
              <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
                <circle cx="8" cy="8" r="7" stroke="var(--c-sage)" stroke-width="1.5" />
                <path d="M5 8l2 2 4-4" stroke="var(--c-sage)" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
              </svg>
              <span>3-5 个工作日发货</span>
            </div>
          </div>

          <div class="detail-actions">
            <button class="btn-primary-lg" @click="startCustomize">
              <svg width="18" height="18" viewBox="0 0 18 18" fill="none">
                <path d="M3 14l3-3m0 0l6-6 3 3-6 6m-3-3l-1.5 4.5L6 14" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
              </svg>
              开始定制
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getTemplateDetail } from '../api/modules/template'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const template = ref({})

const categoryMap = {
  ticket: '票根',
  acrylic: '亚克力立牌',
  keychain: '钥匙扣',
  badge: '徽章'
}

async function loadTemplate() {
  loading.value = true
  try {
    const res = await getTemplateDetail(route.params.id)
    template.value = res.data
  } catch (e) {
    // error handled
  } finally {
    loading.value = false
  }
}

function startCustomize() {
  router.push(`/editor/${route.params.id}`)
}

onMounted(() => {
  loadTemplate()
})
</script>

<style scoped>
.detail-page {
  padding-bottom: 60px;
}

.breadcrumb {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 20px 0;
  font-size: 14px;
  color: var(--c-text-muted);
}

.breadcrumb span:first-child {
  cursor: pointer;
  color: var(--c-text-secondary);
  transition: color 0.2s;
}

.breadcrumb span:first-child:hover {
  color: var(--c-orange);
}

.detail-content {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 48px;
  min-height: 400px;
}

.preview-card {
  background: linear-gradient(135deg, var(--c-cream-dark), var(--c-cream));
  border-radius: var(--r-lg);
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 440px;
  border: 1px solid var(--c-border);
}

.preview-inner {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  color: var(--c-text-muted);
  font-size: 14px;
}

.info-tag {
  display: inline-block;
  font-size: 12px;
  font-weight: 600;
  letter-spacing: 2px;
  color: var(--c-orange);
  background: rgba(232, 114, 42, 0.08);
  padding: 5px 14px;
  border-radius: 20px;
  margin-bottom: 16px;
}

.detail-title {
  font-family: var(--f-display);
  font-size: 30px;
  color: var(--c-brown);
  margin-bottom: 12px;
  line-height: 1.3;
}

.detail-desc {
  font-size: 15px;
  color: var(--c-text-secondary);
  line-height: 1.7;
  margin-bottom: 28px;
}

.detail-price-row {
  margin-bottom: 28px;
  padding-bottom: 28px;
  border-bottom: 1px solid var(--c-border);
}

.detail-price {
  font-family: var(--f-heading);
  font-size: 36px;
  font-weight: 700;
  color: var(--c-orange);
}

.detail-unit {
  font-size: 14px;
  color: var(--c-text-muted);
  margin-left: 4px;
}

.detail-features {
  display: flex;
  flex-direction: column;
  gap: 14px;
  margin-bottom: 32px;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 14px;
  color: var(--c-text-secondary);
}

.detail-actions {
  display: flex;
  gap: 16px;
}

.btn-primary-lg {
  display: inline-flex;
  align-items: center;
  gap: 10px;
  padding: 16px 40px;
  font-size: 16px;
  font-weight: 600;
  color: #fff;
  background: var(--c-orange);
  border: none;
  border-radius: var(--r-md);
  cursor: pointer;
  transition: all 0.3s var(--ease-bounce);
  font-family: var(--f-body);
  box-shadow: 0 4px 16px rgba(232, 114, 42, 0.3);
}

.btn-primary-lg:hover {
  background: var(--c-orange-dark);
  transform: translateY(-2px);
  box-shadow: 0 8px 28px rgba(232, 114, 42, 0.4);
}
</style>
