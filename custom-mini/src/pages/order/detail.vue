<template>
  <view class="detail" v-if="order.id">
    <!-- 状态栏 -->
    <view class="status-bar" :class="'status-' + order.status">
      <text class="status-text">{{ statusMap[order.status] }}</text>
      <text class="order-no">订单号：{{ order.orderNo }}</text>
    </view>

    <!-- 商品信息 -->
    <view class="card">
      <view class="card-title">商品信息</view>
      <view class="product-row">
        <image v-if="template.previewUrl" class="product-img" :src="baseUrl + template.previewUrl" mode="aspectFill" />
        <view velse class="product-img placeholder">📦</view>
        <view class="product-info">
          <text class="product-name">{{ template.name || '文创定制产品' }}</text>
          <text class="product-cat">{{ categoryMap[template.category] || '' }}</text>
          <view class="product-price-row">
            <text class="product-price">¥{{ order.totalPrice }}</text>
            <text class="product-qty">x{{ order.quantity }}</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 收货信息 -->
    <view class="card">
      <view class="card-title">收货信息</view>
      <view class="info-row">
        <text class="info-label">收货人</text>
        <text class="info-value">{{ order.receiverName }}</text>
      </view>
      <view class="info-row">
        <text class="info-label">手机号</text>
        <text class="info-value">{{ order.receiverPhone }}</text>
      </view>
      <view class="info-row">
        <text class="info-label">地址</text>
        <text class="info-value">{{ order.receiverAddress }}</text>
      </view>
      <view class="info-row" v-if="order.remark">
        <text class="info-label">备注</text>
        <text class="info-value">{{ order.remark }}</text>
      </view>
    </view>

    <!-- 订单信息 -->
    <view class="card">
      <view class="card-title">订单信息</view>
      <view class="info-row">
        <text class="info-label">下单时间</text>
        <text class="info-value">{{ order.createTime }}</text>
      </view>
      <view class="info-row">
        <text class="info-label">订单状态</text>
        <text class="info-value status-text">{{ statusMap[order.status] }}</text>
      </view>
    </view>

    <!-- 操作栏 -->
    <view class="action-bar" v-if="order.status === 'pending'">
      <button class="btn-cancel" @click="cancel">取消订单</button>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { getOrderDetail, cancelOrder } from '../../api/modules/order'
import { getCustomization } from '../../api/modules/customization'
import { getTemplateDetail } from '../../api/modules/template'

const baseUrl = 'http://localhost:8080'
const order = ref({})
const template = ref({})
const categoryMap = { ticket: '票根', acrylic: '亚克力立牌', keychain: '钥匙扣', badge: '徽章' }
const statusMap = { pending: '待付款', paid: '待发货', shipped: '已发货', completed: '已完成', cancelled: '已取消' }

onLoad(async (options) => {
  try {
    const res = await getOrderDetail(options.id)
    order.value = res.data
    // 通过定制记录获取模板信息
    if (res.data.customizationId) {
      try {
        const custRes = await getCustomization(res.data.customizationId)
        if (custRes.data.templateId) {
          const tplRes = await getTemplateDetail(custRes.data.templateId)
          template.value = tplRes.data
        }
      } catch (e) {}
    }
  } catch (e) {
    uni.showToast({ title: '加载失败', icon: 'none' })
  }
})

const cancel = () => {
  uni.showModal({
    title: '确认取消',
    content: '确定要取消这个订单吗？',
    success: async (res) => {
      if (res.confirm) {
        try {
          await cancelOrder(order.value.id)
          uni.showToast({ title: '已取消', icon: 'success' })
          order.value.status = 'cancelled'
        } catch (e) {}
      }
    }
  })
}
</script>

<style scoped>
.detail {
  padding-bottom: 120rpx;
}

.status-bar {
  padding: 40rpx 30rpx;
  color: #fff;
}

.status-pending { background: linear-gradient(135deg, #E8722A, #c95f1f); }
.status-paid { background: linear-gradient(135deg, #3498db, #2980b9); }
.status-shipped { background: linear-gradient(135deg, #27ae60, #219a52); }
.status-completed { background: linear-gradient(135deg, #95a5a6, #7f8c8d); }
.status-cancelled { background: linear-gradient(135deg, #95a5a6, #7f8c8d); }

.status-text {
  font-size: 36rpx;
  font-weight: 700;
  display: block;
}

.order-no {
  font-size: 24rpx;
  opacity: 0.8;
  margin-top: 8rpx;
  display: block;
}

.card {
  margin: 20rpx 24rpx;
  background: #fff;
  border-radius: 16rpx;
  padding: 24rpx;
  box-shadow: 0 4rpx 16rpx rgba(45, 24, 16, 0.06);
}

.card-title {
  font-size: 28rpx;
  font-weight: 600;
  color: #2D1810;
  margin-bottom: 16rpx;
  padding-bottom: 12rpx;
  border-bottom: 2rpx solid #f0ebe5;
}

.product-row {
  display: flex;
  gap: 20rpx;
}

.product-img {
  width: 160rpx;
  height: 160rpx;
  border-radius: 12rpx;
  flex-shrink: 0;
}

.product-img.placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f0eb;
  font-size: 60rpx;
}

.product-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.product-name {
  font-size: 28rpx;
  font-weight: 600;
  color: #2D1810;
  display: block;
}

.product-cat {
  font-size: 22rpx;
  color: #E8722A;
  margin-top: 6rpx;
  display: block;
}

.product-price-row {
  display: flex;
  align-items: baseline;
  gap: 8rpx;
  margin-top: 12rpx;
}

.product-price {
  font-size: 32rpx;
  font-weight: 700;
  color: #E8722A;
}

.product-qty {
  font-size: 24rpx;
  color: #999;
}

.info-row {
  display: flex;
  justify-content: space-between;
  padding: 10rpx 0;
  font-size: 26rpx;
}

.info-label {
  color: #999;
  flex-shrink: 0;
  width: 140rpx;
}

.info-value {
  color: #333;
  flex: 1;
  text-align: right;
}

.info-value.status-text {
  color: #E8722A;
  font-weight: 500;
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

.btn-cancel {
  width: 100%;
  background: #fff;
  color: #e74c3c;
  border: 2rpx solid #e74c3c;
  border-radius: 12rpx;
  padding: 18rpx;
  font-size: 28rpx;
}
</style>
