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
          <span>{{ categoryMap[template.category] }} · {{ shapeLabel }}</span>
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
      <!-- 左侧：素材库 -->
      <div class="material-panel">
        <div class="material-tabs">
          <button
            v-for="tab in materialTabs"
            :key="tab.key"
            class="mat-tab"
            :class="{ active: activeTab === tab.key }"
            @click="activeTab = tab.key"
          >{{ tab.label }}</button>
        </div>

        <div class="material-grid" v-loading="loadingMaterials">
          <!-- 文字模板 -->
          <template v-if="activeTab === 'text'">
            <div
              v-for="item in textPresets"
              :key="item.label"
              class="mat-item text-preset"
              @click="addTextLayer(item)"
            >
              <span :style="{ fontSize: '14px', fontFamily: item.fontFamily === 'serif' ? 'Georgia, serif' : 'inherit', color: item.color }">{{ item.label }}</span>
            </div>
          </template>

          <!-- 图片素材 -->
          <template v-else>
            <div
              v-for="mat in filteredMaterials"
              :key="mat.id"
              class="mat-item"
              @click="addMaterialLayer(mat)"
            >
              <img :src="mat.url" :alt="mat.name" />
              <span>{{ mat.name }}</span>
            </div>
            <div v-if="filteredMaterials.length === 0 && !loadingMaterials" class="mat-empty">
              暂无素材
            </div>
          </template>
        </div>

        <!-- 上传用户图片 -->
        <div class="material-upload">
          <input ref="userUploadRef" type="file" accept="image/*" style="display:none" @change="handleUserUpload" />
          <button class="btn-upload" @click="userUploadRef?.click()">
            <svg width="16" height="16" viewBox="0 0 16 16" fill="none"><path d="M8 2v8M4 6l4-4 4 4" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/><path d="M2 12v1a1 1 0 001 1h10a1 1 0 001-1v-1" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/></svg>
            上传我的图片
          </button>
        </div>
      </div>

      <!-- 中间：画布 -->
      <div class="canvas-area" @mousedown.self="deselectAll">
        <div class="canvas-wrapper">
          <div
            class="canvas"
            :class="['canvas-' + (template.shape || 'rect')]"
            :style="canvasSizeStyle"
            @mousedown.self="deselectAll"
          >
            <div
              v-for="layer in layers"
              :key="layer.id"
              class="canvas-layer"
              :class="{ active: selectedLayerId === layer.id, dragging: draggingId === layer.id, 'layer-background': layer.type === 'background' }"
              :style="layerCanvasStyle(layer)"
              @mousedown.stop="layer.type !== 'background' ? startDrag($event, layer) : null"
            >
              <!-- 背景/图片/装饰/边框图层 -->
              <img v-if="layer.type !== 'text'" :src="layer.src" draggable="false" :style="layerImgStyle(layer)" />
              <!-- 文字图层 -->
              <div v-else class="layer-text-content" :style="layerTextStyle(layer)">
                {{ layer.content }}
              </div>

              <!-- 选中时的拖动把手 -->
              <div v-if="selectedLayerId === layer.id" class="layer-handles">
                <div class="handle handle-tl" @mousedown.stop="startResize($event, layer, 'tl')"></div>
                <div class="handle handle-tr" @mousedown.stop="startResize($event, layer, 'tr')"></div>
                <div class="handle handle-bl" @mousedown.stop="startResize($event, layer, 'bl')"></div>
                <div class="handle handle-br" @mousedown.stop="startResize($event, layer, 'br')"></div>
              </div>
            </div>
          </div>

          <!-- 缩放控制 -->
          <div class="zoom-controls">
            <button @click="canvasScale = Math.max(0.5, canvasScale - 0.1)">−</button>
            <span>{{ Math.round(canvasScale * 100) }}%</span>
            <button @click="canvasScale = Math.min(2, canvasScale + 0.1)">+</button>
          </div>
        </div>
      </div>

      <!-- 右侧：图层管理 -->
      <div class="layer-panel">
        <div class="layer-header">
          <span class="layer-title">图层列表</span>
          <button class="btn-add-text" @click="addTextLayer(textPresets[0])" title="添加文字">
            <svg width="14" height="14" viewBox="0 0 14 14" fill="none"><path d="M2 3h10M7 3v9M5 12h4" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/></svg>
          </button>
        </div>

        <div class="layer-list">
          <div
            v-for="(layer, index) in layers.slice().reverse()"
            :key="layer.id"
            class="layer-item"
            :class="{ active: selectedLayerId === layer.id }"
            @click="selectedLayerId = layer.id"
          >
            <span class="layer-icon">{{ layerTypeIcon(layer.type) }}</span>
            <span class="layer-name">{{ layerName(layer) }}</span>
            <div class="layer-actions">
              <button @click.stop="moveLayer(layer.id, 1)" :disabled="index === 0" title="上移">↑</button>
              <button @click.stop="moveLayer(layer.id, -1)" :disabled="index === layers.length - 1" title="下移">↓</button>
              <button @click.stop="removeLayer(layer.id)" class="btn-delete" title="删除">×</button>
            </div>
          </div>
          <div v-if="layers.length === 0" class="layer-empty">
            从左侧素材库添加元素
          </div>
        </div>

        <!-- 选中图层属性 -->
        <div v-if="selectedLayer" class="layer-props">
          <div class="prop-title">属性编辑</div>

          <!-- 图片类图层属性 -->
          <template v-if="selectedLayer.type !== 'text'">
            <div class="prop-row">
              <label>大小</label>
              <div class="prop-size">
                <input type="number" v-model.number="selectedLayer.width" @change="clampLayer(selectedLayer)" min="20" />
                <span>×</span>
                <input type="number" v-model.number="selectedLayer.height" @change="clampLayer(selectedLayer)" min="20" />
              </div>
            </div>

            <!-- 图片编辑控件（仅 image 类型） -->
            <template v-if="selectedLayer.type === 'image'">
              <div class="image-controls">
                <div class="control-row">
                  <label>缩放 <span class="control-val">{{ selectedLayer.scale || 100 }}%</span></label>
                  <el-slider
                    :model-value="selectedLayer.scale || 100"
                    @update:model-value="selectedLayer.scale = $event"
                    :min="10" :max="500" :step="1"
                    :show-tooltip="false"
                    size="small"
                  />
                </div>
                <div class="control-row">
                  <label>旋转 <span class="control-val">{{ selectedLayer.rotation || 0 }}°</span></label>
                  <el-slider
                    :model-value="selectedLayer.rotation || 0"
                    @update:model-value="selectedLayer.rotation = $event"
                    :min="0" :max="360" :step="1"
                    :show-tooltip="false"
                    size="small"
                  />
                </div>
              </div>

              <div class="action-grid">
                <button class="action-btn" @click="maximizeHeight(selectedLayer)" title="高度最大化">
                  <svg width="16" height="16" viewBox="0 0 16 16" fill="none"><path d="M8 2v12M4 6l4-4 4 4M4 10l4 4 4-4" stroke="currentColor" stroke-width="1.3" stroke-linecap="round" stroke-linejoin="round"/></svg>
                  高度最大化
                </button>
                <button class="action-btn" @click="maximizeWidth(selectedLayer)" title="水平最大化">
                  <svg width="16" height="16" viewBox="0 0 16 16" fill="none"><path d="M2 8h12M6 4l-4 4 4 4M10 4l4 4-4 4" stroke="currentColor" stroke-width="1.3" stroke-linecap="round" stroke-linejoin="round"/></svg>
                  水平最大化
                </button>
                <button class="action-btn" @click="selectedLayer.flipX = !selectedLayer.flipX" :class="{ active: selectedLayer.flipX }" title="水平翻转">
                  <svg width="16" height="16" viewBox="0 0 16 16" fill="none"><path d="M8 2v12" stroke="currentColor" stroke-width="1.3" stroke-linecap="round"/><path d="M6 4L3 8l3 4" stroke="currentColor" stroke-width="1.3" stroke-linecap="round" stroke-linejoin="round"/><path d="M10 4l3 4-3 4" stroke="currentColor" stroke-width="1.3" stroke-linecap="round" stroke-linejoin="round"/></svg>
                  水平翻转
                </button>
                <button class="action-btn" @click="selectedLayer.flipY = !selectedLayer.flipY" :class="{ active: selectedLayer.flipY }" title="垂直翻转">
                  <svg width="16" height="16" viewBox="0 0 16 16" fill="none"><path d="M2 8h12" stroke="currentColor" stroke-width="1.3" stroke-linecap="round"/><path d="M4 6L8 3l4 3" stroke="currentColor" stroke-width="1.3" stroke-linecap="round" stroke-linejoin="round"/><path d="M4 10l4 3 4-3" stroke="currentColor" stroke-width="1.3" stroke-linecap="round" stroke-linejoin="round"/></svg>
                  垂直翻转
                </button>
                <button class="action-btn" @click="rotateCCW(selectedLayer)" title="逆时针旋转">
                  <svg width="16" height="16" viewBox="0 0 16 16" fill="none"><path d="M3 8a5 5 0 019.5-2" stroke="currentColor" stroke-width="1.3" stroke-linecap="round"/><path d="M12.5 3.5V6H10" stroke="currentColor" stroke-width="1.3" stroke-linecap="round" stroke-linejoin="round"/><path d="M5 10a5 5 0 007 4.5" stroke="currentColor" stroke-width="1.3" stroke-linecap="round"/></svg>
                  逆时针
                </button>
                <button class="action-btn" @click="rotateCW(selectedLayer)" title="顺时针旋转">
                  <svg width="16" height="16" viewBox="0 0 16 16" fill="none"><path d="M13 8a5 5 0 00-9.5-2" stroke="currentColor" stroke-width="1.3" stroke-linecap="round"/><path d="M3.5 3.5V6H6" stroke="currentColor" stroke-width="1.3" stroke-linecap="round" stroke-linejoin="round"/><path d="M11 10a5 5 0 01-7 4.5" stroke="currentColor" stroke-width="1.3" stroke-linecap="round"/></svg>
                  顺时针
                </button>
              </div>
            </template>

            <button class="btn-replace" @click="replaceImage(selectedLayer)">
              替换图片
            </button>
          </template>

          <!-- 文字图层属性 -->
          <template v-else>
            <div class="prop-row">
              <label>内容</label>
              <input class="prop-input" v-model="selectedLayer.content" placeholder="输入文字" />
            </div>
            <div class="prop-row">
              <label>字号</label>
              <input type="number" class="prop-input small" v-model.number="selectedLayer.fontSize" min="10" max="72" />
            </div>
            <div class="prop-row">
              <label>颜色</label>
              <input type="color" class="prop-color" v-model="selectedLayer.color" />
            </div>
          </template>

          <div class="prop-row">
            <label>位置</label>
            <div class="prop-size">
              <input type="number" v-model.number="selectedLayer.x" />
              <span>,</span>
              <input type="number" v-model.number="selectedLayer.y" />
            </div>
          </div>
        </div>

        <div class="layer-tip">
          <svg width="14" height="14" viewBox="0 0 14 14" fill="none"><circle cx="7" cy="7" r="6" stroke="currentColor" stroke-width="1.2"/><path d="M7 5v3M7 9.5v.5" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/></svg>
          <span>拖动画布上的元素调整位置，点击选中后可编辑属性</span>
        </div>
      </div>
    </div>

    <!-- 隐藏的替换图片上传 -->
    <input ref="replaceUploadRef" type="file" accept="image/*" style="display:none" @change="handleReplaceUpload" />
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, onBeforeUnmount } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getTemplateDetail } from '../api/modules/template'
import { getMaterials } from '../api/modules/material'
import { saveCustomization } from '../api/modules/customization'
import request from '../api/request'

const route = useRoute()
const router = useRouter()
const saving = ref(false)
const canvasScale = ref(1)
const template = ref({})
const layers = reactive([])
const selectedLayerId = ref(null)
const draggingId = ref(null)
const replaceUploadRef = ref(null)
const userUploadRef = ref(null)
const replaceTargetId = ref(null)

// 素材库
const activeTab = ref('background')
const allMaterials = ref([])
const loadingMaterials = ref(false)

const categoryMap = { ticket: '票根', acrylic: '亚克力', keychain: '钥匙扣', badge: '徽章' }
const shapeMap = { rect: '长方形', circle: '圆形', roundRect: '圆角矩形' }
const shapeLabel = computed(() => shapeMap[template.value.shape] || '长方形')

const materialTabs = [
  { key: 'background', label: '底图' },
  { key: 'border', label: '边框' },
  { key: 'decoration', label: '装饰' },
  { key: 'text', label: '文字' }
]

const textPresets = [
  { label: '标题文字', content: '标题文字', fontSize: 24, color: '#ffffff', fontFamily: 'serif', letterSpacing: 2 },
  { label: '副标题', content: '副标题文字', fontSize: 16, color: '#ffffff', fontFamily: 'sans', letterSpacing: 1 },
  { label: '正文内容', content: '正文内容文字', fontSize: 14, color: '#cccccc', fontFamily: 'sans', letterSpacing: 0 },
  { label: '日期', content: '2025.01.01', fontSize: 12, color: '#aaaaaa', fontFamily: 'sans', letterSpacing: 1 }
]

const filteredMaterials = computed(() => {
  const cat = template.value.category
  return allMaterials.value.filter(m => m.type === activeTab.value && (!m.category || m.category === cat))
})

const selectedLayer = computed(() => layers.find(l => l.id === selectedLayerId.value))

const canvasSizeStyle = computed(() => {
  const s = canvasScale.value
  const struct = parseStructure()
  const w = struct.width || 280
  const h = struct.height || 280
  return {
    width: (s * w) + 'px',
    height: (s * h) + 'px'
  }
})

function parseStructure() {
  try {
    return typeof template.value.structureJson === 'string'
      ? JSON.parse(template.value.structureJson)
      : (template.value.structureJson || {})
  } catch { return {} }
}

let layerCounter = 0
function nextId() {
  return 'layer_' + Date.now() + '_' + (++layerCounter)
}

// 获取图片真实尺寸
function getImageSize(url) {
  return new Promise((resolve) => {
    const img = new Image()
    img.onload = () => resolve({ width: img.naturalWidth, height: img.naturalHeight })
    img.onerror = () => resolve({ width: 100, height: 100 })
    img.src = url
  })
}

// 按比例缩放图片尺寸，使其适配画布
function fitToCanvas(natW, natH, canvasW, canvasH, maxRatio) {
  const maxW = canvasW * maxRatio
  const maxH = canvasH * maxRatio
  const scale = Math.min(maxW / natW, maxH / natH, 1)
  return {
    width: Math.round(natW * scale),
    height: Math.round(natH * scale)
  }
}

// 添加素材图层
async function addMaterialLayer(mat) {
  const struct = parseStructure()
  const cw = struct.width || 280
  const ch = struct.height || 280

  if (mat.type === 'background') {
    const existing = layers.find(l => l.type === 'background')
    if (existing) {
      existing.src = mat.url
      selectedLayerId.value = existing.id
      return
    }
    layers.unshift({
      id: nextId(),
      type: 'background',
      src: mat.url,
      name: mat.name,
      x: 0, y: 0,
      width: cw, height: ch,
      zIndex: layers.length
    })
  } else {
    // 获取素材真实尺寸，按比例适配画布
    const nat = await getImageSize(mat.url)
    const maxRatio = mat.type === 'border' ? 0.9 : 0.5
    const fitted = fitToCanvas(nat.width, nat.height, cw, ch, maxRatio)
    layers.push({
      id: nextId(),
      type: mat.type,
      src: mat.url,
      name: mat.name,
      x: Math.round((cw - fitted.width) / 2),
      y: Math.round((ch - fitted.height) / 2),
      width: fitted.width,
      height: fitted.height,
      zIndex: layers.length,
      scale: 100,
      rotation: 0,
      flipX: false,
      flipY: false
    })
  }
  selectedLayerId.value = layers[layers.length - 1]?.id
}

// 添加文字图层
function addTextLayer(preset) {
  const struct = parseStructure()
  const w = struct.width || 280
  const h = struct.height || 280

  layers.push({
    id: nextId(),
    type: 'text',
    content: preset.content,
    name: preset.label,
    fontSize: preset.fontSize,
    color: preset.color,
    fontFamily: preset.fontFamily,
    letterSpacing: preset.letterSpacing,
    x: Math.round(w * 0.1),
    y: Math.round(h * 0.4),
    width: Math.round(w * 0.8),
    height: preset.fontSize + 20,
    zIndex: layers.length
  })
  selectedLayerId.value = layers[layers.length - 1]?.id
}

// 上传用户图片
async function handleUserUpload(event) {
  const file = event.target.files[0]
  if (!file) return
  const url = await uploadFile(file)
  if (url) {
    const struct = parseStructure()
    const cw = struct.width || 280
    const ch = struct.height || 280
    const nat = await getImageSize(url)
    const fitted = fitToCanvas(nat.width, nat.height, cw, ch, 0.6)
    layers.push({
      id: nextId(),
      type: 'image',
      src: url,
      name: '我的图片',
      x: Math.round((cw - fitted.width) / 2),
      y: Math.round((ch - fitted.height) / 2),
      width: fitted.width,
      height: fitted.height,
      zIndex: layers.length,
      scale: 100,
      rotation: 0,
      flipX: false,
      flipY: false
    })
    selectedLayerId.value = layers[layers.length - 1]?.id
    ElMessage.success('图片已添加到画布')
  }
  event.target.value = ''
}

// 替换图片
function replaceImage(layer) {
  replaceTargetId.value = layer.id
  replaceUploadRef.value?.click()
}

async function handleReplaceUpload(event) {
  const file = event.target.files[0]
  if (!file) return
  const url = await uploadFile(file)
  if (url) {
    const layer = layers.find(l => l.id === replaceTargetId.value)
    if (layer) {
      layer.src = url
      ElMessage.success('图片已替换')
    }
  }
  event.target.value = ''
}

async function uploadFile(file) {
  const formData = new FormData()
  formData.append('file', file)
  try {
    const res = await request.post('/file/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
    return res.data
  } catch (e) {
    ElMessage.error('上传失败')
    return null
  }
}

// 图层操作
function removeLayer(id) {
  const idx = layers.findIndex(l => l.id === id)
  if (idx !== -1) {
    layers.splice(idx, 1)
    if (selectedLayerId.value === id) selectedLayerId.value = null
    reindexZ()
  }
}

function moveLayer(id, direction) {
  const idx = layers.findIndex(l => l.id === id)
  const targetIdx = idx + direction
  if (targetIdx < 0 || targetIdx >= layers.length) return
  const temp = layers[idx]
  layers[idx] = layers[targetIdx]
  layers[targetIdx] = temp
  reindexZ()
}

function reindexZ() {
  layers.forEach((l, i) => { l.zIndex = i })
  // 底图始终保持最顶层
  const bg = layers.find(l => l.type === 'background')
  if (bg) bg.zIndex = layers.length
}

function deselectAll() {
  selectedLayerId.value = null
}

function clampLayer(layer) {
  layer.width = Math.max(20, layer.width || 20)
  layer.height = Math.max(20, layer.height || 20)
}

// 图片编辑操作
function maximizeHeight(layer) {
  const struct = parseStructure()
  const canvasH = struct.height || 280
  const scale = (layer.scale || 100) / 100
  const currentVisualH = layer.height * scale
  if (currentVisualH <= 0) return
  const newScale = Math.round((canvasH / layer.height) * 100)
  layer.scale = Math.min(500, Math.max(10, newScale))
}

function maximizeWidth(layer) {
  const struct = parseStructure()
  const canvasW = struct.width || 280
  const scale = (layer.scale || 100) / 100
  const currentVisualW = layer.width * scale
  if (currentVisualW <= 0) return
  const newScale = Math.round((canvasW / layer.width) * 100)
  layer.scale = Math.min(500, Math.max(10, newScale))
}

function rotateCCW(layer) {
  layer.rotation = ((layer.rotation || 0) - 90 + 360) % 360
}

function rotateCW(layer) {
  layer.rotation = ((layer.rotation || 0) + 90) % 360
}

// 拖动
let dragStart = { x: 0, y: 0 }
let dragLayerStart = { x: 0, y: 0 }

function startDrag(e, layer) {
  selectedLayerId.value = layer.id
  draggingId.value = layer.id
  dragStart = { x: e.clientX, y: e.clientY }
  dragLayerStart = { x: layer.x, y: layer.y }

  const onMove = (ev) => {
    const dx = (ev.clientX - dragStart.x) / canvasScale.value
    const dy = (ev.clientY - dragStart.y) / canvasScale.value
    layer.x = Math.round(dragLayerStart.x + dx)
    layer.y = Math.round(dragLayerStart.y + dy)
  }

  const onUp = () => {
    draggingId.value = null
    document.removeEventListener('mousemove', onMove)
    document.removeEventListener('mouseup', onUp)
  }

  document.addEventListener('mousemove', onMove)
  document.addEventListener('mouseup', onUp)
}

// 缩放（拖拽角点）
function startResize(e, layer, corner) {
  e.stopPropagation()
  const startX = e.clientX
  const startY = e.clientY
  const startW = layer.width
  const startH = layer.height
  const startLX = layer.x
  const startLY = layer.y

  const onMove = (ev) => {
    const dx = (ev.clientX - startX) / canvasScale.value
    const dy = (ev.clientY - startY) / canvasScale.value

    if (corner === 'br') {
      layer.width = Math.max(20, Math.round(startW + dx))
      layer.height = Math.max(20, Math.round(startH + dy))
    } else if (corner === 'bl') {
      layer.width = Math.max(20, Math.round(startW - dx))
      layer.height = Math.max(20, Math.round(startH + dy))
      layer.x = Math.round(startLX + dx)
    } else if (corner === 'tr') {
      layer.width = Math.max(20, Math.round(startW + dx))
      layer.height = Math.max(20, Math.round(startH - dy))
      layer.y = Math.round(startLY + dy)
    } else if (corner === 'tl') {
      layer.width = Math.max(20, Math.round(startW - dx))
      layer.height = Math.max(20, Math.round(startH - dy))
      layer.x = Math.round(startLX + dx)
      layer.y = Math.round(startLY + dy)
    }
  }

  const onUp = () => {
    document.removeEventListener('mousemove', onMove)
    document.removeEventListener('mouseup', onUp)
  }

  document.addEventListener('mousemove', onMove)
  document.addEventListener('mouseup', onUp)
}

// 画布上的图层样式
function layerCanvasStyle(layer) {
  const s = canvasScale.value
  const style = {
    left: (layer.x || 0) * s + 'px',
    top: (layer.y || 0) * s + 'px',
    width: (layer.width || 100) * s + 'px',
    height: (layer.height || 100) * s + 'px',
    zIndex: layer.zIndex || 0
  }
  // 非背景图层应用旋转和翻转变换
  if (layer.type !== 'background') {
    const rotation = layer.rotation || 0
    const flipX = layer.flipX ? -1 : 1
    const flipY = layer.flipY ? -1 : 1
    if (rotation || layer.flipX || layer.flipY) {
      style.transform = `rotate(${rotation}deg) scaleX(${flipX}) scaleY(${flipY})`
    }
  }
  return style
}

// 图片元素样式（应用缩放）
function layerImgStyle(layer) {
  if (layer.type === 'background') return {}
  const scale = (layer.scale || 100) / 100
  return {
    width: (layer.width || 100) * scale + 'px',
    height: (layer.height || 100) * scale + 'px',
    objectFit: 'contain'
  }
}

function layerTextStyle(layer) {
  const s = canvasScale.value
  return {
    fontSize: (layer.fontSize || 16) * s + 'px',
    color: layer.color || '#ffffff',
    fontFamily: layer.fontFamily === 'serif' ? 'Georgia, serif' : 'inherit',
    letterSpacing: (layer.letterSpacing || 0) * s + 'px'
  }
}

function layerTypeIcon(type) {
  const map = { background: '🎨', border: '🖼', decoration: '✨', text: 'T', image: '📷' }
  return map[type] || '?'
}

function layerName(layer) {
  if (layer.name) return layer.name
  if (layer.type === 'text') return layer.content?.slice(0, 8) || '文字'
  return layer.type === 'background' ? '背景' : layer.type === 'image' ? '图片' : '素材'
}

// 保存草稿
async function saveDraft() {
  saving.value = true
  try {
    const customData = { layers: layers.map(l => ({ ...l })) }
    await saveCustomization({
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

// 提交订单
function submitOrder() {
  saveDraft().then(() => {
    router.push({
      path: '/order-confirm',
      query: { templateId: template.value.id }
    })
  })
}

// 加载素材
async function loadMaterials() {
  loadingMaterials.value = true
  try {
    const res = await getMaterials({ size: 100 })
    allMaterials.value = res.data.records
  } catch (e) {} finally {
    loadingMaterials.value = false
  }
}

onMounted(async () => {
  try {
    const res = await getTemplateDetail(route.params.id)
    template.value = res.data
  } catch (e) {
    ElMessage.error('加载模板失败')
  }
  loadMaterials()
})
</script>

<style scoped>
.editor-page {
  height: calc(100vh - 64px);
  display: flex;
  flex-direction: column;
  background: #f5f0eb;
}

/* ---- Header ---- */
.editor-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 20px;
  background: #fff;
  border-bottom: 1px solid var(--c-border);
  flex-shrink: 0;
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

/* ---- Left: Material Panel ---- */
.material-panel {
  width: 260px;
  background: #fff;
  border-right: 1px solid var(--c-border);
  display: flex;
  flex-direction: column;
  flex-shrink: 0;
}

.material-tabs {
  display: flex;
  border-bottom: 1px solid var(--c-border);
  flex-shrink: 0;
}

.mat-tab {
  flex: 1;
  padding: 12px 0;
  font-size: 13px;
  font-weight: 500;
  color: var(--c-text-muted);
  background: none;
  border: none;
  border-bottom: 2px solid transparent;
  cursor: pointer;
  transition: all 0.2s;
  font-family: var(--f-body);
}

.mat-tab.active {
  color: var(--c-orange);
  border-bottom-color: var(--c-orange);
}

.mat-tab:hover {
  color: var(--c-orange);
}

.material-grid {
  flex: 1;
  overflow-y: auto;
  padding: 12px;
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 10px;
  align-content: start;
}

.mat-item {
  border: 1px solid var(--c-border);
  border-radius: var(--r-sm);
  overflow: hidden;
  cursor: pointer;
  transition: all 0.2s;
  display: flex;
  flex-direction: column;
}

.mat-item:hover {
  border-color: var(--c-orange);
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.mat-item img {
  width: 100%;
  aspect-ratio: 1;
  object-fit: cover;
  display: block;
}

.mat-item span {
  font-size: 11px;
  color: var(--c-text-secondary);
  padding: 6px 8px;
  text-align: center;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.mat-item.text-preset {
  padding: 14px 10px;
  align-items: center;
  justify-content: center;
  min-height: 60px;
  background: var(--c-cream);
}

.mat-empty {
  grid-column: span 2;
  text-align: center;
  color: var(--c-text-muted);
  font-size: 13px;
  padding: 40px 0;
}

.material-upload {
  padding: 12px;
  border-top: 1px solid var(--c-border);
  flex-shrink: 0;
}

.btn-upload {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 10px;
  font-size: 13px;
  font-weight: 500;
  color: var(--c-orange);
  background: rgba(232, 114, 42, 0.06);
  border: 1px dashed var(--c-orange);
  border-radius: var(--r-sm);
  cursor: pointer;
  transition: all 0.2s;
  font-family: var(--f-body);
}

.btn-upload:hover {
  background: rgba(232, 114, 42, 0.12);
}

/* ---- Center: Canvas ---- */
.canvas-area {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  background-image: radial-gradient(circle, #d5cfc8 1px, transparent 1px);
  background-size: 20px 20px;
  overflow: hidden;
}

.canvas-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
}

.canvas {
  position: relative;
  overflow: hidden;
  box-shadow: 0 8px 32px rgba(45, 24, 16, 0.15);
  background: #e8e0d8;
}

.canvas-rect {
  border-radius: var(--r-md);
}

.canvas-circle {
  border-radius: 50%;
}

.canvas-roundRect {
  border-radius: 24px;
}

.canvas-layer {
  position: absolute;
  cursor: move;
  user-select: none;
}

.canvas-layer img {
  width: 100%;
  height: 100%;
  object-fit: contain;
  pointer-events: none;
}

.canvas-layer.active {
  outline: 2px solid var(--c-orange);
  outline-offset: 1px;
}

.layer-background {
  pointer-events: none;
}

.canvas-layer.dragging {
  opacity: 0.85;
}

.layer-text-content {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  word-break: break-all;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.2);
  pointer-events: none;
}

/* Handles */
.layer-handles {
  position: absolute;
  inset: -4px;
  pointer-events: none;
}

.handle {
  position: absolute;
  width: 10px;
  height: 10px;
  background: var(--c-orange);
  border: 2px solid #fff;
  border-radius: 50%;
  pointer-events: all;
  cursor: nwse-resize;
  z-index: 10;
}

.handle-tl { top: 0; left: 0; cursor: nwse-resize; }
.handle-tr { top: 0; right: 0; cursor: nesw-resize; }
.handle-bl { bottom: 0; left: 0; cursor: nesw-resize; }
.handle-br { bottom: 0; right: 0; cursor: nwse-resize; }

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

/* ---- Right: Layer Panel ---- */
.layer-panel {
  width: 280px;
  background: #fff;
  border-left: 1px solid var(--c-border);
  display: flex;
  flex-direction: column;
  flex-shrink: 0;
  overflow-y: auto;
}

.layer-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 14px 16px;
  border-bottom: 1px solid var(--c-border);
  flex-shrink: 0;
}

.layer-title {
  font-size: 14px;
  font-weight: 600;
  color: var(--c-brown);
}

.btn-add-text {
  width: 28px;
  height: 28px;
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

.btn-add-text:hover {
  border-color: var(--c-orange);
  color: var(--c-orange);
}

.layer-list {
  flex: 1;
  overflow-y: auto;
  min-height: 120px;
}

.layer-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 16px;
  border-bottom: 1px solid var(--c-cream-dark, #f0ebe5);
  cursor: pointer;
  transition: all 0.15s;
  font-size: 13px;
}

.layer-item:hover {
  background: var(--c-cream);
}

.layer-item.active {
  background: rgba(232, 114, 42, 0.08);
  border-left: 3px solid var(--c-orange);
}

.layer-icon {
  font-size: 14px;
  width: 20px;
  text-align: center;
  flex-shrink: 0;
}

.layer-name {
  flex: 1;
  color: var(--c-text);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.layer-actions {
  display: flex;
  gap: 2px;
  flex-shrink: 0;
}

.layer-actions button {
  width: 22px;
  height: 22px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: none;
  border: 1px solid transparent;
  border-radius: 3px;
  cursor: pointer;
  font-size: 12px;
  color: var(--c-text-muted);
  transition: all 0.15s;
}

.layer-actions button:hover {
  border-color: var(--c-border);
  color: var(--c-text);
}

.layer-actions button:disabled {
  opacity: 0.3;
  cursor: not-allowed;
}

.layer-actions .btn-delete:hover {
  color: #e74c3c;
  border-color: #e74c3c33;
}

.layer-empty {
  text-align: center;
  color: var(--c-text-muted);
  font-size: 13px;
  padding: 40px 16px;
}

/* ---- Properties ---- */
.layer-props {
  padding: 16px;
  border-top: 1px solid var(--c-border);
  flex-shrink: 0;
}

.prop-title {
  font-size: 13px;
  font-weight: 600;
  color: var(--c-brown);
  margin-bottom: 12px;
}

.prop-row {
  margin-bottom: 12px;
}

.prop-row label {
  display: block;
  font-size: 12px;
  color: var(--c-text-muted);
  margin-bottom: 4px;
}

.prop-input {
  width: 100%;
  padding: 7px 10px;
  font-size: 13px;
  border: 1px solid var(--c-border);
  border-radius: var(--r-sm);
  outline: none;
  font-family: var(--f-body);
  color: var(--c-text);
  background: var(--c-cream);
  transition: border-color 0.2s;
}

.prop-input:focus {
  border-color: var(--c-orange);
  background: #fff;
}

.prop-input.small {
  width: 80px;
}

.prop-size {
  display: flex;
  align-items: center;
  gap: 6px;
}

.prop-size input {
  width: 70px;
  padding: 7px 8px;
  font-size: 13px;
  border: 1px solid var(--c-border);
  border-radius: var(--r-sm);
  outline: none;
  font-family: var(--f-body);
  color: var(--c-text);
  background: var(--c-cream);
  text-align: center;
}

.prop-size input:focus {
  border-color: var(--c-orange);
}

.prop-size span {
  color: var(--c-text-muted);
  font-size: 12px;
}

.prop-color {
  width: 40px;
  height: 32px;
  border: 1px solid var(--c-border);
  border-radius: var(--r-sm);
  cursor: pointer;
  padding: 2px;
}

.btn-replace {
  width: 100%;
  padding: 8px;
  font-size: 12px;
  color: var(--c-orange);
  background: rgba(232, 114, 42, 0.06);
  border: 1px dashed var(--c-orange);
  border-radius: var(--r-sm);
  cursor: pointer;
  transition: all 0.2s;
  font-family: var(--f-body);
  margin-top: 4px;
}

.btn-replace:hover {
  background: rgba(232, 114, 42, 0.12);
}

/* ---- Image Controls ---- */
.image-controls {
  margin-bottom: 12px;
}

.control-row {
  margin-bottom: 10px;
}

.control-row label {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 12px;
  color: var(--c-text-muted);
  margin-bottom: 4px;
}

.control-val {
  font-weight: 600;
  color: var(--c-orange);
  font-size: 11px;
}

.control-row :deep(.el-slider) {
  --el-slider-main-bg-color: var(--c-orange);
  --el-slider-runway-bg-color: var(--c-cream-dark, #f0ebe5);
  margin-top: 2px;
}

.control-row :deep(.el-slider__button) {
  width: 14px;
  height: 14px;
  border-color: var(--c-orange);
}

.action-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 6px;
  margin-bottom: 12px;
}

.action-btn {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 3px;
  padding: 8px 4px;
  font-size: 10px;
  color: var(--c-text-secondary);
  background: var(--c-cream);
  border: 1px solid var(--c-border);
  border-radius: var(--r-sm);
  cursor: pointer;
  transition: all 0.2s;
  font-family: var(--f-body);
  line-height: 1;
}

.action-btn:hover {
  border-color: var(--c-orange);
  color: var(--c-orange);
  background: rgba(232, 114, 42, 0.06);
}

.action-btn.active {
  border-color: var(--c-orange);
  color: var(--c-orange);
  background: rgba(232, 114, 42, 0.1);
}

.layer-tip {
  display: flex;
  align-items: flex-start;
  gap: 8px;
  font-size: 11px;
  color: var(--c-text-muted);
  padding: 12px 16px;
  background: var(--c-cream);
  line-height: 1.5;
  flex-shrink: 0;
  margin-top: auto;
}

.layer-tip svg {
  flex-shrink: 0;
  margin-top: 1px;
}
</style>
