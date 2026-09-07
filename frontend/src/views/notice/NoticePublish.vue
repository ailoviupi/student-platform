<template>
  <div class="page-container">
    <div class="page-header"><h2>发布通知</h2></div>
    <el-card class="form-card">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="通知标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入标题" maxlength="200" show-word-limit />
        </el-form-item>
        <el-form-item label="通知类型" prop="noticeType">
          <el-radio-group v-model="form.noticeType">
            <el-radio label="SCHOOL">校级</el-radio>
            <el-radio label="DEPT">院系</el-radio>
            <el-radio label="CLASS">班级</el-radio>
            <el-radio label="DIRECTED">定向</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="通知级别">
          <el-radio-group v-model="form.level">
            <el-radio label="NORMAL">普通</el-radio>
            <el-radio label="URGENT">紧急</el-radio>
            <el-radio label="EMERGENCY">特急</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="通知内容" prop="content">
          <el-input v-model="form.content" type="textarea" :rows="10" placeholder="请输入通知内容..." />
        </el-form-item>
        <el-form-item label="附件上传">
          <el-upload action="#" :auto-upload="false" :limit="5">
            <el-button type="primary">选择文件</el-button>
            <template #tip>
              <div class="upload-tip">单个文件不超过20MB，最多5个文件</div>
            </template>
          </el-upload>
        </el-form-item>
        <el-form-item label="需要回执">
          <el-switch v-model="form.requireReceipt" />
        </el-form-item>
        <el-form-item label="发布方式">
          <el-radio-group v-model="publishType">
            <el-radio label="now">立即发布</el-radio>
            <el-radio label="scheduled">定时发布</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="publishType === 'scheduled'" label="定时时间">
          <el-date-picker v-model="form.scheduledTime" type="datetime" placeholder="选择发布时间" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="large" @click="handleSubmit">发布</el-button>
          <el-button size="large" @click="handleSaveDraft">保存草稿</el-button>
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
const publishType = ref('now')
const form = reactive({ title: '', noticeType: 'SCHOOL', level: 'NORMAL', content: '', requireReceipt: false, scheduledTime: '' })
const rules = {
  title: [{ required: true, message: '请输入标题' }],
  noticeType: [{ required: true, message: '请选择类型' }],
  content: [{ required: true, message: '请输入内容' }]
}

function handleSubmit() {
  formRef.value.validate(valid => {
    if (valid) ElMessage.success('通知发布成功')
  })
}
function handleSaveDraft() { ElMessage.success('草稿已保存') }
function handleReset() { formRef.value.resetFields() }
</script>

<style scoped>
.form-card { max-width: 800px; }
.upload-tip { font-size: 12px; color: #909399; margin-top: 8px; }
</style>
