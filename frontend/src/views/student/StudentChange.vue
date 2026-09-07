<template>
  <div class="page-container">
    <div class="page-header"><h2>学籍异动</h2></div>
    <el-card>
      <el-tabs v-model="activeTab">
        <el-tab-pane label="转专业" name="transfer">
          <el-alert title="转专业条件: 一年级末申请，成绩前30%，无不及格，不跨类别" type="info" show-icon class="mb-20" />
          <el-form :model="transferForm" label-width="120px">
            <el-form-item label="原专业"><el-input v-model="transferForm.originalMajor" disabled /></el-form-item>
            <el-form-item label="目标专业"><el-input v-model="transferForm.targetMajor" placeholder="请输入目标专业" /></el-form-item>
            <el-form-item label="申请理由"><el-input v-model="transferForm.reason" type="textarea" :rows="4" /></el-form-item>
            <el-form-item><el-button type="primary">提交申请</el-button></el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="休学" name="suspend">
          <el-alert title="休学最长2年" type="warning" show-icon class="mb-20" />
          <el-form :model="suspendForm" label-width="120px">
            <el-form-item label="休学原因"><el-input v-model="suspendForm.reason" type="textarea" :rows="4" /></el-form-item>
            <el-form-item label="休学期限"><el-input-number v-model="suspendForm.years" :min="0.5" :max="2" :step="0.5" /> 年</el-form-item>
            <el-form-item><el-button type="primary">提交申请</el-button></el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="复学" name="resume">
          <el-alert title="复学需提前1个月申请" type="info" show-icon class="mb-20" />
          <el-form :model="resumeForm" label-width="120px">
            <el-form-item label="原休学时间"><el-input v-model="resumeForm.suspendDate" disabled /></el-form-item>
            <el-form-item label="申请复学时间"><el-date-picker v-model="resumeForm.resumeDate" type="date" /></el-form-item>
            <el-form-item><el-button type="primary">提交申请</el-button></el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
const activeTab = ref('transfer')
const transferForm = reactive({ originalMajor: '软件技术', targetMajor: '', reason: '' })
const suspendForm = reactive({ reason: '', years: 1 })
const resumeForm = reactive({ suspendDate: '2024-09-01', resumeDate: '' })
</script>

<style scoped>
.mb-20 { margin-bottom: 20px; }
</style>
