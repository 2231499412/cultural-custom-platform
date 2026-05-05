<template>
  <div>
    <h2 style="margin-bottom: 20px">订单管理</h2>

    <el-table :data="orders" v-loading="loading" border>
      <el-table-column prop="orderNo" label="订单号" width="200" />
      <el-table-column prop="receiverName" label="收货人" width="120" />
      <el-table-column prop="totalPrice" label="总价" width="100" />
      <el-table-column prop="quantity" label="数量" width="80" />
      <el-table-column prop="status" label="状态" width="120">
        <template #default="{ row }">
          <el-tag :type="statusType[row.status]">{{ statusMap[row.status] }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="下单时间" width="180" />
      <el-table-column label="操作" width="200">
        <template #default="{ row }">
          <el-button link type="primary" size="small">详情</el-button>
          <el-button link type="warning" size="small" v-if="row.status === 'pending'">确认</el-button>
          <el-button link type="success" size="small" v-if="row.status === 'confirmed'">发货</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const loading = ref(false)
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

onMounted(() => {
  // TODO: 加载订单列表
})
</script>
