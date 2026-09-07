<template>
  <div class="page-container">
    <!-- 欢迎卡片 -->
    <el-row :gutter="20">
      <el-col :span="16">
        <el-card class="welcome-card">
          <div class="welcome-content">
            <h2>您好，{{ userStore.realName }}！</h2>
            <p>欢迎使用遂宁工程职业学院一站式学生工作智能平台</p>
            <p class="motto">厚德精技 · 求实创新</p>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="time-card">
          <div class="current-time">
            <div class="time">{{ currentTime }}</div>
            <div class="date">{{ currentDate }}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 数据统计 -->
    <el-row :gutter="20" class="mt-20">
      <el-col :span="6" v-for="item in statCards" :key="item.title">
        <el-card class="stat-card" :body-style="{ padding: '20px' }">
          <div class="stat-content">
            <el-icon :size="40" :color="item.color"><component :is="item.icon" /></el-icon>
            <div class="stat-info">
              <div class="stat-value">{{ item.value }}</div>
              <div class="stat-title">{{ item.title }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 待办事项与通知 -->
    <el-row :gutter="20" class="mt-20">
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>待办事项</span>
              <el-tag type="danger">{{ pendingTasks.length }}</el-tag>
            </div>
          </template>
          <div class="todo-list">
            <div v-for="task in pendingTasks" :key="task.id" class="todo-item">
              <el-icon :color="task.urgent ? '#F56C6C' : '#E6A23C'"><Warning /></el-icon>
              <span class="todo-text">{{ task.content }}</span>
              <span class="todo-time">{{ task.time }}</span>
            </div>
            <el-empty v-if="pendingTasks.length === 0" description="暂无待办" :image-size="60" />
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>最新通知</span>
              <el-button text type="primary" @click="$router.push('/notice')">更多</el-button>
            </div>
          </template>
          <div class="notice-list">
            <div v-for="notice in latestNotices" :key="notice.id" class="notice-item" @click="$router.push('/notice/' + notice.id)">
              <el-tag :type="notice.level === 'URGENT' ? 'danger' : 'info'" size="small">
                {{ notice.level === 'URGENT' ? '紧急' : '普通' }}
              </el-tag>
              <span class="notice-title">{{ notice.title }}</span>
              <span class="notice-time">{{ notice.time }}</span>
            </div>
            <el-empty v-if="latestNotices.length === 0" description="暂无通知" :image-size="60" />
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useUserStore } from '../store/user'
import dayjs from 'dayjs'

const userStore = useUserStore()
const currentTime = ref('')
const currentDate = ref('')
let timer = null

const statCards = ref([
  { title: '在校学生', value: '8,562', icon: 'User', color: '#409EFF' },
  { title: '今日请假', value: '23', icon: 'Document', color: '#E6A23C' },
  { title: '待处理报修', value: '12', icon: 'Tools', color: '#F56C6C' },
  { title: '本月通知', value: '45', icon: 'Bell', color: '#67C23A' }
])

const pendingTasks = ref([
  { id: 1, content: '审批学生请假申请(3条)', urgent: true, time: '10分钟前' },
  { id: 2, content: '成绩录入截止提醒', urgent: true, time: '1小时前' },
  { id: 3, content: '奖学金评议工作', urgent: false, time: '2小时前' },
  { id: 4, content: '毕业审核材料核对', urgent: false, time: '3小时前' }
])

const latestNotices = ref([
  { id: 1, title: '关于2024年秋季学期开学通知', level: 'NORMAL', time: '2024-08-25' },
  { id: 2, title: '关于开展奖学金评定工作的通知', level: 'NORMAL', time: '2024-08-24' },
  { id: 3, title: '紧急: 台风期间教学安排调整', level: 'URGENT', time: '2024-08-23' },
  { id: 4, title: '关于暑假宿舍退宿安排', level: 'NORMAL', time: '2024-08-22' }
])

function updateTime() {
  currentTime.value = dayjs().format('HH:mm:ss')
  currentDate.value = dayjs().format('YYYY年MM月DD日 dddd')
}

onMounted(() => {
  updateTime()
  timer = setInterval(updateTime, 1000)
})

onUnmounted(() => {
  clearInterval(timer)
})
</script>

<style scoped>
.welcome-card {
  height: 160px;
}

.welcome-content h2 {
  font-size: 22px;
  color: #303133;
  margin-bottom: 10px;
}

.welcome-content p {
  color: #606266;
  margin-bottom: 5px;
}

.motto {
  color: #909399;
  font-size: 14px;
  letter-spacing: 2px;
}

.time-card {
  height: 160px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.current-time {
  text-align: center;
}

.time {
  font-size: 36px;
  font-weight: bold;
  color: #409EFF;
}

.date {
  font-size: 14px;
  color: #909399;
  margin-top: 8px;
}

.mt-20 {
  margin-top: 20px;
}

.stat-card {
  cursor: pointer;
  transition: transform 0.2s;
}

.stat-card:hover {
  transform: translateY(-4px);
}

.stat-content {
  display: flex;
  align-items: center;
  gap: 16px;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}

.stat-title {
  font-size: 14px;
  color: #909399;
  margin-top: 4px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.todo-list, .notice-list {
  max-height: 240px;
  overflow-y: auto;
}

.todo-item, .notice-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 0;
  border-bottom: 1px solid #EBEEF5;
  cursor: pointer;
}

.todo-item:last-child, .notice-item:last-child {
  border-bottom: none;
}

.todo-text, .notice-title {
  flex: 1;
  font-size: 14px;
  color: #606266;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.todo-time, .notice-time {
  font-size: 12px;
  color: #C0C4CC;
}
</style>
