<template>
  <div>
    <h2 style="margin-bottom: 20px; font-family: var(--f-heading)">订单管理</h2>

    <div style="margin-bottom: 16px">
      <el-radio-group v-model="statusFilter" @change="loadOrders">
        <el-radio-button label="">全部</el-radio-button>
        <el-radio-button label="pending">待确认</el-radio-button>
        <el-radio-button label="confirmed">已确认</el-radio-button>
        <el-radio-button label="producing">生产中</el-radio-button>
        <el-radio-button label="shipped">已发货</el-radio-button>
      </el-radio-group>
    </div>

    <el-table :data="orders" v-loading="loading" border stripe>
      <el-table-column prop="orderNo" label="订单号" width="180" />
      <el-table-column prop="receiverName" label="收货人" width="100" />
      <el-table-column prop="receiverPhone" label="电话" width="130" />
      <el-table-column prop="totalPrice" label="总价" width="80">
        <template #default="{ row }">¥{{ row.totalPrice }}</template>
      </el-table-column>
      <el-table-column prop="quantity" label="数量" width="60" />
      <el-table-column prop="status" label="状态" width="90">
        <template #default="{ row }">
          <el-tag :type="statusType[row.status]" size="small">{{ statusMap[row.status] }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="下单时间" width="170" />
      <el-table-column label="操作" min-width="200">
        <template #default="{ row }">
          <el-button link type="primary" size="small" @click="handleConfirm(row)" v-if="row.status === 'pending'">确认</el-button>
          <el-button link type="warning" size="small" @click="handleProduce(row)" v-if="row.status === 'confirmed'">开始生产</el-button>
          <el-button link type="success" size="small" @click="handleShip(row)" v-if="row.status === 'producing'">发货</el-button>
          <el-button link type="primary" size="small" @click="handleComplete(row)" v-if="row.status === 'shipped'">完成</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div style="display:flex;justify-content:center;margin-top:20px">
      <el-pagination background layout="prev, pager, next" :total="total" :page-size="pageSize" v-model:current-page="currentPage" @current-change="loadOrders" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { adminGetOrders, adminConfirmOrder, adminProduceOrder, adminShipOrder, adminCompleteOrder } from '../../api/modules/admin'

const loading = ref(false)
const orders = ref([])
const statusFilter = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const statusMap = {
  pending: '待确认', confirmed: '已确认', producing: '生产中',
  shipped: '已发货', completed: '已完成', cancelled: '已取消'
}
const statusType = {
  pending: 'warning', confirmed: 'info', producing: '',
  shipped: 'success', completed: 'success', cancelled: 'danger'
}

async function loadOrders() {
  loading.value = true
  try {
    const res = await adminGetOrders({
      status: statusFilter.value || undefined,
      current: currentPage.value,
      size: pageSize.value
    })
    orders.value = res.data.records
    total.value = res.data.total
  } catch (e) {} finally {
    loading.value = false
  }
}

async function handleConfirm(row) {
  await adminConfirmOrder(row.id)
  ElMessage.success('已确认')
  loadOrders()
}

async function handleProduce(row) {
  await adminProduceOrder(row.id)
  ElMessage.success('已开始生产')
  loadOrders()
}

async function handleShip(row) {
  await adminShipOrder(row.id, '商品已发货')
  ElMessage.success('已发货')
  loadOrders()
}

async function handleComplete(row) {
  await adminCompleteOrder(row.id)
  ElMessage.success('已完成')
  loadOrders()
}

onMounted(() => loadOrders())
</script>
