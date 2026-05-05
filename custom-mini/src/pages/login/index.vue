<template>
  <view class="login">
    <view class="logo-area">
      <text class="logo-text">造物记</text>
      <text class="logo-sub">文创周边定制平台</text>
    </view>

    <view class="form card">
      <view class="form-item">
        <text class="label">用户名</text>
        <input class="input" v-model="username" placeholder="请输入用户名" />
      </view>
      <view class="form-item">
        <text class="label">密码</text>
        <input class="input" v-model="password" placeholder="请输入密码" password />
      </view>
      <button class="btn-primary" style="width:100%;margin-top:24rpx;" @click="doLogin">登录</button>
      <view class="register-link" @click="goRegister">还没有账号？去注册</view>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { login } from '../../api/modules/user'

const username = ref('')
const password = ref('')

const doLogin = async () => {
  if (!username.value || !password.value) {
    uni.showToast({ title: '请输入用户名和密码', icon: 'none' })
    return
  }
  try {
    const res = await login({ username: username.value, password: password.value })
    uni.setStorageSync('token', res.data.token)
    uni.setStorageSync('userInfo', JSON.stringify(res.data.user))
    uni.showToast({ title: '登录成功', icon: 'success' })
    setTimeout(() => {
      uni.navigateBack()
    }, 1500)
  } catch (e) {}
}

const goRegister = () => {
  // 可以后续添加注册页面
  uni.showToast({ title: '暂不支持注册', icon: 'none' })
}
</script>

<style scoped>
.login {
  padding: 60rpx 30rpx;
}

.logo-area {
  text-align: center;
  margin-bottom: 60rpx;
}

.logo-text {
  font-size: 56rpx;
  font-weight: 700;
  color: #E8722A;
  display: block;
}

.logo-sub {
  font-size: 26rpx;
  color: #999;
  margin-top: 8rpx;
  display: block;
}

.form-item {
  margin-bottom: 24rpx;
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

.register-link {
  text-align: center;
  margin-top: 24rpx;
  font-size: 26rpx;
  color: #E8722A;
}
</style>
