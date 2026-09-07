<template>
  <div class="page-container">
    <div class="page-header"><h2>请假审批</h2></div>
    <div class="table-container">
      <el-table :data="approvalList" border stripe>
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="studentNo" label="学号" width="120" />
        <el-table-column prop="studentName" label="姓名" width="100" />
        <el-table-column prop="leaveType" label="类型" width="80">
          <template #default="{ row }">
            <el-tag size="small">{{ typeMap[row.leaveType] }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="startTime" label="开始时间" width="170" />
        <el-table-column prop="endTime" label="结束时间" width="170" />
        <el-table-column prop="days" label="天数" width="70" align="center" />
        <el-table-column prop="reason" label="事由" min-width="200" show-overflow-tooltip />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="success" link size="small" @click="handleApprove(row, true)">通过</el-button>
            <el-button type="danger" link size="small" @click="handleApprove(row, false)">驳回</el-button>
            <el-button type="info" link size="small">详情</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'

const approvalList = ref([])
const typeMap = { PERSONAL: '事假', SICK: '病假', PUBLIC: '公假' }

const mockData = [
  { id: 1, studentNo: '202401001', studentName: '张三', leaveType: 'PERSONAL', startTime: '2024-08-26 08:00', endTime: '2024-08-26 18:00', days: 1, reason: '家中有事需处理' },
  { id: 2, studentNo: '202401005', studentName: '钱七', leaveType: 'SICK', startTime: '2024-08-27 08:00', endTime: '2024-08-28 18:00', days: 2, reason: '感冒发烧' }
]

onMounted(() => { approvalList.value = mockData })
function handleApprove(row, approved) {
  ElMessage.success(approved ? '已通过' : '已驳回')
}
</script>
