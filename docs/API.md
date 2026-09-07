# 遂宁工程职业学院一站式学生工作智能平台 - API文档

## 接口规范

### 统一返回格式
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {},
  "timestamp": "2024-08-28T10:30:00"
}
```

### 状态码说明
| 码值 | 说明 |
|------|------|
| 200 | 成功 |
| 400 | 参数错误 |
| 401 | 未登录或登录已过期 |
| 403 | 无访问权限 |
| 404 | 资源不存在 |
| 500 | 系统错误 |

### 请求头
```
Authorization: Bearer <token>
Content-Type: application/json
```

---

## 一、认证接口

### 1.1 登录
- **URL**: `POST /auth/login`
- **请求体**:
```json
{
  "username": "202401001",
  "password": "123456"
}
```
- **响应**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "tokenType": "Bearer",
    "expiresIn": 86400,
    "userId": 1,
    "username": "202401001",
    "realName": "张三",
    "role": "STUDENT"
  }
}
```

### 1.2 刷新Token
- **URL**: `POST /auth/refresh`
- **请求头**: `Authorization: Bearer <token>`

### 1.3 退出登录
- **URL**: `POST /auth/logout`

---

## 二、成绩管理接口

### 2.1 录入成绩
- **URL**: `POST /score/entry`
- **权限**: TEACHER, ACADEMIC_ADMIN
- **请求体**:
```json
{
  "studentId": 1,
  "studentNo": "202401001",
  "courseId": 1,
  "offeringId": 1,
  "semester": "2024-2025-1",
  "usualScore": 85.00,
  "midtermScore": 78.00,
  "finalScore": 82.00
}
```

### 2.2 批量录入成绩
- **URL**: `POST /score/batch-entry`
- **权限**: TEACHER, ACADEMIC_ADMIN

### 2.3 分页查询成绩
- **URL**: `GET /score/page`
- **参数**: studentId, courseId, offeringId, semester, pageNum, pageSize

### 2.4 计算学期GPA
- **URL**: `GET /score/gpa/{semester}`
- **响应**:
```json
{
  "code": 200,
  "data": {
    "studentId": 1,
    "semester": "2024-2025-1",
    "gpa": 3.45,
    "totalCredits": 28.5,
    "courseCount": 8
  }
}
```

### 2.5 锁定成绩
- **URL**: `PUT /score/lock/{offeringId}`
- **权限**: ACADEMIC_ADMIN

---

## 三、考勤管理接口

### 3.1 申请请假
- **URL**: `POST /attendance/leave/apply`
- **权限**: STUDENT
- **请求体**:
```json
{
  "studentNo": "202401001",
  "leaveType": "PERSONAL",
  "startTime": "2024-08-26T08:00:00",
  "endTime": "2024-08-26T18:00:00",
  "reason": "家中有事需处理"
}
```

### 3.2 审批请假
- **URL**: `POST /attendance/leave/approve`
- **权限**: COUNSELOR, DEPT_ADMIN, ACADEMIC_ADMIN
- **请求体**:
```json
{
  "leaveId": 1,
  "approved": true,
  "remark": "同意"
}
```

### 3.3 分页查询请假记录
- **URL**: `GET /attendance/leave/page`

### 3.4 查询待审批列表
- **URL**: `GET /attendance/leave/pending`

---

## 四、奖助学金接口

### 4.1 申请奖学金
- **URL**: `POST /scholarship/apply`
- **权限**: STUDENT
- **请求体**:
```json
{
  "scholarshipType": "NATIONAL",
  "academicYear": "2024-2025",
  "gpa": 3.85,
  "ranking": 5,
  "totalStudents": 40,
  "reason": "学习成绩优异，综合素质突出"
}
```

### 4.2 班级评议
- **URL**: `POST /scholarship/class-review/{applyId}?approved=true`
- **权限**: COUNSELOR

### 4.3 院系公示
- **URL**: `POST /scholarship/dept-publicity/{applyId}`
- **权限**: DEPT_ADMIN

### 4.4 学院审定
- **URL**: `POST /scholarship/college-approve/{applyId}?approved=true`
- **权限**: ACADEMIC_ADMIN

### 4.5 全校公示
- **URL**: `POST /scholarship/college-publicity/{applyId}`
- **权限**: ACADEMIC_ADMIN

### 4.6 分页查询
- **URL**: `GET /scholarship/page`

---

## 五、宿舍管理接口

### 5.1 提交报修
- **URL**: `POST /dormitory/repair/apply`
- **权限**: STUDENT
- **请求体**:
```json
{
  "studentNo": "202401001",
  "roomId": 1,
  "repairType": "ELECTRIC",
  "description": "宿舍灯管不亮",
  "isUrgent": false
}
```

### 5.2 派单
- **URL**: `POST /dormitory/repair/dispatch/{repairId}?workerId=1&workerName=张师傅&workerPhone=13800138000`
- **权限**: ACADEMIC_ADMIN

### 5.3 开始维修
- **URL**: `POST /dormitory/repair/start/{repairId}`

### 5.4 完成维修
- **URL**: `POST /dormitory/repair/finish/{repairId}`

### 5.5 评价报修
- **URL**: `POST /dormitory/repair/rating`
- **请求体**:
```json
{
  "repairId": 1,
  "rating": 5,
  "ratingContent": "维修及时，服务态度好"
}
```

### 5.6 分页查询报修
- **URL**: `GET /dormitory/repair/page`

---

## 六、通知公告接口

### 6.1 发布通知
- **URL**: `POST /notice/publish`
- **权限**: COUNSELOR, DEPT_ADMIN, ACADEMIC_ADMIN, SYS_ADMIN
- **请求体**:
```json
{
  "title": "关于2024年秋季学期开学通知",
  "content": "各位同学，新学期将于9月1日正式上课...",
  "noticeType": "SCHOOL",
  "level": "NORMAL",
  "requireReceipt": true,
  "scheduledTime": "2024-08-29T08:00:00"
}
```

### 6.2 撤回通知
- **URL**: `PUT /notice/withdraw/{noticeId}`

### 6.3 分页查询通知
- **URL**: `GET /notice/page`

### 6.4 获取通知详情
- **URL**: `GET /notice/{noticeId}`

---

## 七、学籍管理接口

### 7.1 查询学籍信息
- **URL**: `GET /student/info`

### 7.2 申请学籍异动
- **URL**: `POST /student/change`
- **请求体**:
```json
{
  "changeType": "TRANSFER_MAJOR",
  "reason": "对目标专业有浓厚兴趣",
  "targetMajorId": 2
}
```

### 7.3 申请在读证明
- **URL**: `POST /student/enrollment-proof`

---

## 限流规则

| 接口类型 | 限制 |
|---------|------|
| 普通接口 | 100次/分钟 |
| 批量接口 | 10次/分钟 |
| 敏感操作 | 需二次验证 |
