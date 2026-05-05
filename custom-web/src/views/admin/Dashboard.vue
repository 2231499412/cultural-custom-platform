<template>
  <div>
    <h2 style="margin-bottom: 24px; font-family: var(--f-heading)">数据概览</h2>
    <el-row :gutter="20">
      <el-col :span="6" v-for="item in statCards" :key="item.label">
        <el-card shadow="hover" class="stat-card-wrap">
          <div class="stat-card">
            <div class="stat-icon" :style="{ background: item.bg }">
              <span>{{ item.icon }}</span>
            </div>
            <div class="stat-value">{{ item.value }}</div>
            <div class="stat-label">{{ item.label }}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getDashboardStats } from '../../api/modules/admin'

const statCards = ref([
  { label: '用户总数', value: 0, icon: '👤', bg: 'rgba(64,158,255,0.1)' },
  { label: '模板数量', value: 0, icon: '🎨', bg: 'rgba(103,194,58,0.1)' },
  { label: '订单总数', value: 0, icon: '📦', bg: 'rgba(230,162,60,0.1)' },
  { label: '总收入', value: '¥0', icon: '💰', bg: 'rgba(245,108,108,0.1)' }
])

onMounted(async () => {
  try {
    const res = await getDashboardStats()
    const d = res.data
    statCards.value[0].value = d.userCount
    statCards.value[1].value = d.templateCount
    statCards.value[2].value = d.orderCount
    statCards.value[3].value = '¥' + (d.totalIncome || 0)
  } catch (e) {}
})
</script>

<style scoped>
.stat-card-wrap {
  border-radius: 12px;
}

.stat-card {
  text-align: center;
  padding: 12px 0;
}

.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  margin-bottom: 12px;
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  font-family: var(--f-heading);
  color: #303133;
}

.stat-label {
  font-size: 13px;
  color: #909399;
  margin-top: 4px;
}
</style>
