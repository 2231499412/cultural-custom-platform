<script setup>
import { onLaunch } from '@dcloudio/uni-app'

onLaunch(() => {
  console.log('App Launch')
})
</script>

<style>
page {
  background-color: #FFF8F0;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  color: #333;
  font-size: 28rpx;
}

.container {
  padding: 24rpx;
}

.btn-primary {
  background: #E8722A;
  color: #fff;
  border: none;
  border-radius: 12rpx;
  padding: 20rpx 40rpx;
  font-size: 28rpx;
  font-weight: 600;
  text-align: center;
}

.btn-outline {
  background: transparent;
  color: #E8722A;
  border: 2rpx solid #E8722A;
  border-radius: 12rpx;
  padding: 18rpx 38rpx;
  font-size: 28rpx;
  text-align: center;
}

.card {
  background: #fff;
  border-radius: 16rpx;
  padding: 24rpx;
  box-shadow: 0 4rpx 16rpx rgba(45, 24, 16, 0.06);
}
</style>
