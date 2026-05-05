<template>
  <view class="profile">
    <!-- 用户信息 -->
    <view class="user-card" v-if="userInfo">
      <view class="avatar">{{ userInfo.nickname?.[0] || '?' }}</view>
      <view class="user-info">
        <text class="nickname">{{ userInfo.nickname || userInfo.username }}</text>
        <text class="role">{{ userInfo.role === 'admin' ? '管理员' : '普通用户' }}</text>
      </view>
    </view>
    <view class="user-card" v-else @click="goLogin">
      <view class="avatar">?</view>
      <view class="user-info">
        <text class="nickname">点击登录</text>
      </view>
    </view>

    <!-- 功能列表 -->
    <view class="menu-list">
      <view class="menu-item" @click="goPage('/pages/order/list')">
        <text>我的订单</text>
        <text class="arrow">></text>
      </view>
      <view class="menu-item" @click="goPage('/pages/templates/list')">
        <text>浏览模板</text>
        <text class="arrow">></text>
      </view>
    </view>

    <button v-if="userInfo" class="logout-btn" @click="logout">退出登录</button>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { getUserInfo } from '../../api/modules/user'

const userInfo = ref(null)

onShow(async () => {
  const token = uni.getStorageSync('token')
  if (token) {
    try {
      const res = await getUserInfo()
      userInfo.value = res.data
    } catch (e) {
      userInfo.value = null
    }
  }
})

const goLogin = () => {
  uni.navigateTo({ url: '/pages/login/index' })
}

const goPage = (url) => {
  if (url.includes('switchTab') || url === '/pages/order/list') {
    uni.switchTab({ url })
  } else {
    uni.navigateTo({ url })
  }
}

const logout = () => {
  uni.removeStorageSync('token')
  uni.removeStorageSync('userInfo')
  userInfo.value = null
  uni.showToast({ title: '已退出', icon: 'success' })
}
</script>

<style scoped>
.user-card {
  display: flex;
  align-items: center;
  gap: 24rpx;
  padding: 40rpx 30rpx;
  background: linear-gradient(135deg, #E8722A, #c95f1f);
  color: #fff;
}

.avatar {
  width: 100rpx;
  height: 100rpx;
  border-radius: 50%;
  background: rgba(255,255,255,0.3);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 40rpx;
  font-weight: 700;
}

.nickname {
  font-size: 32rpx;
  font-weight: 600;
  display: block;
}

.role {
  font-size: 24rpx;
  opacity: 0.8;
  margin-top: 4rpx;
  display: block;
}

.menu-list {
  margin: 24rpx;
  background: #fff;
  border-radius: 16rpx;
  overflow: hidden;
}

.menu-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 28rpx 30rpx;
  border-bottom: 2rpx solid #f0ebe5;
  font-size: 28rpx;
}

.menu-item:last-child {
  border-bottom: none;
}

.arrow {
  color: #999;
  font-size: 28rpx;
}

.logout-btn {
  margin: 40rpx 24rpx;
  background: #fff;
  color: #e74c3c;
  border: 2rpx solid #e74c3c;
  border-radius: 12rpx;
  font-size: 28rpx;
}
</style>
