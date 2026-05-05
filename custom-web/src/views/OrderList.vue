<template>
  <div class="page-container">
    <h1 class="page-title">我的订单</h1>

    <div class="filter-bar">
      <button
        v-for="tab in tabs"
        :key="tab.value"
        class="filter-btn"
        :class="{ active: activeTab === tab.value }"
        @click="selectTab(tab.value)"
      >
        {{ tab.label }}
      </button>
    </div>

    <div class="order-list" v-loading="loading">
      <div v-for="order in orders" :key="order.id" class="order-card">
        <div class="order-header">
          <span class="order-no">订单号：{{ order.orderNo }}</span>
          <el-tag :type="statusType[order.status]" size="small">{{ statusMap[order.status] }}</el-tag>
        </div>
        <div class="order-body">
          <div class="order-info">
            <p class="order-receiver">{{ order.receiverName }} {{ order.receiverPhone }}</p>
            <p class="order-addr">{{ order.receiverAddress }}</p>
            <p class="order-time">{{ order.createTime }}</p>
          </div>
          <div class="order-right">
            <div class="order-price">¥{{ order.totalPrice }}</div>
            <div class="order-qty">x{{ order.quantity }}</div>
            <button
              v-if="order.status === 'pending'"
              class="btn-cancel"
              @click="handleCancel(order)"
            >取消订单</button>
          </div>
        </div>
      </div>
    </div>

    <el-empty v-if="!loading && orders.length === 0" description="暂无订单" />

    <div style="display:flex;justify-content:center;margin-top:24px" v-if="total > pageSize">
      <el-pagination background layout="prev, pager, next" :total="total" :page-size="pageSize" v-model:current-page="currentPage" @current-change="loadOrders" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getOrderList, cancelOrder } from '../api/modules/order'

const loading = ref(false)
const activeTab = ref('')
const orders = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const tabs = [
  { label: '全部', value: '' },
  { label: '待确认', value: 'pending' },
  { label: '已确认', value: 'confirmed' },
  { label: '生产中', value: 'producing' },
  { label: '已发货', value: 'shipped' },
  { label: '已完成', value: 'completed' }
]

const statusMap = {
  pending: '待确认', confirmed: '已确认', producing: '生产中',
  shipped: '已发货', completed: '已完成', cancelled: '已取消'
}
const statusType = {
  pending: 'warning', confirmed: 'info', producing: '',
  shipped: 'success', completed: 'success', cancelled: 'danger'
}

function selectTab(val) {
  activeTab.value = val
  currentPage.value = 1
  loadOrders()
}

async function loadOrders() {
  loading.value = true
  try {
    const res = await getOrderList({
      status: activeTab.value || undefined,
      current: currentPage.value,
      size: pageSize.value
    })
    orders.value = res.data.records
    total.value = res.data.total
  } catch (e) {} finally {
    loading.value = false
  }
}

async function handleCancel(order) {
  await ElMessageBox.confirm('确定取消该订单？', '提示', { type: 'warning' })
  try {
    await cancelOrder(order.id)
    ElMessage.success('已取消')
    loadOrders()
  } catch (e) {}
}

onMounted(() => loadOrders())
</script>

<style scoped>
.page-title {
  font-family: var(--f-display);
  font-size: 28px;
  color: var(--c-brown);
  margin: 20px 0;
}

.filter-bar {
  display: flex;
  gap: 8px;
  margin-bottom: 24px;
  flex-wrap: wrap;
}

.filter-btn {
  padding: 8px 20px;
  font-size: 14px;
  font-weight: 500;
  color: var(--c-text-secondary);
  background: var(--c-card);
  border: 1px solid var(--c-border);
  border-radius: 40px;
  cursor: pointer;
  transition: all 0.25s;
  font-family: var(--f-body);
}

.filter-btn:hover {
  color: var(--c-orange);
  border-color: var(--c-orange-light);
}

.filter-btn.active {
  color: #fff;
  background: var(--c-orange);
  border-color: var(--c-orange);
}

.order-card {
  background: var(--c-card);
  border-radius: var(--r-md);
  margin-bottom: 12px;
  overflow: hidden;
  border: 1px solid rgba(45,24,16,0.04);
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 20px;
  background: var(--c-cream);
  font-size: 13px;
}

.order-no {
  color: var(--c-text-muted);
  font-family: monospace;
}

.order-body {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
}

.order-receiver {
  font-weight: 600;
  color: var(--c-brown);
  margin-bottom: 4px;
}

.order-addr {
  font-size: 13px;
  color: var(--c-text-secondary);
  margin-bottom: 4px;
}

.order-time {
  font-size: 12px;
  color: var(--c-text-muted);
}

.order-right {
  text-align: right;
}

.order-price {
  font-family: var(--f-heading);
  font-size: 20px;
  font-weight: 700;
  color: var(--c-orange);
}

.order-qty {
  font-size: 13px;
  color: var(--c-text-muted);
  margin: 4px 0 8px;
}

.btn-cancel {
  padding: 6px 16px;
  font-size: 12px;
  color: var(--c-text-muted);
  background: none;
  border: 1px solid var(--c-border);
  border-radius: var(--r-sm);
  cursor: pointer;
  transition: all 0.2s;
  font-family: var(--f-body);
}

.btn-cancel:hover {
  color: #f56c6c;
  border-color: #f56c6c;
}
</style>
