<template>
  <div class="page-container">
    <div class="page-header"><h2>奖学金申请</h2></div>
    <el-card class="form-card">
      <el-alert title="申请条件: 成绩前30%，无不及格，处分未解除/欠费/造假不得申请" type="info" show-icon class="mb-20" />
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="奖学金类型" prop="scholarshipType">
          <el-select v-model="form.scholarshipType" placeholder="选择类型" style="width: 100%">
            <el-option label="国家奖学金(¥8000)" value="NATIONAL" />
            <el-option label="国家励志奖学金(¥5000)" value="MOTIVATIONAL" />
            <el-option label="学院一等奖学金(¥2000)" value="COLLEGE_FIRST" />
            <el-option label="学院二等奖学金(¥1000)" value="COLLEGE_SECOND" />
            <el-option label="学院三等奖学金(¥500)" value="COLLEGE_THIRD" />
            <el-option label="单项奖学金(¥300)" value="SINGLE" />
          </el-select>
        </el-form-item>
        <el-form-item label="申请学年" prop="academicYear">
          <el-select v-model="form.academicYear" placeholder="选择学年" style="width: 100%">
            <el-option label="2024-2025" value="2024-2025" />
            <el-option label="2023-2024" value="2023-2024" />
          </el-select>
        </el-form-item>
        <el-form-item label="平均学分绩点" prop="gpa">
          <el-input-number v-model="form.gpa" :min="0" :max="4" :precision="2" :step="0.01" style="width: 100%" />
        </el-form-item>
        <el-form-item label="专业排名" prop="ranking">
          <el-input-number v-model="form.ranking" :min="1" style="width: 100%" />
          <span class="tip">共 {{ form.totalStudents || 0 }} 人，前30%可申请</span>
        </el-form-item>
        <el-form-item label="申请理由" prop="reason">
          <el-input v-model="form.reason" type="textarea" :rows="4" placeholder="请填写申请理由..." />
        </el-form-item>
        <el-form-item label="审批流程">
          <el-steps :active="0" finish-status="success" simple>
            <el-step title="申请" />
            <el-step title="班级评议" />
            <el-step title="院系公示(3天)" />
            <el-step title="学院审定" />
            <el-step title="全校公示(5天)" />
          </el-steps>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="large" @click="handleSubmit">提交申请</el-button>
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
const form = reactive({ scholarshipType: '', academicYear: '', gpa: 3.5, ranking: 1, totalStudents: 40, reason: '' })
const rules = {
  scholarshipType: [{ required: true, message: '请选择奖学金类型' }],
  academicYear: [{ required: true, message: '请选择学年' }],
  gpa: [{ required: true, message: '请输入GPA' }],
  ranking: [{ required: true, message: '请输入排名' }],
  reason: [{ required: true, message: '请填写申请理由' }]
}

function handleSubmit() {
  formRef.value.validate(valid => {
    if (valid) ElMessage.success('申请已提交，请等待班级评议')
  })
}
function handleReset() { formRef.value.resetFields() }
</script>

<style scoped>
.form-card { max-width: 700px; }
.mb-20 { margin-bottom: 20px; }
.tip { margin-left: 10px; color: #909399; font-size: 12px; }
</style>
