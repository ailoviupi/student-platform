<template>
  <div class="page-container">
    <div class="page-header">
      <h2>报修管理</h2>
      <el-button type="primary" @click="$router.push('/repair-apply')"><el-icon><Plus /></el-icon>提交报修</el-button>
    </div>
    <div class="filter-container">
      <el-form :inline="true" :model="queryForm">
        <el-form-item label="状态">
          <el-select v-model="queryForm.status" placeholder="全部" clearable>
            <el-option label="待派单" :value="0" />
            <el-option label="已派单" :value="1" />
            <el-option label="维修中" :value="2" />
            <el-option label="已完成" :value="3" />
            <el-option label="已评价" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="table-container">
      <el-table :data="repairList" border stripe>
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="roomCode" label="房间" width="100" />
        <el-table-column prop="repairType" label="类型" width="100" />
        <el-table-column prop="description" label="故障描述" min-width="200" show-overflow-tooltip />
        <el-table-column prop="isUrgent" label="紧急" width="70" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.isUrgent" type="danger" size="small">紧急</el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="workerName" label="维修工" width="100" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="statusMap[row.status].type" size="small">{{ statusMap[row.status].text }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdTime" label="提交时间" width="170" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button v-if="row.status === 3" type="success" link size="small" @click="handleRating(row)">评价</el-button>
            <el-button type="info" link size="small">详情</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'

const queryForm = reactive({ status: '', pageNum: 1, pageSize: 20 })
const repairList = ref([])
const statusMap = {
  0: { text: '待派单', type: 'warning' },
  1: { text: '已派单', type: 'primary' },
  2: { text: '维修中', type: 'info' },
  3: { text: '已完成', type: 'success' },
  4: { text: '已评价', type: 'success' }
}

const mockRepairs = [
  { id: 1, roomCode: '1-301', repairType: '电路', description: '宿舍灯管不亮', isUrgent: false, workerName: '张师傅', status: 3, createdTime: '2024-08-20 10:30' },
  { id: 2, roomCode: '1-302', repairType: '水路', description: '水龙头漏水', isUrgent: true, workerName: '李师傅', status: 2, createdTime: '2024-08-22 14:20' },
  { id: 3, roomCode: '2-201', repairType: '空调', description: '空调不制冷', isUrgent: false, workerName: '', status: 0, createdTime: '2024-08-25 09:15' }
]

onMounted(() => { repairList.value = mockRepairs })
function handleSearch() {}
function handleRating(row) {}
</script>

<style scoped>
.page-header { display: flex; justify-content: space-between; align-items: center; }
</style>
