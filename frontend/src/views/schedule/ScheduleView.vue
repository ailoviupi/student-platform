<template>
  <div class="page-container">
    <div class="page-header"><h2>课表查询</h2></div>
    <div class="filter-container">
      <el-form :inline="true">
        <el-form-item label="学期">
          <el-select v-model="semester" placeholder="选择学期">
            <el-option label="2024-2025-1" value="2024-2025-1" />
          </el-select>
        </el-form-item>
        <el-form-item label="班级" v-if="userStore.role !== 'STUDENT'">
          <el-select v-model="classId" placeholder="选择班级">
            <el-option label="软件24-1班" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary">查询</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="table-container">
      <el-table :data="scheduleData" border stripe>
        <el-table-column prop="time" label="时间/节次" width="150" align="center" />
        <el-table-column prop="mon" label="周一" min-width="120" align="center" />
        <el-table-column prop="tue" label="周二" min-width="120" align="center" />
        <el-table-column prop="wed" label="周三" min-width="120" align="center" />
        <el-table-column prop="thu" label="周四" min-width="120" align="center" />
        <el-table-column prop="fri" label="周五" min-width="120" align="center" />
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useUserStore } from '../../store/user'

const userStore = useUserStore()
const semester = ref('2024-2025-1')
const classId = ref(1)

const scheduleData = ref([
  { time: '第1-2节 08:00-09:40', mon: 'Java程序设计 A-301', tue: '', wed: '数据库原理 A-302', thu: '', fri: '大学英语 B-201' },
  { time: '第3-4节 10:00-11:40', mon: '', tue: '高等数学 A-101', wed: '', thu: 'Java程序设计 A-301', fri: '' },
  { time: '第5-6节 14:00-15:40', mon: '计算机网络 B-301', tue: '', wed: '体育 操场', thu: '', fri: '操作系统 A-201' },
  { time: '第7-8节 16:00-17:40', mon: '', tue: 'Web前端开发 C-101', wed: '', thu: '', fri: '' }
])
</script>

<style scoped>
:deep(.el-table .cell) {
  white-space: pre-line;
}
</style>
