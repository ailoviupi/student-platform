<template>
  <div class="page-container">
    <div class="page-header">
      <h2>通知公告</h2>
      <el-button type="primary" @click="$router.push('/notice-publish')"><el-icon><Plus /></el-icon>发布通知</el-button>
    </div>
    <div class="filter-container">
      <el-form :inline="true" :model="queryForm">
        <el-form-item label="类型">
          <el-select v-model="queryForm.noticeType" placeholder="全部" clearable>
            <el-option label="校级" value="SCHOOL" />
            <el-option label="院系" value="DEPT" />
            <el-option label="班级" value="CLASS" />
          </el-select>
        </el-form-item>
        <el-form-item label="标题">
          <el-input v-model="queryForm.keyword" placeholder="搜索标题" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="table-container">
      <el-table :data="noticeList" border stripe>
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="title" label="标题" min-width="250">
          <template #default="{ row }">
            <span class="notice-title-cell" @click="handleDetail(row)">
              <el-tag v-if="row.level === 'URGENT'" type="danger" size="small">急</el-tag>
              {{ row.title }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="noticeType" label="类型" width="80">
          <template #default="{ row }">
            <el-tag size="small">{{ typeMap[row.noticeType] }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="publisherName" label="发布人" width="100" />
        <el-table-column prop="publishTime" label="发布时间" width="170" />
        <el-table-column prop="viewCount" label="浏览" width="80" align="center" />
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleDetail(row)">查看</el-button>
            <el-button type="danger" link size="small" @click="handleWithdraw(row)">撤回</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'

const queryForm = reactive({ noticeType: '', keyword: '', pageNum: 1, pageSize: 20 })
const noticeList = ref([])
const typeMap = { SCHOOL: '校级', DEPT: '院系', CLASS: '班级', DIRECTED: '定向' }

const mockNotices = [
  { id: 1, title: '关于2024年秋季学期开学通知', noticeType: 'SCHOOL', publisherName: '教务处', publishTime: '2024-08-25 09:00', viewCount: 1256, level: 'NORMAL' },
  { id: 2, title: '关于开展奖学金评定工作的通知', noticeType: 'SCHOOL', publisherName: '学生处', publishTime: '2024-08-24 14:30', viewCount: 892, level: 'NORMAL' },
  { id: 3, title: '紧急: 台风期间教学安排调整', noticeType: 'SCHOOL', publisherName: '学校办公室', publishTime: '2024-08-23 18:00', viewCount: 2103, level: 'URGENT' },
  { id: 4, title: '关于暑假宿舍退宿安排', noticeType: 'DEPT', publisherName: '后勤处', publishTime: '2024-08-22 10:00', viewCount: 654, level: 'NORMAL' }
]

onMounted(() => { noticeList.value = mockNotices })
function handleSearch() {}
function handleDetail(row) {}
function handleWithdraw(row) {}
</script>

<style scoped>
.page-header { display: flex; justify-content: space-between; align-items: center; }
.notice-title-cell { cursor: pointer; }
.notice-title-cell:hover { color: #409EFF; }
</style>
