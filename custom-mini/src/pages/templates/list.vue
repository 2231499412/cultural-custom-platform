<template>
  <view class="template-list">
    <!-- 分类筛选 -->
    <view class="filter-bar">
      <view class="filter-item" :class="{ active: currentCat === '' }" @click="switchCat('')">全部</view>
      <view class="filter-item" :class="{ active: currentCat === cat.key }" v-for="cat in categories" :key="cat.key" @click="switchCat(cat.key)">{{ cat.name }}</view>
    </view>

    <!-- 模板列表 -->
    <view class="grid">
      <view class="tpl-card" v-for="tpl in templates" :key="tpl.id" @click="goDetail(tpl.id)">
        <image class="tpl-img" :src="baseUrl + tpl.previewUrl" mode="aspectFill" />
        <view class="tpl-info">
          <text class="tpl-name">{{ tpl.name }}</text>
          <text class="tpl-desc">{{ tpl.description }}</text>
          <text class="tpl-price">¥{{ tpl.price }}</text>
        </view>
      </view>
    </view>

    <view v-if="templates.length === 0 && !loading" class="empty">暂无模板</view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { getTemplateList } from '../../api/modules/template'

const baseUrl = 'http://localhost:8080'
const templates = ref([])
const loading = ref(false)
const currentCat = ref('')

const categories = [
  { key: 'ticket', name: '票根' },
  { key: 'acrylic', name: '亚克力' },
  { key: 'keychain', name: '钥匙扣' },
  { key: 'badge', name: '徽章' }
]

onLoad((options) => {
  if (options.category) currentCat.value = options.category
  loadTemplates()
})

const loadTemplates = async () => {
  loading.value = true
  try {
    const params = { current: 1, size: 20 }
    if (currentCat.value) params.category = currentCat.value
    const res = await getTemplateList(params)
    templates.value = res.data.records
  } catch (e) {} finally {
    loading.value = false
  }
}

const switchCat = (cat) => {
  currentCat.value = cat
  loadTemplates()
}

const goDetail = (id) => {
  uni.navigateTo({ url: `/pages/templates/detail?id=${id}` })
}
</script>

<style scoped>
.filter-bar {
  display: flex;
  padding: 20rpx 24rpx;
  gap: 16rpx;
  background: #fff;
  overflow-x: auto;
  white-space: nowrap;
}

.filter-item {
  padding: 12rpx 28rpx;
  border-radius: 32rpx;
  font-size: 26rpx;
  color: #666;
  background: #f5f0eb;
  flex-shrink: 0;
}

.filter-item.active {
  background: #E8722A;
  color: #fff;
}

.grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20rpx;
  padding: 24rpx;
}

.tpl-card {
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
  font-weight: 600;
  color: #2D1810;
  display: block;
}

.tpl-desc {
  font-size: 22rpx;
  color: #999;
  margin-top: 4rpx;
  display: block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.tpl-price {
  font-size: 28rpx;
  color: #E8722A;
  font-weight: 600;
  margin-top: 8rpx;
  display: block;
}

.empty {
  text-align: center;
  color: #999;
  padding: 100rpx 0;
  font-size: 28rpx;
}
</style>
