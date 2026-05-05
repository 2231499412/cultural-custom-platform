<template>
  <view class="detail" v-if="template.id">
    <image class="preview" :src="baseUrl + template.previewUrl" mode="aspectFit" />

    <view class="info-card">
      <text class="name">{{ template.name }}</text>
      <text class="category">{{ categoryMap[template.category] }}</text>
      <text class="desc">{{ template.description }}</text>
      <view class="price-row">
        <text class="price">¥{{ template.price }}</text>
        <text class="unit">/件</text>
      </view>
    </view>

    <view class="action-bar">
      <button class="btn-primary" @click="goEditor">开始定制</button>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { getTemplateDetail } from '../../api/modules/template'

const baseUrl = 'http://localhost:8080'
const template = ref({})
const categoryMap = { ticket: '票根', acrylic: '亚克力立牌', keychain: '钥匙扣', badge: '徽章' }

onLoad(async (options) => {
  try {
    const res = await getTemplateDetail(options.id)
    template.value = res.data
  } catch (e) {
    uni.showToast({ title: '加载失败', icon: 'none' })
  }
})

const goEditor = () => {
  uni.navigateTo({ url: `/pages/editor/index?id=${template.value.id}` })
}
</script>

<style scoped>
.detail {
  padding-bottom: 120rpx;
}

.preview {
  width: 100%;
  height: 600rpx;
  background: #f5f0eb;
}

.info-card {
  margin: 24rpx;
  background: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  box-shadow: 0 4rpx 16rpx rgba(45, 24, 16, 0.06);
}

.name {
  font-size: 36rpx;
  font-weight: 700;
  color: #2D1810;
  display: block;
}

.category {
  font-size: 24rpx;
  color: #E8722A;
  background: rgba(232, 114, 42, 0.1);
  padding: 4rpx 16rpx;
  border-radius: 8rpx;
  display: inline-block;
  margin-top: 12rpx;
}

.desc {
  font-size: 26rpx;
  color: #666;
  margin-top: 16rpx;
  display: block;
  line-height: 1.6;
}

.price-row {
  margin-top: 20rpx;
  display: flex;
  align-items: baseline;
}

.price {
  font-size: 40rpx;
  font-weight: 700;
  color: #E8722A;
}

.unit {
  font-size: 24rpx;
  color: #999;
  margin-left: 4rpx;
}

.action-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 20rpx 30rpx;
  padding-bottom: calc(20rpx + env(safe-area-inset-bottom));
  background: #fff;
  box-shadow: 0 -4rpx 16rpx rgba(0,0,0,0.06);
}

.action-bar .btn-primary {
  width: 100%;
}
</style>
