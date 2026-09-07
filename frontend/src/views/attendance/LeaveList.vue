<template>
  <div class="page-container">
    <div class="page-header">
      <h2>请假管理</h2>
      <el-button type="primary" @click="$router.push('/leave-apply')">
        <el-icon><Plus /></el-icon>新建请假
      </el-button>
    </div>

    <div class="filter-container">
      <el-form :inline="true" :model="queryForm">
        <el-form-item label="请假类型">
          <el-select v-model="queryForm.leaveType" placeholder="全部" clearable>
            <el-option label="事假" value="PERSONAL" />
            <el-option label="病假" value="SICK" />
            <el-option label="公假" value="PUBLIC" />
          </el-select>
        </el-form-item>
        <el-form-item label="审批状态">
          <el-select v-model="queryForm.status" placeholder="全部" clearable>
            <el-option label="待审批" :value="0" />
            <el-option label="已通过" :value="1" />
            <el-option label="已驳回" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="申请时间">
          <el-date-picker v-model="queryForm.dateRange" type="daterange" range-separator="至" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="table-container">
      <el-table :data="leaveList" border stripe>
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
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="statusMap[row.status].type" size="small">
              {{ statusMap[row.status].text }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button v-if="row.status === 0" type="primary" link size="small" @click="handleApprove(row)">审批</el-button>
            <el-button type="info" link size="small" @click="handleDetail(row)">详情</el-button>
            <el-button v-if="row.status === 0" type="danger" link size="small" @click="handleCancel(row)">撤销</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          v-model:current-page="queryForm.pageNum"
          v-model:page-size="queryForm.pageSize"
          :total="total"
          layout="total, sizes, prev, pager, next"
          @size-change="handleSearch"
          @current-change="handleSearch"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'

const queryForm = reactive({
  leaveType: '',
  status: '',
  dateRange: [],
  pageNum: 1,
  pageSize: 20
})

const total = ref(0)
const leaveList = ref([])

const typeMap = { PERSONAL: '事假', SICK: '病假', PUBLIC: '公假' }
const statusMap = {
  0: { text: '待审批', type: 'warning' },
  1: { text: '已通过', type: 'success' },
  2: { text: '已驳回', type: 'danger' },
  3: { text: '已撤销', type: 'info' }
}

const mockLeaves = [
  { id: 1, studentNo: '202401001', studentName: '张三', leaveType: 'PERSONAL', startTime: '2024-08-26 08:00', endTime: '2024-08-26 18:00', days: 1, reason: '家中有事需处理', status: 0 },
  { id: 2, studentNo: '202401002', studentName: '李四', leaveType: 'SICK', startTime: '2024-08-25 08:00', endTime: '2024-08-27 18:00', days: 3, reason: '感冒发烧，需休息', status: 1 },
  { id: 3, studentNo: '202401003', studentName: '王五', leaveType: 'PUBLIC', startTime: '2024-08-28 08:00', endTime: '2024-08-28 18:00', days: 1, reason: '参加市级技能竞赛', status: 1 },
  { id: 4, studentNo: '202401004', studentName: '赵六', leaveType: 'PERSONAL', startTime: '2024-08-29 08:00', endTime: '2024-09-02 18:00', days: 5, reason: '回家处理急事', status: 2 }
]

onMounted(() => {
  leaveList.value = mockLeaves
  total.value = mockLeaves.length
})

function handleSearch() {}
function handleReset() {
  queryForm.leaveType = ''
  queryForm.status = ''
  queryForm.dateRange = []
  queryForm.pageNum = 1
}
function handleApprove(row) {}
function handleDetail(row) {}
function handleCancel(row) {}
</script>

<style scoped>
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
