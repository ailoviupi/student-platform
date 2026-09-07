<template>
  <div class="page-container">
    <div class="page-header"><h2>成绩录入</h2></div>
    <div class="filter-container">
      <el-form :inline="true">
        <el-form-item label="学期">
          <el-select v-model="semester" placeholder="选择学期">
            <el-option label="2024-2025-1" value="2024-2025-1" />
          </el-select>
        </el-form-item>
        <el-form-item label="课程">
          <el-select v-model="courseId" placeholder="选择课程">
            <el-option label="Java程序设计" :value="1" />
            <el-option label="数据库原理" :value="2" />
          </el-select>
        </el-form-item>
      </el-form>
    </div>
    <div class="table-container">
      <el-table :data="scoreList" border stripe>
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="studentNo" label="学号" width="120" />
        <el-table-column prop="studentName" label="姓名" width="100" />
        <el-table-column label="平时(30%)" width="120">
          <template #default="{ row }">
            <el-input-number v-model="row.usualScore" :min="0" :max="100" size="small" />
          </template>
        </el-table-column>
        <el-table-column label="期中(20%)" width="120">
          <template #default="{ row }">
            <el-input-number v-model="row.midtermScore" :min="0" :max="100" size="small" />
          </template>
        </el-table-column>
        <el-table-column label="期末(50%)" width="120">
          <template #default="{ row }">
            <el-input-number v-model="row.finalScore" :min="0" :max="100" size="small" />
          </template>
        </el-table-column>
        <el-table-column label="总评" width="100" align="center">
          <template #default="{ row }">
            <span>{{ calculateTotal(row) }}</span>
          </template>
        </el-table-column>
      </el-table>
      <div class="mt-20">
        <el-button type="primary" size="large" @click="handleSave">保存</el-button>
        <el-button type="success" size="large" @click="handleLock">锁定成绩</el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'

const semester = ref('2024-2025-1')
const courseId = ref(1)
const scoreList = ref([
  { id: 1, studentNo: '202401001', studentName: '张三', usualScore: 85, midtermScore: 78, finalScore: 82 },
  { id: 2, studentNo: '202401002', studentName: '李四', usualScore: 90, midtermScore: 85, finalScore: 88 },
  { id: 3, studentNo: '202401003', studentName: '王五', usualScore: 70, midtermScore: 65, finalScore: 55 },
  { id: 4, studentNo: '202401004', studentName: '赵六', usualScore: 88, midtermScore: 90, finalScore: 92 }
])

function calculateTotal(row) {
  return (row.usualScore * 0.3 + row.midtermScore * 0.2 + row.finalScore * 0.5).toFixed(2)
}
function handleSave() { ElMessage.success('成绩已保存') }
function handleLock() { ElMessage.success('成绩已锁定') }
</script>

<style scoped>
.mt-20 { margin-top: 20px; }
</style>
