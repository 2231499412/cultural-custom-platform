<template>
  <view class="order-list">
    <!-- 状态筛选 -->
    <view class="filter-bar">
      <view class="filter-item" :class="{ active: currentStatus === '' }" @click="switchStatus('')">全部</view>
      <view class="filter-item" :class="{ active: currentStatus === 'pending' }" @click="switchStatus('pending')">待付款</view>
      <view class="filter-item" :class="{ active: currentStatus === 'paid' }" @click="switchStatus('paid')">待发货</view>
      <view class="filter-item" :class="{ active: currentStatus === 'shipped' }" @click="switchStatus('shipped')">已发货</view>
    </view>

    <!-- 订单列表 -->
    <view class="order-card card" v-for="order in orders" :key="order.id">
      <view class="order-header">
        <text class="order-no">{{ order.orderNo }}</text>
        <text class="order-status">{{ statusMap[order.status] }}</text>
      </view>
      <view class="order-body">
        <text class="order-price">¥{{ order.totalPrice }}</text>
        <text class="order-qty">x{{ order.quantity }}</text>
      </view>
      <view class="order-footer">
        <text class="order-time">{{ order.createTime }}</text>
        <button v-if="order.status === 'pending'" class="cancel-btn" @click="cancel(order.id)">取消</button>
      </view>
    </view>

    <view v-if="orders.length === 0 && !loading" class="empty">
      <text>暂无订单</text>
      <button class="btn-outline" @click="goHome" style="margin-top: 20rpx;">去逛逛</button>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { getOrderList, cancelOrder } from '../../api/modules/order'

const orders = ref([])
const loading = ref(false)
const currentStatus = ref('')
const statusMap = { pending: '待付款', paid: '待发货', shipped: '已发货', completed: '已完成', cancelled: '已取消' }

onShow(() => {
  loadOrders()
})

const loadOrders = async () => {
  loading.value = true
  try {
    const params = { current: 1, size: 20 }
    if (currentStatus.value) params.status = currentStatus.value
    const res = await getOrderList(params)
    orders.value = res.data.records
  } catch (e) {
    console.error('加载订单失败:', e)
  } finally {
    loading.value = false
  }
}

const switchStatus = (status) => {
  currentStatus.value = status
  loadOrders()
}

const cancel = async (id) => {
  uni.showModal({
    title: '确认取消',
    content: '确定要取消这个订单吗？',
    success: async (res) => {
      if (res.confirm) {
        try {
          await cancelOrder(id)
          uni.showToast({ title: '已取消', icon: 'success' })
          loadOrders()
        } catch (e) {}
      }
    }
  })
}

const goHome = () => {
  uni.switchTab({ url: '/pages/index/index' })
}
</script>

<style scoped>
.filter-bar {
  display: flex;
  padding: 20rpx 24rpx;
  gap: 16rpx;
  background: #fff;
}

.filter-item {
  flex: 1;
  text-align: center;
  padding: 12rpx 0;
  border-radius: 32rpx;
  font-size: 26rpx;
  color: #666;
  background: #f5f0eb;
}

.filter-item.active {
  background: #E8722A;
  color: #fff;
}

.order-card {
  margin: 20rpx 24rpx;
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 16rpx;
  border-bottom: 2rpx solid #f0ebe5;
}

.order-no {
  font-size: 24rpx;
  color: #999;
}

.order-status {
  font-size: 26rpx;
  color: #E8722A;
  font-weight: 500;
}

.order-body {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16rpx 0;
}

.order-price {
  font-size: 32rpx;
  font-weight: 700;
  color: #2D1810;
}

.order-qty {
  font-size: 26rpx;
  color: #999;
}

.order-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.order-time {
  font-size: 22rpx;
  color: #999;
}

.cancel-btn {
  font-size: 24rpx;
  color: #999;
  background: none;
  border: 2rpx solid #ddd;
  border-radius: 8rpx;
  padding: 8rpx 24rpx;
}

.empty {
  text-align: center;
  padding: 120rpx 0;
  color: #999;
  font-size: 28rpx;
}
</style>
