<template>
  <view class="editor">
    <!-- 画布区域 -->
    <view class="canvas-area">
      <canvas canvas-id="editorCanvas" id="editorCanvas" class="canvas"
        :style="{ width: canvasW + 'px', height: canvasH + 'px' }"
        @touchstart="onTouchStart" @touchmove="onTouchMove" @touchend="onTouchEnd" />
    </view>

    <!-- 图片控制 -->
    <view class="controls" v-if="userImage">
      <view class="control-row">
        <text class="label">缩放 {{ scale }}%</text>
        <slider :value="scale" :min="10" :max="300" @change="onScaleChange" activeColor="#E8722A" />
      </view>
      <view class="control-row">
        <text class="label">旋转 {{ rotation }}°</text>
        <slider :value="rotation" :min="0" :max="360" @change="onRotationChange" activeColor="#E8722A" />
      </view>
      <view class="btn-row">
        <button class="ctrl-btn" @click="flipX = !flipX">水平翻转</button>
        <button class="ctrl-btn" @click="flipY = !flipY">垂直翻转</button>
        <button class="ctrl-btn" @click="rotation = (rotation + 90) % 360; draw()">旋转90°</button>
      </view>
    </view>

    <!-- 操作栏 -->
    <view class="action-bar">
      <button class="btn-outline" @click="chooseImage">上传图片</button>
      <button class="btn-outline" @click="save">保存草稿</button>
      <button class="btn-primary" @click="submitOrder">提交订单</button>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { getTemplateDetail } from '../../api/modules/template'
import { saveCustomization } from '../../api/modules/customization'

const baseUrl = 'http://localhost:8080'
const template = ref({})
const canvasW = ref(280)
const canvasH = ref(400)
const userImage = ref(null)
const scale = ref(100)
const rotation = ref(0)
const flipX = ref(false)
const flipY = ref(false)
let ctx = null

onLoad(async (options) => {
  try {
    const res = await getTemplateDetail(options.id)
    template.value = res.data
    const struct = JSON.parse(res.data.structureJson || '{}')
    canvasW.value = struct.width || 280
    canvasH.value = struct.height || 400
    ctx = uni.createCanvasContext('editorCanvas')
    draw()
  } catch (e) {}
})

const draw = () => {
  if (!ctx) return
  ctx.clearRect(0, 0, canvasW.value, canvasH.value)

  // 绘制用户图片
  if (userImage.value) {
    const s = scale.value / 100
    const w = (userImage.value.w || 100) * s
    const h = (userImage.value.h || 100) * s
    const x = (canvasW.value - w) / 2
    const y = (canvasH.value - h) / 2

    ctx.save()
    ctx.translate(canvasW.value / 2, canvasH.value / 2)
    ctx.rotate(rotation.value * Math.PI / 180)
    ctx.scale(flipX.value ? -1 : 1, flipY.value ? -1 : 1)
    ctx.drawImage(userImage.value.path, -w / 2, -h / 2, w, h)
    ctx.restore()
  }

  // 绘制边框（模板预览）
  if (template.value.previewUrl) {
    ctx.drawImage(baseUrl + template.value.previewUrl, 0, 0, canvasW.value, canvasH.value)
  }

  ctx.draw()
}

const chooseImage = () => {
  uni.chooseImage({
    count: 1,
    sizeType: ['compressed'],
    success: (res) => {
      uni.getImageInfo({
        src: res.tempFilePaths[0],
        success: (info) => {
          const maxW = canvasW.value * 0.6
          const maxH = canvasH.value * 0.6
          const ratio = Math.min(maxW / info.width, maxH / info.height, 1)
          userImage.value = {
            path: res.tempFilePaths[0],
            w: Math.round(info.width * ratio),
            h: Math.round(info.height * ratio)
          }
          scale.value = 100
          rotation.value = 0
          flipX.value = false
          flipY.value = false
          draw()
        }
      })
    }
  })
}

const onScaleChange = (e) => {
  scale.value = e.detail.value
  draw()
}

const onRotationChange = (e) => {
  rotation.value = e.detail.value
  draw()
}

const save = async () => {
  try {
    const res = await saveCustomization({
      templateId: template.value.id,
      customDataJson: JSON.stringify({
        userImage: userImage.value,
        scale: scale.value,
        rotation: rotation.value,
        flipX: flipX.value,
        flipY: flipY.value
      })
    })
    uni.showToast({ title: '保存成功', icon: 'success' })
    return res.data
  } catch (e) {
    uni.showToast({ title: '请先登录', icon: 'none' })
    return null
  }
}

const submitOrder = async () => {
  const customizationId = await save()
  if (customizationId) {
    uni.navigateTo({
      url: `/pages/order/confirm?customizationId=${customizationId}&templateId=${template.value.id}&price=${template.value.price}`
    })
  }
}
</script>

<style scoped>
.editor {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.canvas-area {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 30rpx;
  background: #f5f0eb;
}

.canvas {
  background: #e8e0d8;
  border-radius: 12rpx;
}

.controls {
  padding: 20rpx 24rpx;
  background: #fff;
}

.control-row {
  margin-bottom: 16rpx;
}

.label {
  font-size: 24rpx;
  color: #666;
  margin-bottom: 8rpx;
  display: block;
}

.btn-row {
  display: flex;
  gap: 16rpx;
  margin-top: 16rpx;
}

.ctrl-btn {
  flex: 1;
  font-size: 24rpx;
  padding: 14rpx 0;
  background: #f5f0eb;
  color: #333;
  border-radius: 8rpx;
  border: none;
}

.action-bar {
  display: flex;
  gap: 20rpx;
  padding: 20rpx 30rpx;
  padding-bottom: calc(20rpx + env(safe-area-inset-bottom));
  background: #fff;
  box-shadow: 0 -4rpx 16rpx rgba(0,0,0,0.06);
}

.action-bar .btn-outline,
.action-bar .btn-primary {
  flex: 1;
}
</style>
