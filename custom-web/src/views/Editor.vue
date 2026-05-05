<template>
  <div class="editor-page">
    <div class="editor-header">
      <el-page-header @back="$router.back()">
        <template #content>
          <span>在线定制编辑器</span>
        </template>
      </el-page-header>
      <div class="editor-actions">
        <el-button @click="saveDraft">保存草稿</el-button>
        <el-button type="primary" @click="submitOrder">提交订单</el-button>
      </div>
    </div>

    <div class="editor-body">
      <!-- 画布区域 -->
      <div class="canvas-area">
        <div class="canvas" ref="canvasRef">
          <div class="canvas-placeholder">
            <p>画布区域</p>
            <p class="hint">选择模板后在此编辑预览</p>
          </div>
        </div>
      </div>

      <!-- 右侧属性面板 -->
      <div class="property-panel">
        <h3>定制属性</h3>
        <el-form label-width="80px">
          <el-form-item label="文字内容">
            <el-input v-model="customData.text" placeholder="输入自定义文字" />
          </el-form-item>
          <el-form-item label="字体颜色">
            <el-color-picker v-model="customData.textColor" />
          </el-form-item>
          <el-form-item label="上传图片">
            <el-upload
              action="/api/file/upload"
              :show-file-list="false"
              :on-success="handleUploadSuccess"
            >
              <el-button>选择图片</el-button>
            </el-upload>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'

const route = useRoute()
const canvasRef = ref(null)

const customData = reactive({
  text: '',
  textColor: '#333333',
  imageUrl: ''
})

function handleUploadSuccess(response) {
  customData.imageUrl = response.data
  ElMessage.success('图片上传成功')
}

function saveDraft() {
  // TODO: 保存定制方案
  ElMessage.success('草稿已保存')
}

function submitOrder() {
  // TODO: 跳转到订单确认页
  ElMessage.info('提交订单功能开发中')
}
</script>

<style scoped>
.editor-page {
  height: calc(100vh - 60px);
  display: flex;
  flex-direction: column;
}

.editor-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 20px;
  background: #fff;
  border-bottom: 1px solid #e4e7ed;
}

.editor-body {
  flex: 1;
  display: flex;
  overflow: hidden;
}

.canvas-area {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f0f2f5;
}

.canvas {
  width: 400px;
  height: 560px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  justify-content: center;
}

.canvas-placeholder {
  text-align: center;
  color: #c0c4cc;
}

.canvas-placeholder .hint {
  font-size: 13px;
  margin-top: 8px;
}

.property-panel {
  width: 320px;
  background: #fff;
  padding: 20px;
  border-left: 1px solid #e4e7ed;
  overflow-y: auto;
}

.property-panel h3 {
  margin-bottom: 20px;
  font-size: 16px;
}
</style>
