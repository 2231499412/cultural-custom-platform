<template>
  <view class="confirm">
    <view class="form card">
      <view class="form-title">收货信息</view>
      <view class="form-item">
        <text class="label">收货人</text>
        <input class="input" v-model="form.receiverName" placeholder="请输入收货人姓名" />
      </view>
      <view class="form-item">
        <text class="label">手机号</text>
        <input class="input" v-model="form.receiverPhone" placeholder="请输入手机号" type="number" maxlength="11" />
      </view>
      <view class="form-item">
        <text class="label">收货地址</text>
        <input class="input" v-model="form.receiverAddress" placeholder="请输入详细地址" />
      </view>
      <view class="form-item">
        <text class="label">备注</text>
        <input class="input" v-model="form.remark" placeholder="选填" />
      </view>
    </view>

    <view class="price-card card">
      <view class="price-row">
        <text>商品单价</text>
        <text>¥{{ price }}</text>
      </view>
      <view class="price-row">
        <text>数量</text>
        <view class="qty">
          <button class="qty-btn" @click="form.quantity = Math.max(1, form.quantity - 1)">-</button>
          <text>{{ form.quantity }}</text>
          <button class="qty-btn" @click="form.quantity++">+</button>
        </view>
      </view>
      <view class="price-row total">
        <text>合计</text>
        <text class="total-price">¥{{ (price * form.quantity).toFixed(2) }}</text>
      </view>
    </view>

    <view class="action-bar">
      <button class="btn-primary" @click="submit" :disabled="submitting">提交订单</button>
    </view>
  </view>
</template>

<script setup>
import { ref, computed } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { createOrder } from '../../api/modules/order'

const customizationId = ref(null)
const templateId = ref(null)
const price = ref(0)
const submitting = ref(false)
const form = ref({
  receiverName: '',
  receiverPhone: '',
  receiverAddress: '',
  remark: '',
  quantity: 1
})

onLoad((options) => {
  customizationId.value = options.customizationId
  templateId.value = options.templateId
  price.value = parseFloat(options.price) || 29.9
})

const submit = async () => {
  if (!form.value.receiverName || !form.value.receiverPhone || !form.value.receiverAddress) {
    uni.showToast({ title: '请填写完整收货信息', icon: 'none' })
    return
  }
  submitting.value = true
  try {
    const res = await createOrder({
      templateId: parseInt(templateId.value),
      customizationId: customizationId.value ? parseInt(customizationId.value) : null,
      totalPrice: price.value * form.value.quantity,
      ...form.value
    })
    uni.showToast({ title: '下单成功', icon: 'success' })
    setTimeout(() => {
      uni.switchTab({ url: '/pages/order/list' })
    }, 1500)
  } catch (e) {
    uni.showToast({ title: '请先登录', icon: 'none' })
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.confirm {
  padding: 24rpx;
  padding-bottom: 140rpx;
}

.form-title {
  font-size: 30rpx;
  font-weight: 600;
  color: #2D1810;
  margin-bottom: 20rpx;
}

.form-item {
  margin-bottom: 20rpx;
}

.label {
  font-size: 26rpx;
  color: #666;
  margin-bottom: 8rpx;
  display: block;
}

.input {
  border: 2rpx solid #e8e0d8;
  border-radius: 12rpx;
  padding: 18rpx 20rpx;
  font-size: 28rpx;
}

.price-card {
  margin-top: 24rpx;
}

.price-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12rpx 0;
  font-size: 28rpx;
  color: #333;
}

.total {
  border-top: 2rpx solid #f0ebe5;
  margin-top: 12rpx;
  padding-top: 16rpx;
}

.total-price {
  font-size: 36rpx;
  font-weight: 700;
  color: #E8722A;
}

.qty {
  display: flex;
  align-items: center;
  gap: 20rpx;
}

.qty-btn {
  width: 48rpx;
  height: 48rpx;
  line-height: 44rpx;
  text-align: center;
  background: #f5f0eb;
  border: none;
  border-radius: 8rpx;
  font-size: 28rpx;
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
