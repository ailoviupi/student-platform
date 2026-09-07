<template>
  <div class="page-container">
    <div class="page-header">
      <h2>请假申请</h2>
    </div>

    <el-card class="form-card">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="请假类型" prop="leaveType">
              <el-radio-group v-model="form.leaveType">
                <el-radio label="PERSONAL">事假</el-radio>
                <el-radio label="SICK">病假</el-radio>
                <el-radio label="PUBLIC">公假</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否紧急">
              <el-switch v-model="form.isUrgent" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="开始时间" prop="startTime">
              <el-date-picker
                v-model="form.startTime"
                type="datetime"
                placeholder="选择开始时间"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束时间" prop="endTime">
              <el-date-picker
                v-model="form.endTime"
                type="datetime"
                placeholder="选择结束时间"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="请假天数">
          <el-input-number v-model="form.days" :min="0.5" :step="0.5" disabled />
          <span class="tip">天 (根据时间自动计算)</span>
        </el-form-item>

        <el-form-item label="请假事由" prop="reason">
          <el-input
            v-model="form.reason"
            type="textarea"
            :rows="4"
            placeholder="请详细说明请假事由..."
          />
        </el-form-item>

        <el-form-item label="证明材料" v-if="form.leaveType === 'SICK'">
          <el-upload
            action="#"
            :auto-upload="false"
            list-type="picture-card"
            :limit="3"
          >
            <el-icon><Plus /></el-icon>
            <template #tip>
              <div class="upload-tip">病假需提供医院证明(最多3张)</div>
            </template>
          </el-upload>
        </el-form-item>

        <el-form-item label="审批流程">
          <el-steps :active="1" finish-status="success" simple>
            <el-step title="辅导员审批" />
            <el-step title="院系审批" v-if="form.days > 1" />
            <el-step title="学工部审批" v-if="form.days > 3" />
            <el-step title="完成" />
          </el-steps>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" size="large" @click="handleSubmit">提交申请</el-button>
          <el-button size="large" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 请假规则提示 -->
    <el-card class="mt-20">
      <template #header>
        <span><el-icon><InfoFilled /></el-icon> 请假规则说明</span>
      </template>
      <el-descriptions :column="1" border>
        <el-descriptions-item label="事假">≤1天辅导员批，1-3天院系批，>3天学工部批；学期累计≤30天</el-descriptions-item>
        <el-descriptions-item label="病假">需医院证明，3天内辅导员批</el-descriptions-item>
        <el-descriptions-item label="公假">需组织证明备案</el-descriptions-item>
        <el-descriptions-item label="考试周">考试周不批事假</el-descriptions-item>
        <el-descriptions-item label="出勤预警">出勤率<85%预警，旷课>10学时重点预警</el-descriptions-item>
      </el-descriptions>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, watch } from 'vue'
import { ElMessage } from 'element-plus'

const formRef = ref()
const form = reactive({
  leaveType: 'PERSONAL',
  isUrgent: false,
  startTime: '',
  endTime: '',
  days: 1,
  reason: ''
})

const rules = {
  leaveType: [{ required: true, message: '请选择请假类型', trigger: 'change' }],
  startTime: [{ required: true, message: '请选择开始时间', trigger: 'change' }],
  endTime: [{ required: true, message: '请选择结束时间', trigger: 'change' }],
  reason: [{ required: true, message: '请填写请假事由', trigger: 'blur' }]
}

// 自动计算天数
watch([() => form.startTime, () => form.endTime], ([start, end]) => {
  if (start && end) {
    const diff = (new Date(end) - new Date(start)) / (1000 * 60 * 60 * 24)
    form.days = Math.max(0.5, Math.round(diff * 2) / 2)
  }
})

function handleSubmit() {
  formRef.value.validate(valid => {
    if (valid) {
      ElMessage.success('请假申请已提交，请等待审批')
    }
  })
}

function handleReset() {
  formRef.value.resetFields()
}
</script>

<style scoped>
.form-card {
  max-width: 800px;
}

.tip {
  margin-left: 10px;
  color: #909399;
  font-size: 12px;
}

.upload-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 8px;
}

.mt-20 {
  margin-top: 20px;
}
</style>
