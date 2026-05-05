<template>
  <div class="editor-page">
    <!-- 顶部栏 -->
    <div class="editor-header">
      <div class="header-left">
        <button class="btn-back" @click="$router.back()">
          <svg width="18" height="18" viewBox="0 0 18 18" fill="none">
            <path d="M11 4L6 9l5 5" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
        </button>
        <div class="header-info">
          <h3>{{ template.name }}</h3>
          <span>{{ categoryMap[template.category] }}</span>
        </div>
      </div>
      <div class="header-actions">
        <button class="btn-outline-sm" @click="saveDraft" :disabled="saving">
          <svg width="14" height="14" viewBox="0 0 14 14" fill="none"><path d="M11 1H3a2 2 0 00-2 2v8a2 2 0 002 2h8a2 2 0 002-2V4l-2-3z" stroke="currentColor" stroke-width="1.2"/><path d="M4 1v3h5V1M4 8h6" stroke="currentColor" stroke-width="1.2" stroke-linecap="round"/></svg>
          保存草稿
        </button>
        <button class="btn-primary-sm" @click="submitOrder" :disabled="saving">
          <svg width="14" height="14" viewBox="0 0 14 14" fill="none"><path d="M1 7l4 4L13 3" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/></svg>
          提交订单
        </button>
      </div>
    </div>

    <div class="editor-body">
      <!-- 左侧画布 -->
      <div class="canvas-area">
        <div class="canvas-wrapper">
          <div
            class="canvas"
            :style="{
              width: (canvasScale * (structure.width || 280)) + 'px',
              height: (canvasScale * (structure.height || 560)) + 'px'
            }"
          >
            <!-- 背景层 -->
            <div class="layer layer-bg" :style="bgStyle"></div>

            <!-- 图片层 -->
            <div
              v-for="layer in imageLayers"
              :key="layer.key"
              class="layer layer-image"
              :class="{ active: activeLayer === layer.key }"
              :style="layerStyle(layer)"
              @click="selectLayer(layer.key)"
            >
              <img v-if="customData[layer.key]" :src="customData[layer.key]" />
              <div v-else class="image-placeholder">
                <svg width="24" height="24" viewBox="0 0 24 24" fill="none">
                  <rect x="2" y="2" width="20" height="20" rx="3" stroke="currentColor" stroke-width="1.5" opacity="0.4"/>
                  <circle cx="8" cy="8" r="2" fill="currentColor" opacity="0.3"/>
                  <path d="M2 16l5-5 3 3 4-4 8 8" stroke="currentColor" stroke-width="1.5" opacity="0.4"/>
                </svg>
                <span>点击上传</span>
              </div>
            </div>

            <!-- 文字层 -->
            <div
              v-for="layer in textLayers"
              :key="layer.key"
              class="layer layer-text"
              :class="{ active: activeLayer === layer.key }"
              :style="layerStyle(layer)"
              @click="selectLayer(layer.key)"
            >
              {{ customData[layer.key] || layer.default }}
            </div>
          </div>

          <!-- 缩放控制 -->
          <div class="zoom-controls">
            <button @click="canvasScale = Math.max(0.5, canvasScale - 0.1)">-</button>
            <span>{{ Math.round(canvasScale * 100) }}%</span>
            <button @click="canvasScale = Math.min(2, canvasScale + 0.1)">+</button>
          </div>
        </div>
      </div>

      <!-- 右侧属性面板 -->
      <div class="property-panel">
        <div class="panel-section">
          <h4 class="panel-title">
            <svg width="16" height="16" viewBox="0 0 16 16" fill="none"><path d="M2 4h12M2 8h12M2 12h8" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/></svg>
            定制内容
          </h4>

          <!-- 图片编辑区 -->
          <div v-for="layer in imageLayers" :key="layer.key" class="prop-group">
            <label class="prop-label">{{ layer.label || layer.key }}</label>
            <div class="upload-area" @click="triggerUpload(layer.key)">
              <img v-if="customData[layer.key]" :src="customData[layer.key]" class="upload-preview" />
              <div v-else class="upload-placeholder">
                <svg width="28" height="28" viewBox="0 0 28 28" fill="none">
                  <rect x="3" y="3" width="22" height="22" rx="4" stroke="currentColor" stroke-width="1.5" opacity="0.3"/>
                  <path d="M14 9v10M9 14h10" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" opacity="0.4"/>
                </svg>
                <span>点击上传图片</span>
              </div>
            </div>
            <input :ref="el => uploadRefs[layer.key] = el" type="file" accept="image/*" style="display:none" @change="handleFileChange($event, layer.key)" />
          </div>

          <!-- 文字编辑区 -->
          <div v-for="layer in textLayers" :key="layer.key" class="prop-group">
            <label class="prop-label">{{ layer.label || layer.key }}</label>
            <input
              class="prop-input"
              :value="customData[layer.key] || layer.default"
              @input="customData[layer.key] = $event.target.value"
              :placeholder="layer.default"
            />
          </div>
        </div>

        <!-- 提示 -->
        <div class="panel-tip">
          <svg width="14" height="14" viewBox="0 0 14 14" fill="none"><circle cx="7" cy="7" r="6" stroke="currentColor" stroke-width="1.2"/><path d="M7 5v3M7 9.5v.5" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/></svg>
          <span>点击画布中的元素可快速选中编辑</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getTemplateDetail } from '../api/modules/template'
import { saveCustomization } from '../api/modules/customization'
import request from '../api/request'

const route = useRoute()
const router = useRouter()
const saving = ref(false)
const canvasScale = ref(1)
const activeLayer = ref('')
const template = ref({})
const structure = ref({})
const customData = reactive({})
const uploadRefs = ref({})

const categoryMap = { ticket: '票根', acrylic: '亚克力立牌', keychain: '钥匙扣', badge: '徽章' }

// 背景渐变映射
const bgGradientMap = {
  'bg-dark-blue': 'linear-gradient(160deg, #1a1a2e, #16213e, #0f3460)',
  'bg-orange-red': 'linear-gradient(160deg, #ff6b35, #f7931e, #ff4500)',
  'bg-dark-green': 'linear-gradient(160deg, #1a3a2a, #0d4a2f, #0a3d1f)',
  'bg-cyber-dark': 'linear-gradient(160deg, #0a0a1a, #1a0a2e, #0a1a3e)',
  'bg-pastel': 'linear-gradient(160deg, #e8d5f5, #f0c4e0, #fbc2eb)',
  'bg-cream': 'linear-gradient(160deg, #fff8f0, #ffecd2, #ffe4c4)',
  'bg-light-yellow': 'linear-gradient(160deg, #fff9e6, #fff3cc, #ffe8b0)',
  'bg-pink-gradient': 'linear-gradient(160deg, #ffecd2, #fcb69f, #ff9a9e)',
  'bg-white-circle': '#ffffff',
  'bg-metal': 'linear-gradient(160deg, #434343, #636363, #434343)',
  'bg-cream-badge': 'linear-gradient(160deg, #fff8f0, #f5ede0)',
  'bg-navy-badge': 'linear-gradient(160deg, #1a1a3e, #2a2a5e, #1a1a3e)'
}

const textLayers = computed(() =>
  (structure.value.layers || []).filter(l => l.type === 'text')
)
const imageLayers = computed(() =>
  (structure.value.layers || []).filter(l => l.type === 'image')
)

const bgStyle = computed(() => {
  const bg = (structure.value.layers || []).find(l => l.type === 'background')
  if (!bg) return { background: '#f0f2f5' }
  return { background: bgGradientMap[bg.src] || '#f0f2f5' }
})

function layerStyle(layer) {
  const s = canvasScale.value
  const base = {
    left: (layer.x || 0) * s + 'px',
    top: (layer.y || 0) * s + 'px',
    width: (layer.width || 100) * s + 'px',
    height: (layer.height || 40) * s + 'px'
  }
  if (layer.type === 'text') {
    base.fontSize = (layer.fontSize || 16) * s + 'px'
    base.color = layer.color || '#ffffff'
    base.fontFamily = layer.fontFamily === 'serif' ? 'Georgia, serif' : 'inherit'
    base.textAlign = layer.align || 'center'
    if (layer.letterSpacing) base.letterSpacing = layer.letterSpacing * s + 'px'
  }
  return base
}

function selectLayer(key) {
  activeLayer.value = key
}

function triggerUpload(key) {
  uploadRefs.value[key]?.click()
}

async function handleFileChange(event, key) {
  const file = event.target.files[0]
  if (!file) return
  const formData = new FormData()
  formData.append('file', file)
  try {
    const res = await request.post('/file/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
    customData[key] = res.data
    ElMessage.success('图片上传成功')
  } catch (e) {
    ElMessage.error('上传失败')
  }
  event.target.value = ''
}

async function saveDraft() {
  saving.value = true
  try {
    const res = await saveCustomization({
      templateId: template.value.id,
      customDataJson: JSON.stringify(customData)
    })
    ElMessage.success('草稿已保存')
  } catch (e) {
    ElMessage.error('保存失败')
  } finally {
    saving.value = false
  }
}

function submitOrder() {
  // 先保存，再跳转到订单确认页
  saveDraft().then(() => {
    router.push({
      path: '/order-confirm',
      query: { templateId: template.value.id }
    })
  })
}

onMounted(async () => {
  try {
    const res = await getTemplateDetail(route.params.id)
    template.value = res.data
    try {
      structure.value = JSON.parse(res.data.structureJson)
    } catch {
      structure.value = {}
    }
    // 初始化自定义数据
    for (const layer of structure.value.layers || []) {
      if (layer.key) {
        customData[layer.key] = layer.default || ''
      }
    }
  } catch (e) {
    ElMessage.error('加载模板失败')
  }
})
</script>

<style scoped>
.editor-page {
  height: calc(100vh - 64px);
  display: flex;
  flex-direction: column;
  background: var(--c-cream);
}

/* ---- Header ---- */
.editor-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 20px;
  background: #fff;
  border-bottom: 1px solid var(--c-border);
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.btn-back {
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: none;
  border: 1px solid var(--c-border);
  border-radius: var(--r-sm);
  cursor: pointer;
  color: var(--c-text-secondary);
  transition: all 0.2s;
}

.btn-back:hover {
  border-color: var(--c-orange);
  color: var(--c-orange);
}

.header-info h3 {
  font-size: 15px;
  font-weight: 600;
  color: var(--c-brown);
}

.header-info span {
  font-size: 12px;
  color: var(--c-text-muted);
}

.header-actions {
  display: flex;
  gap: 10px;
}

.btn-outline-sm {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 8px 16px;
  font-size: 13px;
  font-weight: 500;
  color: var(--c-text);
  background: none;
  border: 1px solid var(--c-border);
  border-radius: var(--r-sm);
  cursor: pointer;
  transition: all 0.2s;
  font-family: var(--f-body);
}

.btn-outline-sm:hover {
  border-color: var(--c-orange);
  color: var(--c-orange);
}

.btn-primary-sm {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 8px 20px;
  font-size: 13px;
  font-weight: 600;
  color: #fff;
  background: var(--c-orange);
  border: none;
  border-radius: var(--r-sm);
  cursor: pointer;
  transition: all 0.25s var(--ease-bounce);
  font-family: var(--f-body);
}

.btn-primary-sm:hover {
  background: var(--c-orange-dark);
}

/* ---- Body ---- */
.editor-body {
  flex: 1;
  display: flex;
  overflow: hidden;
}

/* ---- Canvas ---- */
.canvas-area {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  background-image:
    radial-gradient(circle, var(--c-border) 1px, transparent 1px);
  background-size: 20px 20px;
}

.canvas-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
}

.canvas {
  position: relative;
  border-radius: var(--r-md);
  box-shadow: 0 8px 32px rgba(45, 24, 16, 0.12);
  overflow: hidden;
}

.layer {
  position: absolute;
}

.layer-bg {
  inset: 0;
  z-index: 0;
}

.layer-image {
  z-index: 1;
  border: 2px dashed transparent;
  border-radius: var(--r-sm);
  cursor: pointer;
  transition: border-color 0.2s;
  overflow: hidden;
}

.layer-image:hover,
.layer-image.active {
  border-color: var(--c-orange);
}

.layer-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.image-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 4px;
  background: rgba(255, 255, 255, 0.15);
  backdrop-filter: blur(2px);
  color: rgba(255, 255, 255, 0.6);
  font-size: 11px;
}

.layer-text {
  z-index: 2;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  border: 2px dashed transparent;
  border-radius: 4px;
  transition: border-color 0.2s;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.2);
  word-break: break-all;
  overflow: hidden;
}

.layer-text:hover,
.layer-text.active {
  border-color: var(--c-gold);
}

.zoom-controls {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 13px;
  color: var(--c-text-muted);
}

.zoom-controls button {
  width: 28px;
  height: 28px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #fff;
  border: 1px solid var(--c-border);
  border-radius: var(--r-sm);
  cursor: pointer;
  font-size: 16px;
  color: var(--c-text-secondary);
  transition: all 0.2s;
}

.zoom-controls button:hover {
  border-color: var(--c-orange);
  color: var(--c-orange);
}

/* ---- Property Panel ---- */
.property-panel {
  width: 340px;
  background: #fff;
  border-left: 1px solid var(--c-border);
  overflow-y: auto;
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.panel-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 15px;
  font-weight: 600;
  color: var(--c-brown);
  margin-bottom: 16px;
}

.prop-group {
  margin-bottom: 16px;
}

.prop-label {
  display: block;
  font-size: 13px;
  font-weight: 500;
  color: var(--c-text-secondary);
  margin-bottom: 8px;
}

.upload-area {
  border: 2px dashed var(--c-border);
  border-radius: var(--r-md);
  overflow: hidden;
  cursor: pointer;
  transition: all 0.2s;
  min-height: 120px;
}

.upload-area:hover {
  border-color: var(--c-orange);
  background: rgba(232, 114, 42, 0.02);
}

.upload-preview {
  width: 100%;
  max-height: 160px;
  object-fit: cover;
  display: block;
}

.upload-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 24px;
  color: var(--c-text-muted);
  font-size: 13px;
  gap: 8px;
}

.prop-input {
  width: 100%;
  padding: 10px 14px;
  font-size: 14px;
  border: 1px solid var(--c-border);
  border-radius: var(--r-sm);
  outline: none;
  transition: border-color 0.2s;
  font-family: var(--f-body);
  color: var(--c-text);
  background: var(--c-cream);
}

.prop-input:focus {
  border-color: var(--c-orange);
  background: #fff;
}

.panel-tip {
  display: flex;
  align-items: flex-start;
  gap: 8px;
  font-size: 12px;
  color: var(--c-text-muted);
  padding: 12px;
  background: var(--c-cream);
  border-radius: var(--r-sm);
  line-height: 1.5;
}

.panel-tip svg {
  flex-shrink: 0;
  margin-top: 1px;
}
</style>
