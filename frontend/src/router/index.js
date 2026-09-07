import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '../store/user'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/',
    component: () => import('../views/Layout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('../views/Dashboard.vue'),
        meta: { title: '首页', icon: 'HomeFilled' }
      },
      // 学籍管理
      {
        path: 'student',
        name: 'Student',
        component: () => import('../views/student/StudentInfo.vue'),
        meta: { title: '学籍信息', icon: 'User' }
      },
      {
        path: 'student-change',
        name: 'StudentChange',
        component: () => import('../views/student/StudentChange.vue'),
        meta: { title: '学籍异动', icon: 'Switch' }
      },
      // 成绩管理
      {
        path: 'score',
        name: 'Score',
        component: () => import('../views/score/ScoreList.vue'),
        meta: { title: '成绩查询', icon: 'DataAnalysis' }
      },
      {
        path: 'score-entry',
        name: 'ScoreEntry',
        component: () => import('../views/score/ScoreEntry.vue'),
        meta: { title: '成绩录入', icon: 'Edit' }
      },
      // 课表
      {
        path: 'schedule',
        name: 'Schedule',
        component: () => import('../views/schedule/ScheduleView.vue'),
        meta: { title: '课表查询', icon: 'Calendar' }
      },
      // 考勤请假
      {
        path: 'leave',
        name: 'Leave',
        component: () => import('../views/attendance/LeaveList.vue'),
        meta: { title: '请假管理', icon: 'Document' }
      },
      {
        path: 'leave-apply',
        name: 'LeaveApply',
        component: () => import('../views/attendance/LeaveApply.vue'),
        meta: { title: '请假申请', icon: 'Plus' }
      },
      {
        path: 'leave-approval',
        name: 'LeaveApproval',
        component: () => import('../views/attendance/LeaveApproval.vue'),
        meta: { title: '请假审批', icon: 'Check' }
      },
      // 奖助学金
      {
        path: 'scholarship',
        name: 'Scholarship',
        component: () => import('../views/scholarship/ScholarshipList.vue'),
        meta: { title: '奖助学金', icon: 'Trophy' }
      },
      {
        path: 'scholarship-apply',
        name: 'ScholarshipApply',
        component: () => import('../views/scholarship/ScholarshipApply.vue'),
        meta: { title: '奖学金申请', icon: 'Plus' }
      },
      // 宿舍报修
      {
        path: 'repair',
        name: 'Repair',
        component: () => import('../views/dormitory/RepairList.vue'),
        meta: { title: '报修管理', icon: 'Tools' }
      },
      {
        path: 'repair-apply',
        name: 'RepairApply',
        component: () => import('../views/dormitory/RepairApply.vue'),
        meta: { title: '提交报修', icon: 'Plus' }
      },
      // 通知公告
      {
        path: 'notice',
        name: 'Notice',
        component: () => import('../views/notice/NoticeList.vue'),
        meta: { title: '通知公告', icon: 'Bell' }
      },
      {
        path: 'notice-publish',
        name: 'NoticePublish',
        component: () => import('../views/notice/NoticePublish.vue'),
        meta: { title: '发布通知', icon: 'Edit' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  if (to.path !== '/login' && !userStore.token) {
    next('/login')
  } else {
    next()
  }
})

export default router
