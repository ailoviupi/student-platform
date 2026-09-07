<template>
  <div class="page-container">
    <div class="page-header">
      <h2>奖助学金</h2>
      <el-button type="primary" @click="$router.push('/scholarship-apply')"><el-icon><Plus /></el-icon>申请奖学金</el-button>
    </div>
    <div class="filter-container">
      <el-form :inline="true" :model="queryForm">
        <el-form-item label="类型">
          <el-select v-model="queryForm.scholarshipType" placeholder="全部" clearable>
            <el-option label="国家奖学金" value="NATIONAL" />
            <el-option label="国家励志奖学金" value="MOTIVATIONAL" />
            <el-option label="学院一等奖学金" value="COLLEGE_FIRST" />
            <el-option label="学院二等奖学金" value="COLLEGE_SECOND" />
            <el-option label="学院三等奖学金" value="COLLEGE_THIRD" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryForm.status" placeholder="全部" clearable>
            <el-option label="待评议" :value="0" />
            <el-option label="院系公示" :value="2" />
            <el-option label="学院通过" :value="3" />
            <el-option label="全校公示" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item><el-button type="primary">查询</el-button></el-form-item>
      </el-form>
    </div>
    <div class="table-container">
      <el-table :data="list" border stripe>
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="studentNo" label="学号" width="120" />
        <el-table-column prop="studentName" label="姓名" width="100" />
        <el-table-column prop="scholarshipType" label="类型" width="140">
          <template #default="{ row }">{{ typeMap[row.scholarshipType] }}</template>
        </el-table-column>
        <el-table-column prop="gpa" label="GPA" width="80" align="center" />
        <el-table-column prop="ranking" label="排名" width="80" align="center" />
        <el-table-column prop="amount" label="金额" width="100" align="center">
          <template #default="{ row }">¥{{ row.amount }}</template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="statusMap[row.status].type" size="small">{{ statusMap[row.status].text }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small">详情</el-button>
            <el-button v-if="row.status === 0" type="success" link size="small">评议</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'

const queryForm = reactive({ scholarshipType: '', status: '' })
const list = ref([])
const typeMap = { NATIONAL: '国家奖学金', MOTIVATIONAL: '励志奖学金', COLLEGE_FIRST: '一等', COLLEGE_SECOND: '二等', COLLEGE_THIRD: '三等' }
const statusMap = { 0: { text: '待评议', type: 'warning' }, 2: { text: '院系公示', type: 'primary' }, 3: { text: '学院通过', type: 'success' }, 4: { text: '全校公示', type: 'success' } }

const mockData = [
  { id: 1, studentNo: '202301001', studentName: '陈一', scholarshipType: 'NATIONAL', gpa: 3.85, ranking: 5, amount: 8000, status: 4 },
  { id: 2, studentNo: '202301002', studentName: '林二', scholarshipType: 'MOTIVATIONAL', gpa: 3.72, ranking: 8, amount: 5000, status: 3 },
  { id: 3, studentNo: '202301003', studentName: '黄三', scholarshipType: 'COLLEGE_FIRST', gpa: 3.65, ranking: 12, amount: 2000, status: 2 }
]

onMounted(() => { list.value = mockData })
</script>

<style scoped>
.page-header { display: flex; justify-content: space-between; align-items: center; }
</style>
