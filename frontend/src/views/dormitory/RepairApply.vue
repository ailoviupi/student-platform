<template>
  <div class="page-container">
    <div class="page-header"><h2>提交报修</h2></div>
    <el-card class="form-card">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="宿舍房间" prop="roomId">
          <el-select v-model="form.roomId" placeholder="选择房间" style="width: 100%">
            <el-option label="1栋-301" :value="1" />
            <el-option label="1栋-302" :value="2" />
            <el-option label="2栋-201" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="报修类型" prop="repairType">
          <el-select v-model="form.repairType" placeholder="选择类型" style="width: 100%">
            <el-option label="电路" value="ELECTRIC" />
            <el-option label="水路" value="WATER" />
            <el-option label="门锁" value="DOOR" />
            <el-option label="门窗" value="WINDOW" />
            <el-option label="空调" value="AIR_CONDITION" />
            <el-option label="其他" value="OTHER" />
          </el-select>
        </el-form-item>
        <el-form-item label="是否紧急">
          <el-switch v-model="form.isUrgent" />
          <span class="tip">紧急报修30分钟响应</span>
        </el-form-item>
        <el-form-item label="故障描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="4" placeholder="请详细描述故障情况..." />
        </el-form-item>
        <el-form-item label="故障图片">
          <el-upload action="#" :auto-upload="false" list-type="picture-card" :limit="4">
            <el-icon><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="large" @click="handleSubmit">提交报修</el-button>
          <el-button size="large" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'

const formRef = ref()
const form = reactive({ roomId: '', repairType: '', isUrgent: false, description: '' })
const rules = {
  roomId: [{ required: true, message: '请选择房间' }],
  repairType: [{ required: true, message: '请选择报修类型' }],
  description: [{ required: true, message: '请描述故障' }]
}

function handleSubmit() {
  formRef.value.validate(valid => {
    if (valid) ElMessage.success('报修已提交，工作人员将尽快处理')
  })
}
function handleReset() { formRef.value.resetFields() }
</script>

<style scoped>
.form-card { max-width: 700px; }
.tip { margin-left: 10px; color: #E6A23C; font-size: 12px; }
</style>
