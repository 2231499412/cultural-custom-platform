<template>
  <div class="page-container">
    <h1 class="page-title">确认订单</h1>

    <div class="confirm-layout">
      <!-- 左侧：定制预览 -->
      <div class="confirm-preview">
        <div class="preview-card">
          <div class="preview-header">定制预览</div>
          <div class="preview-body">
            <div class="preview-info" v-if="template.name">
              <h4>{{ template.name }}</h4>
              <p>{{ categoryMap[template.category] }}</p>
            </div>
            <div class="preview-data">
              <div v-for="(layer, index) in previewLayers" :key="index" class="data-row">
                <span class="data-key">{{ layerTypeName(layer) }}</span>
                <span class="data-value" v-if="layer.type === 'text'">{{ layer.content }}</span>
                <img v-else-if="layer.src" :src="layer.src" class="data-img" />
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧：收货信息 -->
      <div class="confirm-form">
        <el-card>
          <template #header>
            <span style="font-weight: 600">收货信息</span>
          </template>

          <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
            <el-form-item label="收货人" prop="receiverName">
              <el-input v-model="form.receiverName" placeholder="请输入收货人姓名" />
            </el-form-item>
            <el-form-item label="手机号" prop="receiverPhone">
              <el-input v-model="form.receiverPhone" placeholder="请输入手机号" />
            </el-form-item>
            <el-form-item label="收货地址" prop="receiverAddress">
              <el-input v-model="form.receiverAddress" type="textarea" :rows="3" placeholder="请输入详细收货地址" />
            </el-form-item>
            <el-form-item label="备注">
              <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="选填，如有特殊要求请备注" />
            </el-form-item>
            <el-form-item label="数量">
              <el-input-number v-model="form.quantity" :min="1" :max="99" />
            </el-form-item>
          </el-form>

          <div class="price-summary">
            <div class="price-row">
              <span>单价</span>
              <span>¥{{ template.price }}</span>
            </div>
            <div class="price-row">
              <span>数量</span>
              <span>x{{ form.quantity }}</span>
            </div>
            <div class="price-row total">
              <span>合计</span>
              <span>¥{{ (template.price * form.quantity).toFixed(2) }}</span>
            </div>
          </div>

          <button class="btn-submit" @click="handleOrder" :disabled="submitting">
            {{ submitting ? '提交中...' : '提交订单' }}
          </button>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getTemplateDetail } from '../api/modules/template'
import { getCustomization } from '../api/modules/customization'
import { createOrder } from '../api/modules/order'

const route = useRoute()
const router = useRouter()
const formRef = ref(null)
const submitting = ref(false)
const template = ref({})
const customData = ref({})
const customizationId = ref(null)

const categoryMap = { ticket: '票根', acrylic: '亚克力立牌', keychain: '钥匙扣', badge: '徽章' }

const previewLayers = computed(() => {
  if (customData.value && customData.value.layers) {
    return customData.value.layers
  }
  return []
})

function layerTypeName(layer) {
  const map = { background: '背景', border: '边框', decoration: '装饰', text: '文字', image: '图片' }
  return layer.name || map[layer.type] || layer.type
}

const form = reactive({
  receiverName: '',
  receiverPhone: '',
  receiverAddress: '',
  remark: '',
  quantity: 1
})

const rules = {
  receiverName: [{ required: true, message: '请输入收货人', trigger: 'blur' }],
  receiverPhone: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
  receiverAddress: [{ required: true, message: '请输入收货地址', trigger: 'blur' }]
}

async function handleOrder() {
  await formRef.value.validate()
  submitting.value = true
  try {
    const orderNo = await createOrder({
      customizationId: customizationId.value,
      totalPrice: template.value.price * form.quantity,
      quantity: form.quantity,
      receiverName: form.receiverName,
      receiverPhone: form.receiverPhone,
      receiverAddress: form.receiverAddress,
      remark: form.remark
    })
    ElMessage.success('下单成功！')
    router.push('/orders')
  } catch (e) {
    ElMessage.error('下单失败')
  } finally {
    submitting.value = false
  }
}

onMounted(async () => {
  const templateId = route.query.templateId
  const custId = route.query.customizationId

  if (templateId) {
    const res = await getTemplateDetail(templateId)
    template.value = res.data
  }

  if (custId) {
    customizationId.value = Number(custId)
    const res = await getCustomization(custId)
    try {
      customData.value = JSON.parse(res.data.customDataJson)
    } catch {
      customData.value = {}
    }
  }
})
</script>

<style scoped>
.page-title {
  font-family: var(--f-display);
  font-size: 28px;
  color: var(--c-brown);
  margin: 20px 0;
}

.confirm-layout {
  display: grid;
  grid-template-columns: 1fr 400px;
  gap: 24px;
}

.preview-card {
  background: var(--c-card);
  border-radius: var(--r-lg);
  overflow: hidden;
  border: 1px solid var(--c-border);
}

.preview-header {
  padding: 12px 20px;
  font-weight: 600;
  font-size: 15px;
  background: var(--c-cream);
  border-bottom: 1px solid var(--c-border);
}

.preview-body {
  padding: 20px;
}

.preview-info h4 {
  font-family: var(--f-heading);
  font-size: 18px;
  color: var(--c-brown);
  margin-bottom: 4px;
}

.preview-info p {
  font-size: 13px;
  color: var(--c-text-muted);
  margin-bottom: 16px;
}

.data-row {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 8px 0;
  border-bottom: 1px solid var(--c-cream-dark);
}

.data-key {
  font-size: 13px;
  color: var(--c-text-muted);
  min-width: 60px;
}

.data-value {
  font-size: 14px;
  color: var(--c-text);
}

.data-img {
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: var(--r-sm);
}

.price-summary {
  margin: 20px 0;
  padding: 16px;
  background: var(--c-cream);
  border-radius: var(--r-md);
}

.price-row {
  display: flex;
  justify-content: space-between;
  font-size: 14px;
  color: var(--c-text-secondary);
  margin-bottom: 8px;
}

.price-row.total {
  font-size: 18px;
  font-weight: 700;
  color: var(--c-orange);
  font-family: var(--f-heading);
  margin-bottom: 0;
  padding-top: 8px;
  border-top: 1px solid var(--c-border);
}

.btn-submit {
  width: 100%;
  padding: 14px;
  font-size: 16px;
  font-weight: 600;
  color: #fff;
  background: var(--c-orange);
  border: none;
  border-radius: var(--r-md);
  cursor: pointer;
  transition: all 0.3s var(--ease-bounce);
  font-family: var(--f-body);
}

.btn-submit:hover:not(:disabled) {
  background: var(--c-orange-dark);
  transform: translateY(-1px);
}

.btn-submit:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}
</style>
