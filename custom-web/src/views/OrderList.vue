<template>
  <div class="page-container">
    <h1 class="page-title">我的订单</h1>

    <el-tabs v-model="activeTab" @tab-change="loadOrders">
      <el-tab-pane label="全部" name="all" />
      <el-tab-pane label="待确认" name="pending" />
      <el-tab-pane label="生产中" name="producing" />
      <el-tab-pane label="已发货" name="shipped" />
      <el-tab-pane label="已完成" name="completed" />
    </el-tabs>

    <div class="order-list" v-loading="loading">
      <div v-for="order in orders" :key="order.id" class="order-card">
        <div class="order-header">
          <span>订单号：{{ order.orderNo }}</span>
          <el-tag :type="statusType[order.status]">{{ statusMap[order.status] }}</el-tag>
        </div>
        <div class="order-body">
          <div class="order-info">
            <p>定制商品</p>
            <p>数量：{{ order.quantity }}</p>
          </div>
          <div class="order-price">¥{{ order.totalPrice }}</div>
        </div>
      </div>
    </div>

    <el-empty v-if="!loading && orders.length === 0" description="暂无订单" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const loading = ref(false)
const activeTab = ref('all')
const orders = ref([])

const statusMap = {
  pending: '待确认',
  confirmed: '已确认',
  producing: '生产中',
  shipped: '已发货',
  completed: '已完成',
  cancelled: '已取消'
}

const statusType = {
  pending: 'warning',
  confirmed: 'info',
  producing: '',
  shipped: 'success',
  completed: 'success',
  cancelled: 'danger'
}

async function loadOrders() {
  loading.value = true
  // TODO: 接入接口
  setTimeout(() => {
    orders.value = []
    loading.value = false
  }, 500)
}

onMounted(() => {
  loadOrders()
})
</script>

<style scoped>
.page-title {
  font-size: 28px;
  margin: 20px 0;
}

.order-list {
  margin-top: 16px;
}

.order-card {
  background: #fff;
  border-radius: 8px;
  margin-bottom: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 20px;
  border-bottom: 1px solid #f0f0f0;
  font-size: 13px;
  color: #909399;
}

.order-body {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
}

.order-info p {
  margin-bottom: 4px;
  color: #606266;
}

.order-price {
  font-size: 20px;
  font-weight: 600;
  color: #f56c6c;
}
</style>
