<template>
  <div class="page-container">
    <div class="page-header">
      <h2>成绩查询</h2>
    </div>
    
    <div class="filter-container">
      <el-form :inline="true" :model="queryForm">
        <el-form-item label="学期">
          <el-select v-model="queryForm.semester" placeholder="选择学期" clearable>
            <el-option label="2024-2025-1" value="2024-2025-1" />
            <el-option label="2023-2024-2" value="2023-2024-2" />
            <el-option label="2023-2024-1" value="2023-2024-1" />
          </el-select>
        </el-form-item>
        <el-form-item label="课程名称">
          <el-input v-model="queryForm.courseName" placeholder="输入课程名称" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>查询
          </el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="table-container">
      <!-- GPA信息 -->
      <el-row :gutter="20" class="mb-20">
        <el-col :span="6">
          <div class="gpa-card">
            <div class="gpa-label">平均学分绩点(GPA)</div>
            <div class="gpa-value">{{ gpaInfo.gpa }}</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="gpa-card">
            <div class="gpa-label">总学分</div>
            <div class="gpa-value">{{ gpaInfo.totalCredits }}</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="gpa-card">
            <div class="gpa-label">课程数</div>
            <div class="gpa-value">{{ gpaInfo.courseCount }}</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="gpa-card">
            <div class="gpa-label">平均分</div>
            <div class="gpa-value">{{ gpaInfo.avgScore }}</div>
          </div>
        </el-col>
      </el-row>

      <el-table :data="scoreList" border stripe>
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="courseName" label="课程名称" min-width="150" />
        <el-table-column prop="credit" label="学分" width="80" align="center" />
        <el-table-column prop="usualScore" label="平时(30%)" width="100" align="center" />
        <el-table-column prop="midtermScore" label="期中(20%)" width="100" align="center" />
        <el-table-column prop="finalScore" label="期末(50%)" width="100" align="center" />
        <el-table-column prop="totalScore" label="总评" width="100" align="center">
          <template #default="{ row }">
            <span :class="row.totalScore < 60 ? 'text-danger' : 'text-success'">
              {{ row.totalScore }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="point" label="绩点" width="80" align="center" />
        <el-table-column prop="gradeLevel" label="等级" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="getGradeType(row.gradeLevel)" size="small">{{ row.gradeLevel }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="isPass" label="状态" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="row.isPass ? 'success' : 'danger'" size="small">
              {{ row.isPass ? '及格' : '不及格' }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          v-model:current-page="queryForm.pageNum"
          v-model:page-size="queryForm.pageSize"
          :total="total"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next, jumper"
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
  semester: '2024-2025-1',
  courseName: '',
  pageNum: 1,
  pageSize: 20
})

const total = ref(0)
const scoreList = ref([])
const gpaInfo = ref({ gpa: '3.45', totalCredits: '28.5', courseCount: '8', avgScore: '82.3' })

const mockScores = [
  { id: 1, courseName: 'Java程序设计', credit: 4.0, usualScore: 85, midtermScore: 78, finalScore: 82, totalScore: 82.1, point: 3.21, gradeLevel: 'B', isPass: 1 },
  { id: 2, courseName: '数据库原理', credit: 3.5, usualScore: 90, midtermScore: 85, finalScore: 88, totalScore: 87.5, point: 3.75, gradeLevel: 'B', isPass: 1 },
  { id: 3, courseName: '高等数学', credit: 4.0, usualScore: 70, midtermScore: 65, finalScore: 55, totalScore: 61.0, point: 1.10, gradeLevel: 'D', isPass: 1 },
  { id: 4, courseName: '大学英语', credit: 3.0, usualScore: 88, midtermScore: 90, finalScore: 92, totalScore: 90.6, point: 4.06, gradeLevel: 'A', isPass: 1 },
  { id: 5, courseName: '计算机网络', credit: 3.5, usualScore: 75, midtermScore: 70, finalScore: 68, totalScore: 70.1, point: 2.01, gradeLevel: 'C', isPass: 1 },
  { id: 6, courseName: '操作系统', credit: 3.0, usualScore: 80, midtermScore: 75, finalScore: 72, totalScore: 74.5, point: 2.45, gradeLevel: 'C', isPass: 1 },
  { id: 7, courseName: 'Web前端开发', credit: 3.0, usualScore: 92, midtermScore: 88, finalScore: 95, totalScore: 92.6, point: 4.26, gradeLevel: 'A', isPass: 1 },
  { id: 8, courseName: '体育', credit: 1.0, usualScore: 85, midtermScore: 80, finalScore: 85, totalScore: 83.5, point: 3.35, gradeLevel: 'B', isPass: 1 }
]

onMounted(() => {
  scoreList.value = mockScores
  total.value = mockScores.length
})

function handleSearch() {
  scoreList.value = mockScores
  total.value = mockScores.length
}

function handleReset() {
  queryForm.semester = ''
  queryForm.courseName = ''
  queryForm.pageNum = 1
  handleSearch()
}

function getGradeType(level) {
  const map = { A: 'success', B: 'primary', C: 'info', D: 'warning', F: 'danger' }
  return map[level] || 'info'
}
</script>

<style scoped>
.gpa-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  padding: 20px;
  border-radius: 8px;
  text-align: center;
}

.gpa-label {
  font-size: 14px;
  opacity: 0.9;
}

.gpa-value {
  font-size: 28px;
  font-weight: bold;
  margin-top: 8px;
}

.mb-20 {
  margin-bottom: 20px;
}

.text-danger {
  color: #F56C6C;
  font-weight: bold;
}

.text-success {
  color: #67C23A;
  font-weight: bold;
}
</style>
