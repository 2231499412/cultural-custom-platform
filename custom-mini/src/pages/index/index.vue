<template>
  <view class="home">
    <!-- Banner -->
    <view class="banner">
      <view class="banner-text">
        <text class="banner-title">造物记</text>
        <text class="banner-sub">定制你的专属文创周边</text>
      </view>
    </view>

    <!-- 分类入口 -->
    <view class="section">
      <view class="section-title">选择品类</view>
      <view class="category-grid">
        <view class="category-item" v-for="cat in categories" :key="cat.key"
          @click="goTemplates(cat.key)">
          <view class="cat-icon">{{ cat.icon }}</view>
          <text class="cat-name">{{ cat.name }}</text>
        </view>
      </view>
    </view>

    <!-- 热门模板 -->
    <view class="section">
      <view class="section-title">热门推荐</view>
      <view class="template-grid">
        <view class="template-card" v-for="tpl in hotTemplates" :key="tpl.id"
          @click="goDetail(tpl.id)">
          <image class="tpl-img" :src="baseUrl + tpl.previewUrl" mode="aspectFill" />
          <view class="tpl-info">
            <text class="tpl-name">{{ tpl.name }}</text>
            <text class="tpl-price">¥{{ tpl.price }}</text>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { getTemplateList } from '../../api/modules/template'

const baseUrl = 'http://localhost:8080'
const hotTemplates = ref([])

const categories = [
  { key: 'ticket', name: '票根', icon: '🎫' },
  { key: 'acrylic', name: '亚克力', icon: '🏆' },
  { key: 'keychain', name: '钥匙扣', icon: '🔑' },
  { key: 'badge', name: '徽章', icon: '🏅' }
]

onLoad(async () => {
  try {
    const res = await getTemplateList({ current: 1, size: 6 })
    hotTemplates.value = res.data.records
  } catch (e) {}
})

const goTemplates = (category) => {
  uni.navigateTo({ url: `/pages/templates/list?category=${category}` })
}

const goDetail = (id) => {
  uni.navigateTo({ url: `/pages/templates/detail?id=${id}` })
}
</script>

<style scoped>
.home {
  padding-bottom: 30rpx;
}

.banner {
  background: linear-gradient(135deg, #E8722A, #c95f1f);
  padding: 60rpx 40rpx;
  color: #fff;
}

.banner-title {
  font-size: 48rpx;
  font-weight: 700;
  display: block;
}

.banner-sub {
  font-size: 26rpx;
  opacity: 0.85;
  margin-top: 8rpx;
  display: block;
}

.section {
  padding: 30rpx 24rpx 0;
}

.section-title {
  font-size: 32rpx;
  font-weight: 600;
  color: #2D1810;
  margin-bottom: 20rpx;
}

.category-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20rpx;
}

.category-item {
  background: #fff;
  border-radius: 16rpx;
  padding: 28rpx 0;
  text-align: center;
  box-shadow: 0 4rpx 16rpx rgba(45, 24, 16, 0.06);
}

.cat-icon {
  font-size: 48rpx;
  margin-bottom: 8rpx;
}

.cat-name {
  font-size: 24rpx;
  color: #666;
}

.template-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20rpx;
}

.template-card {
  background: #fff;
  border-radius: 16rpx;
  overflow: hidden;
  box-shadow: 0 4rpx 16rpx rgba(45, 24, 16, 0.06);
}

.tpl-img {
  width: 100%;
  height: 320rpx;
}

.tpl-info {
  padding: 16rpx 20rpx;
}

.tpl-name {
  font-size: 26rpx;
  font-weight: 500;
  color: #2D1810;
  display: block;
}

.tpl-price {
  font-size: 28rpx;
  color: #E8722A;
  font-weight: 600;
  margin-top: 4rpx;
  display: block;
}
</style>
