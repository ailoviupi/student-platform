<template>
  <el-container class="layout-container">
    <!-- 侧边栏 -->
    <el-aside :width="isCollapse ? '64px' : '220px'" class="layout-aside">
      <div class="logo-container">
        <h1 v-show="!isCollapse">遂宁工程职院</h1>
        <h1 v-show="isCollapse">遂</h1>
      </div>
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
        router
      >
        <template v-for="item in menuItems" :key="item.path">
          <el-sub-menu v-if="item.children" :index="item.path">
            <template #title>
              <el-icon><component :is="item.icon" /></el-icon>
              <span>{{ item.title }}</span>
            </template>
            <el-menu-item v-for="child in item.children" :key="child.path" :index="'/' + child.path">
              {{ child.title }}
            </el-menu-item>
          </el-sub-menu>
          <el-menu-item v-else :index="'/' + item.path">
            <el-icon><component :is="item.icon" /></el-icon>
            <template #title>{{ item.title }}</template>
          </el-menu-item>
        </template>
      </el-menu>
    </el-aside>

    <el-container>
      <!-- 顶部导航 -->
      <el-header class="layout-header">
        <div class="header-left">
          <el-icon class="collapse-btn" @click="isCollapse = !isCollapse">
            <Fold v-if="!isCollapse" />
            <Expand v-else />
          </el-icon>
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/dashboard' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>{{ currentPageTitle }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="header-right">
          <el-badge :value="3" class="notice-badge">
            <el-icon class="header-icon"><Bell /></el-icon>
          </el-badge>
          <el-dropdown @command="handleCommand">
            <span class="user-info">
              <el-avatar :size="32" icon="UserFilled" />
              <span class="user-name">{{ userStore.realName }}</span>
              <el-icon><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                <el-dropdown-item command="password">修改密码</el-dropdown-item>
                <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <!-- 主内容区 -->
      <el-main class="layout-main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessageBox, ElMessage } from 'element-plus'
import { useUserStore } from '../store/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const isCollapse = ref(false)

const activeMenu = computed(() => route.path)
const currentPageTitle = computed(() => route.meta.title || '')

const menuItems = [
  { path: 'dashboard', title: '首页', icon: 'HomeFilled' },
  {
    path: 'student-mgmt',
    title: '学籍管理',
    icon: 'User',
    children: [
      { path: 'student', title: '学籍信息' },
      { path: 'student-change', title: '学籍异动' }
    ]
  },
  {
    path: 'score-mgmt',
    title: '成绩管理',
    icon: 'DataAnalysis',
    children: [
      { path: 'score', title: '成绩查询' },
      { path: 'score-entry', title: '成绩录入' }
    ]
  },
  { path: 'schedule', title: '课表查询', icon: 'Calendar' },
  {
    path: 'attendance',
    title: '考勤管理',
    icon: 'Document',
    children: [
      { path: 'leave', title: '请假管理' },
      { path: 'leave-apply', title: '请假申请' },
      { path: 'leave-approval', title: '请假审批' }
    ]
  },
  {
    path: 'scholarship-mgmt',
    title: '奖助学金',
    icon: 'Trophy',
    children: [
      { path: 'scholarship', title: '奖助学金' },
      { path: 'scholarship-apply', title: '奖学金申请' }
    ]
  },
  {
    path: 'dormitory',
    title: '宿舍管理',
    icon: 'Tools',
    children: [
      { path: 'repair', title: '报修管理' },
      { path: 'repair-apply', title: '提交报修' }
    ]
  },
  {
    path: 'notice-mgmt',
    title: '通知公告',
    icon: 'Bell',
    children: [
      { path: 'notice', title: '通知列表' },
      { path: 'notice-publish', title: '发布通知' }
    ]
  }
]

async function handleCommand(command) {
  if (command === 'logout') {
    await ElMessageBox.confirm('确定退出登录吗？', '提示', { type: 'warning' })
    userStore.logoutAction()
    ElMessage.success('已退出登录')
    router.push('/login')
  }
}
</script>

<style scoped>
.layout-container {
  height: 100%;
}

.layout-aside {
  background-color: #304156;
  transition: width 0.3s;
  overflow: hidden;
}

.logo-container {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #2b3649;
}

.logo-container h1 {
  color: #fff;
  font-size: 16px;
  font-weight: normal;
  white-space: nowrap;
}

.layout-header {
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
}

.header-left {
  display: flex;
  align-items: center;
}

.collapse-btn {
  font-size: 20px;
  cursor: pointer;
  margin-right: 16px;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.notice-badge {
  cursor: pointer;
}

.header-icon {
  font-size: 20px;
  cursor: pointer;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
}

.user-name {
  font-size: 14px;
  color: #606266;
}

.layout-main {
  background: #f0f2f5;
  padding: 0;
  overflow-y: auto;
}

:deep(.el-menu) {
  border-right: none;
}
</style>
