SET NAMES utf8mb4;
SET CHARACTER_SET_CLIENT=utf8mb4;
SET CHARACTER_SET_CONNECTION=utf8mb4;
SET CHARACTER_SET_RESULTS=utf8mb4;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
-- ============================================================
-- 遂宁工程职业学院 一站式学生工作智能平台
-- 数据库初始化脚本 MySQL 8.0
-- 表名规范: sm_模块_表名
-- 字段规范: 小写下划线命名
-- 审计字段: created_by, created_time, updated_by, updated_time, deleted
-- ============================================================

CREATE DATABASE IF NOT EXISTS snvec_student_platform
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE snvec_student_platform;

-- ============================================================
-- 一、用户与权限模块
-- ============================================================

-- 1.1 用户表
CREATE TABLE sm_user (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名(工号/学号)',
    password VARCHAR(255) NOT NULL COMMENT '密码(BCrypt加密)',
    real_name VARCHAR(50) NOT NULL COMMENT '真实姓名',
    role ENUM('STUDENT','TEACHER','COUNSELOR','DEPT_ADMIN','ACADEMIC_ADMIN','SYS_ADMIN','PARENT') NOT NULL COMMENT '角色',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 0-禁用 1-启用',
    email VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    phone VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    avatar VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
    last_login_time DATETIME DEFAULT NULL COMMENT '最后登录时间',
    last_login_ip VARCHAR(50) DEFAULT NULL COMMENT '最后登录IP',
    created_by BIGINT DEFAULT NULL COMMENT '创建人',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by BIGINT DEFAULT NULL COMMENT '更新人',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-未删 1-已删',
    PRIMARY KEY (id),
    UNIQUE KEY uk_username (username),
    KEY idx_role (role),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 1.2 角色表
CREATE TABLE sm_role (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    role_code VARCHAR(50) NOT NULL COMMENT '角色编码',
    role_name VARCHAR(50) NOT NULL COMMENT '角色名称',
    description VARCHAR(255) DEFAULT NULL COMMENT '角色描述',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态',
    created_by BIGINT DEFAULT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT DEFAULT NULL,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    UNIQUE KEY uk_role_code (role_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 1.3 用户角色关联表
CREATE TABLE sm_user_role (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_role (user_id, role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- 1.4 权限表
CREATE TABLE sm_permission (
    id BIGINT NOT NULL AUTO_INCREMENT,
    parent_id BIGINT NOT NULL DEFAULT 0 COMMENT '父权限ID',
    perm_code VARCHAR(100) NOT NULL COMMENT '权限编码',
    perm_name VARCHAR(100) NOT NULL COMMENT '权限名称',
    perm_type TINYINT NOT NULL COMMENT '类型: 1-菜单 2-按钮 3-接口',
    path VARCHAR(255) DEFAULT NULL COMMENT '路由路径',
    icon VARCHAR(100) DEFAULT NULL COMMENT '图标',
    sort_order INT NOT NULL DEFAULT 0 COMMENT '排序',
    status TINYINT NOT NULL DEFAULT 1,
    created_by BIGINT DEFAULT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT DEFAULT NULL,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    UNIQUE KEY uk_perm_code (perm_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限表';

-- 1.5 角色权限关联表
CREATE TABLE sm_role_permission (
    id BIGINT NOT NULL AUTO_INCREMENT,
    role_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_role_perm (role_id, permission_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限关联表';

-- 1.6 操作日志表
CREATE TABLE sm_operation_log (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT DEFAULT NULL COMMENT '操作用户ID',
    username VARCHAR(50) DEFAULT NULL COMMENT '用户名',
    operation VARCHAR(100) DEFAULT NULL COMMENT '操作描述',
    method VARCHAR(255) DEFAULT NULL COMMENT '请求方法',
    params TEXT DEFAULT NULL COMMENT '请求参数',
    ip VARCHAR(50) DEFAULT NULL COMMENT 'IP地址',
    duration BIGINT DEFAULT NULL COMMENT '执行时长(ms)',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 0-失败 1-成功',
    error_msg TEXT DEFAULT NULL COMMENT '错误信息',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_user_id (user_id),
    KEY idx_created_time (created_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

-- ============================================================
-- 二、学籍管理模块
-- ============================================================

-- 2.1 院系表
CREATE TABLE sm_dept (
    id BIGINT NOT NULL AUTO_INCREMENT,
    dept_code VARCHAR(20) NOT NULL COMMENT '院系编码',
    dept_name VARCHAR(100) NOT NULL COMMENT '院系名称',
    leader VARCHAR(50) DEFAULT NULL COMMENT '负责人',
    phone VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
    sort_order INT NOT NULL DEFAULT 0,
    status TINYINT NOT NULL DEFAULT 1,
    created_by BIGINT DEFAULT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT DEFAULT NULL,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    UNIQUE KEY uk_dept_code (dept_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='院系表';

-- 2.2 专业表
CREATE TABLE sm_major (
    id BIGINT NOT NULL AUTO_INCREMENT,
    major_code VARCHAR(20) NOT NULL COMMENT '专业编码',
    major_name VARCHAR(100) NOT NULL COMMENT '专业名称',
    dept_id BIGINT NOT NULL COMMENT '所属院系ID',
    degree_type VARCHAR(20) DEFAULT NULL COMMENT '学位类型',
    duration INT NOT NULL DEFAULT 3 COMMENT '学制(年)',
    status TINYINT NOT NULL DEFAULT 1,
    created_by BIGINT DEFAULT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT DEFAULT NULL,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    UNIQUE KEY uk_major_code (major_code),
    KEY idx_dept_id (dept_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='专业表';

-- 2.3 班级表
CREATE TABLE sm_class (
    id BIGINT NOT NULL AUTO_INCREMENT,
    class_code VARCHAR(30) NOT NULL COMMENT '班级编码',
    class_name VARCHAR(100) NOT NULL COMMENT '班级名称',
    major_id BIGINT NOT NULL COMMENT '专业ID',
    dept_id BIGINT NOT NULL COMMENT '院系ID',
    counselor_id BIGINT DEFAULT NULL COMMENT '辅导员ID',
    grade INT NOT NULL COMMENT '年级',
    student_count INT NOT NULL DEFAULT 0 COMMENT '学生人数',
    status TINYINT NOT NULL DEFAULT 1,
    created_by BIGINT DEFAULT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT DEFAULT NULL,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    UNIQUE KEY uk_class_code (class_code),
    KEY idx_major_id (major_id),
    KEY idx_counselor_id (counselor_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='班级表';

-- 2.4 学生信息表
CREATE TABLE sm_student_info (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    student_no VARCHAR(30) NOT NULL COMMENT '学号',
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    gender TINYINT NOT NULL COMMENT '性别: 0-女 1-男',
    birth_date DATE DEFAULT NULL COMMENT '出生日期',
    id_card VARCHAR(18) DEFAULT NULL COMMENT '身份证号(加密存储)',
    nation VARCHAR(20) DEFAULT NULL COMMENT '民族',
    political_status VARCHAR(20) DEFAULT NULL COMMENT '政治面貌',
    native_place VARCHAR(100) DEFAULT NULL COMMENT '籍贯',
    address VARCHAR(255) DEFAULT NULL COMMENT '家庭地址',
    phone VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
    email VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    dept_id BIGINT NOT NULL COMMENT '院系ID',
    major_id BIGINT NOT NULL COMMENT '专业ID',
    class_id BIGINT NOT NULL COMMENT '班级ID',
    grade INT NOT NULL COMMENT '年级',
    enrollment_date DATE NOT NULL COMMENT '入学日期',
    expected_graduation DATE DEFAULT NULL COMMENT '预计毕业日期',
    education_level VARCHAR(20) DEFAULT NULL COMMENT '培养层次',
    study_type VARCHAR(20) DEFAULT NULL COMMENT '学习形式',
    status ENUM('ENROLLED','SUSPENDED','WITHDRAWN','TRANSFERRED','GRADUATED') NOT NULL DEFAULT 'ENROLLED' COMMENT '学籍状态',
    photo_url VARCHAR(255) DEFAULT NULL COMMENT '照片URL',
    created_by BIGINT DEFAULT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT DEFAULT NULL,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    UNIQUE KEY uk_student_no (student_no),
    UNIQUE KEY uk_user_id (user_id),
    KEY idx_class_id (class_id),
    KEY idx_major_id (major_id),
    KEY idx_dept_id (dept_id),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生信息表';

-- 2.5 学籍异动表
CREATE TABLE sm_student_change (
    id BIGINT NOT NULL AUTO_INCREMENT,
    student_id BIGINT NOT NULL COMMENT '学生ID',
    student_no VARCHAR(30) NOT NULL COMMENT '学号',
    change_type ENUM('SUSPEND','RESUME','WITHDRAW','TRANSFER_MAJOR','TRANSFER_CLASS') NOT NULL COMMENT '异动类型',
    reason VARCHAR(500) NOT NULL COMMENT '异动原因',
    original_major_id BIGINT DEFAULT NULL COMMENT '原专业ID',
    target_major_id BIGINT DEFAULT NULL COMMENT '目标专业ID',
    original_class_id BIGINT DEFAULT NULL COMMENT '原班级ID',
    target_class_id BIGINT DEFAULT NULL COMMENT '目标班级ID',
    effective_date DATE NOT NULL COMMENT '生效日期',
    end_date DATE DEFAULT NULL COMMENT '结束日期(休学)',
    attachments TEXT DEFAULT NULL COMMENT '附件JSON',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态: 0-待审核 1-已通过 2-已驳回',
    auditor_id BIGINT DEFAULT NULL COMMENT '审核人ID',
    audit_time DATETIME DEFAULT NULL COMMENT '审核时间',
    audit_remark VARCHAR(500) DEFAULT NULL COMMENT '审核备注',
    created_by BIGINT DEFAULT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT DEFAULT NULL,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    KEY idx_student_id (student_id),
    KEY idx_change_type (change_type),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学籍异动表';

-- 2.6 在读证明表
CREATE TABLE sm_enrollment_proof (
    id BIGINT NOT NULL AUTO_INCREMENT,
    student_id BIGINT NOT NULL COMMENT '学生ID',
    proof_type VARCHAR(50) NOT NULL COMMENT '证明类型',
    apply_reason VARCHAR(255) NOT NULL COMMENT '申请用途',
    copies INT NOT NULL DEFAULT 1 COMMENT '份数',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态: 0-待办理 1-已办理 2-已领取',
    handler_id BIGINT DEFAULT NULL COMMENT '办理人ID',
    handle_time DATETIME DEFAULT NULL COMMENT '办理时间',
    created_by BIGINT DEFAULT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT DEFAULT NULL,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    KEY idx_student_id (student_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='在读证明表';

-- ============================================================
-- 三、成绩管理模块
-- ============================================================

-- 3.1 课程表
CREATE TABLE sm_course (
    id BIGINT NOT NULL AUTO_INCREMENT,
    course_code VARCHAR(30) NOT NULL COMMENT '课程编码',
    course_name VARCHAR(100) NOT NULL COMMENT '课程名称',
    course_type ENUM('PUBLIC','PROFESSIONAL','ELECTIVE','PRACTICE') NOT NULL COMMENT '课程类型',
    credit DECIMAL(4,1) NOT NULL COMMENT '学分',
    hours INT NOT NULL COMMENT '总学时',
    theory_hours INT DEFAULT 0 COMMENT '理论学时',
    practice_hours INT DEFAULT 0 COMMENT '实践学时',
    dept_id BIGINT NOT NULL COMMENT '开课院系ID',
    description TEXT DEFAULT NULL COMMENT '课程描述',
    status TINYINT NOT NULL DEFAULT 1,
    created_by BIGINT DEFAULT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT DEFAULT NULL,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    UNIQUE KEY uk_course_code (course_code),
    KEY idx_dept_id (dept_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程表';

-- 3.2 开课表(教学任务)
CREATE TABLE sm_course_offering (
    id BIGINT NOT NULL AUTO_INCREMENT,
    course_id BIGINT NOT NULL COMMENT '课程ID',
    teacher_id BIGINT NOT NULL COMMENT '授课教师ID',
    class_id BIGINT NOT NULL COMMENT '班级ID',
    semester VARCHAR(20) NOT NULL COMMENT '学期(如2024-2025-1)',
    weekly_hours INT NOT NULL COMMENT '周学时',
    student_count INT NOT NULL DEFAULT 0 COMMENT '学生人数',
    status TINYINT NOT NULL DEFAULT 1,
    created_by BIGINT DEFAULT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT DEFAULT NULL,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    KEY idx_course_id (course_id),
    KEY idx_teacher_id (teacher_id),
    KEY idx_class_id (class_id),
    KEY idx_semester (semester)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='开课表';

-- 3.3 成绩表
CREATE TABLE sm_score (
    id BIGINT NOT NULL AUTO_INCREMENT,
    student_id BIGINT NOT NULL COMMENT '学生ID',
    student_no VARCHAR(30) NOT NULL COMMENT '学号',
    course_id BIGINT NOT NULL COMMENT '课程ID',
    offering_id BIGINT NOT NULL COMMENT '开课ID',
    semester VARCHAR(20) NOT NULL COMMENT '学期',
    usual_score DECIMAL(5,2) DEFAULT NULL COMMENT '平时成绩(30%)',
    midterm_score DECIMAL(5,2) DEFAULT NULL COMMENT '期中成绩(20%)',
    final_score DECIMAL(5,2) DEFAULT NULL COMMENT '期末成绩(50%)',
    total_score DECIMAL(5,2) DEFAULT NULL COMMENT '总评成绩',
    point DECIMAL(3,2) DEFAULT NULL COMMENT '绩点',
    grade_level VARCHAR(10) DEFAULT NULL COMMENT '等级(A/B/C/D/F)',
    is_pass TINYINT DEFAULT NULL COMMENT '是否及格: 0-否 1-是',
    is_locked TINYINT NOT NULL DEFAULT 0 COMMENT '是否锁定: 0-否 1-是',
    lock_time DATETIME DEFAULT NULL COMMENT '锁定时间',
    remark VARCHAR(255) DEFAULT NULL COMMENT '备注',
    created_by BIGINT DEFAULT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT DEFAULT NULL,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    UNIQUE KEY uk_student_course_semester (student_id, course_id, semester),
    KEY idx_student_no (student_no),
    KEY idx_course_id (course_id),
    KEY idx_offering_id (offering_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='成绩表';

-- 3.4 成绩修改审批表
CREATE TABLE sm_score_modify (
    id BIGINT NOT NULL AUTO_INCREMENT,
    score_id BIGINT NOT NULL COMMENT '成绩ID',
    student_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    field_name VARCHAR(30) NOT NULL COMMENT '修改字段',
    old_value DECIMAL(5,2) DEFAULT NULL COMMENT '原值',
    new_value DECIMAL(5,2) DEFAULT NULL COMMENT '新值',
    reason VARCHAR(500) NOT NULL COMMENT '修改原因',
    attachments TEXT DEFAULT NULL COMMENT '证明材料',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态: 0-待审核 1-已通过 2-已驳回',
    auditor_id BIGINT DEFAULT NULL COMMENT '审核人ID',
    audit_time DATETIME DEFAULT NULL,
    audit_remark VARCHAR(500) DEFAULT NULL,
    created_by BIGINT DEFAULT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT DEFAULT NULL,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    KEY idx_score_id (score_id),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='成绩修改审批表';

-- 3.5 成绩复核申请表
CREATE TABLE sm_score_review (
    id BIGINT NOT NULL AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    semester VARCHAR(20) NOT NULL,
    review_type ENUM('ENTRY','CALCULATION') NOT NULL COMMENT '复核类型: 录入/统分',
    reason VARCHAR(500) NOT NULL COMMENT '复核理由',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态: 0-待处理 1-复核中 2-已复核',
    result VARCHAR(500) DEFAULT NULL COMMENT '复核结果',
    handler_id BIGINT DEFAULT NULL,
    handle_time DATETIME DEFAULT NULL,
    created_by BIGINT DEFAULT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT DEFAULT NULL,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    KEY idx_student_id (student_id),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='成绩复核申请表';

-- ============================================================
-- 四、课表管理模块
-- ============================================================

-- 4.1 教室表
CREATE TABLE sm_classroom (
    id BIGINT NOT NULL AUTO_INCREMENT,
    room_code VARCHAR(30) NOT NULL COMMENT '教室编号',
    room_name VARCHAR(100) NOT NULL COMMENT '教室名称',
    building VARCHAR(50) NOT NULL COMMENT '所在楼栋',
    floor INT DEFAULT NULL COMMENT '楼层',
    room_type ENUM('MULTIMEDIA','ORDINARY','LAB','COMPUTER','MULTIFUNCTIONAL') NOT NULL COMMENT '教室类型',
    capacity INT NOT NULL COMMENT '容量',
    dept_id BIGINT DEFAULT NULL COMMENT '所属院系',
    status TINYINT NOT NULL DEFAULT 1,
    created_by BIGINT DEFAULT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT DEFAULT NULL,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    UNIQUE KEY uk_room_code (room_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教室表';

-- 4.2 排课表
CREATE TABLE sm_schedule (
    id BIGINT NOT NULL AUTO_INCREMENT,
    offering_id BIGINT NOT NULL COMMENT '开课ID',
    course_id BIGINT NOT NULL COMMENT '课程ID',
    teacher_id BIGINT NOT NULL COMMENT '教师ID',
    class_id BIGINT NOT NULL COMMENT '班级ID',
    classroom_id BIGINT NOT NULL COMMENT '教室ID',
    semester VARCHAR(20) NOT NULL COMMENT '学期',
    week_day TINYINT NOT NULL COMMENT '星期(1-7)',
    start_section TINYINT NOT NULL COMMENT '开始节次',
    end_section TINYINT NOT NULL COMMENT '结束节次',
    start_week INT NOT NULL COMMENT '开始周',
    end_week INT NOT NULL COMMENT '结束周',
    week_type ENUM('ALL','ODD','EVEN') DEFAULT 'ALL' COMMENT '周类型: 全/单/双',
    status TINYINT NOT NULL DEFAULT 1,
    created_by BIGINT DEFAULT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT DEFAULT NULL,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    KEY idx_class_id (class_id),
    KEY idx_teacher_id (teacher_id),
    KEY idx_classroom_id (classroom_id),
    KEY idx_semester (semester)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='排课表';

-- 4.3 调停课记录表
CREATE TABLE sm_schedule_change (
    id BIGINT NOT NULL AUTO_INCREMENT,
    schedule_id BIGINT NOT NULL COMMENT '原排课ID',
    change_type ENUM('POSTPONE','CANCEL','ADJUST') NOT NULL COMMENT '类型: 调课/停课/补课',
    original_time VARCHAR(100) DEFAULT NULL COMMENT '原时间',
    new_time VARCHAR(100) DEFAULT NULL COMMENT '新时间',
    original_classroom_id BIGINT DEFAULT NULL,
    new_classroom_id BIGINT DEFAULT NULL,
    reason VARCHAR(255) NOT NULL COMMENT '原因',
    notify_status TINYINT NOT NULL DEFAULT 0 COMMENT '通知状态',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '审批状态',
    created_by BIGINT DEFAULT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT DEFAULT NULL,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    KEY idx_schedule_id (schedule_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='调停课记录表';

-- ============================================================
-- 五、考勤管理模块
-- ============================================================

-- 5.1 请假表
CREATE TABLE sm_leave (
    id BIGINT NOT NULL AUTO_INCREMENT,
    student_id BIGINT NOT NULL COMMENT '学生ID',
    student_no VARCHAR(30) NOT NULL,
    leave_type ENUM('PERSONAL','SICK','PUBLIC') NOT NULL COMMENT '请假类型: 事假/病假/公假',
    start_time DATETIME NOT NULL COMMENT '开始时间',
    end_time DATETIME NOT NULL COMMENT '结束时间',
    days DECIMAL(4,1) NOT NULL COMMENT '天数',
    reason VARCHAR(500) NOT NULL COMMENT '请假事由',
    attachments TEXT DEFAULT NULL COMMENT '附件(医院证明等)',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态: 0-待审批 1-已通过 2-已驳回 3-已撤销',
    current_level TINYINT NOT NULL DEFAULT 1 COMMENT '当前审批层级: 1-辅导员 2-院系 3-学工部',
    counselor_id BIGINT DEFAULT NULL COMMENT '辅导员ID',
    counselor_audit_time DATETIME DEFAULT NULL,
    counselor_remark VARCHAR(255) DEFAULT NULL,
    dept_admin_id BIGINT DEFAULT NULL COMMENT '院系管理员ID',
    dept_audit_time DATETIME DEFAULT NULL,
    dept_remark VARCHAR(255) DEFAULT NULL,
    student_admin_id BIGINT DEFAULT NULL COMMENT '学工部管理员ID',
    student_audit_time DATETIME DEFAULT NULL,
    student_remark VARCHAR(255) DEFAULT NULL,
    created_by BIGINT DEFAULT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT DEFAULT NULL,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    KEY idx_student_id (student_id),
    KEY idx_status (status),
    KEY idx_counselor_id (counselor_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='请假表';

-- 5.2 考勤记录表
CREATE TABLE sm_attendance (
    id BIGINT NOT NULL AUTO_INCREMENT,
    offering_id BIGINT NOT NULL COMMENT '开课ID',
    student_id BIGINT NOT NULL COMMENT '学生ID',
    attendance_date DATE NOT NULL COMMENT '考勤日期',
    section TINYINT DEFAULT NULL COMMENT '节次',
    status ENUM('PRESENT','ABSENT','LATE','LEAVE','EARLY') NOT NULL COMMENT '状态: 出勤/旷课/迟到/请假/早退',
    remark VARCHAR(255) DEFAULT NULL,
    created_by BIGINT DEFAULT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT DEFAULT NULL,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    UNIQUE KEY uk_offering_student_date (offering_id, student_id, attendance_date),
    KEY idx_student_id (student_id),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='考勤记录表';

-- 5.3 考勤预警表
CREATE TABLE sm_attendance_warning (
    id BIGINT NOT NULL AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    warning_type ENUM('ATTENDANCE_RATE','ABSENT_HOURS') NOT NULL COMMENT '预警类型',
    threshold DECIMAL(5,2) NOT NULL COMMENT '预警阈值',
    actual_value DECIMAL(5,2) NOT NULL COMMENT '实际值',
    semester VARCHAR(20) NOT NULL,
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态: 0-未处理 1-已通知 2-已处理',
    notify_time DATETIME DEFAULT NULL,
    created_by BIGINT DEFAULT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT DEFAULT NULL,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    KEY idx_student_id (student_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='考勤预警表';

-- ============================================================
-- 六、奖助学金模块
-- ============================================================

-- 6.1 奖学金申请表
CREATE TABLE sm_scholarship_apply (
    id BIGINT NOT NULL AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    student_no VARCHAR(30) NOT NULL,
    scholarship_type ENUM('NATIONAL','MOTIVATIONAL','COLLEGE_FIRST','COLLEGE_SECOND','COLLEGE_THIRD','SINGLE') NOT NULL COMMENT '奖学金类型',
    academic_year VARCHAR(20) NOT NULL COMMENT '学年',
    gpa DECIMAL(4,2) DEFAULT NULL COMMENT '平均学分绩点',
    ranking INT DEFAULT NULL COMMENT '专业排名',
    total_students INT DEFAULT NULL COMMENT '专业总人数',
    reason VARCHAR(500) DEFAULT NULL COMMENT '申请理由',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态: 0-待评议 1-班级通过 2-院系公示 3-学院通过 4-全校公示 5-已驳回',
    class_review_time DATETIME DEFAULT NULL,
    dept_review_time DATETIME DEFAULT NULL,
    dept_publicity_start DATETIME DEFAULT NULL,
    college_review_time DATETIME DEFAULT NULL,
    college_publicity_start DATETIME DEFAULT NULL,
    amount DECIMAL(10,2) DEFAULT NULL COMMENT '金额',
    created_by BIGINT DEFAULT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT DEFAULT NULL,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    KEY idx_student_id (student_id),
    KEY idx_status (status),
    KEY idx_academic_year (academic_year)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='奖学金申请表';

-- 6.2 助学金申请表
CREATE TABLE sm_grant_apply (
    id BIGINT NOT NULL AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    student_no VARCHAR(30) NOT NULL,
    grant_type ENUM('NATIONAL_FIRST','NATIONAL_SECOND','NATIONAL_THIRD','HARDSHIP','WORK_STUDY') NOT NULL COMMENT '助学金类型',
    academic_year VARCHAR(20) NOT NULL,
    family_income DECIMAL(10,2) DEFAULT NULL COMMENT '家庭年收入',
    family_members INT DEFAULT NULL COMMENT '家庭人口',
    difficulty_level ENUM('EXTREME','GENERAL','OTHER') DEFAULT NULL COMMENT '困难等级',
    reason VARCHAR(500) DEFAULT NULL,
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态',
    work_hours DECIMAL(5,1) DEFAULT NULL COMMENT '勤工助学工时',
    hourly_rate DECIMAL(6,2) DEFAULT 15.00 COMMENT '小时工资',
    amount DECIMAL(10,2) DEFAULT NULL COMMENT '金额',
    created_by BIGINT DEFAULT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT DEFAULT NULL,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    KEY idx_student_id (student_id),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='助学金申请表';

-- ============================================================
-- 七、宿舍管理模块
-- ============================================================

-- 7.1 宿舍楼表
CREATE TABLE sm_dorm_building (
    id BIGINT NOT NULL AUTO_INCREMENT,
    building_code VARCHAR(20) NOT NULL COMMENT '楼栋编号',
    building_name VARCHAR(50) NOT NULL COMMENT '楼栋名称',
    total_floors INT NOT NULL COMMENT '总楼层',
    total_rooms INT NOT NULL COMMENT '总房间数',
    dept_id BIGINT DEFAULT NULL COMMENT '所属院系',
    admin_name VARCHAR(50) DEFAULT NULL COMMENT '楼管员',
    admin_phone VARCHAR(20) DEFAULT NULL,
    status TINYINT NOT NULL DEFAULT 1,
    created_by BIGINT DEFAULT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT DEFAULT NULL,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    UNIQUE KEY uk_building_code (building_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='宿舍楼表';

-- 7.2 宿舍房间表
CREATE TABLE sm_dorm_room (
    id BIGINT NOT NULL AUTO_INCREMENT,
    room_code VARCHAR(20) NOT NULL COMMENT '房间编号',
    building_id BIGINT NOT NULL COMMENT '楼栋ID',
    floor INT NOT NULL COMMENT '楼层',
    room_type ENUM('FOUR','SIX','EIGHT') NOT NULL COMMENT '房间类型',
    capacity INT NOT NULL COMMENT '床位数',
    occupied INT NOT NULL DEFAULT 0 COMMENT '已住人数',
    facilities TEXT DEFAULT NULL COMMENT '设施JSON',
    status TINYINT NOT NULL DEFAULT 1,
    created_by BIGINT DEFAULT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT DEFAULT NULL,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    UNIQUE KEY uk_room_code (room_code),
    KEY idx_building_id (building_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='宿舍房间表';

-- 7.3 学生住宿表
CREATE TABLE sm_dorm_student (
    id BIGINT NOT NULL AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    student_no VARCHAR(30) NOT NULL,
    room_id BIGINT NOT NULL,
    bed_no VARCHAR(10) DEFAULT NULL COMMENT '床位号',
    check_in_date DATE NOT NULL COMMENT '入住日期',
    check_out_date DATE DEFAULT NULL COMMENT '退宿日期',
    status ENUM('LIVING','TRANSFERRED','CHECKED_OUT') NOT NULL DEFAULT 'LIVING',
    created_by BIGINT DEFAULT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT DEFAULT NULL,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    KEY idx_student_id (student_id),
    KEY idx_room_id (room_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生住宿表';

-- 7.4 报修表
CREATE TABLE sm_repair (
    id BIGINT NOT NULL AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    student_no VARCHAR(30) NOT NULL,
    room_id BIGINT NOT NULL,
    repair_type ENUM('ELECTRIC','WATER','DOOR','WINDOW','AIR_CONDITION','OTHER') NOT NULL COMMENT '报修类型',
    description VARCHAR(500) NOT NULL COMMENT '故障描述',
    images TEXT DEFAULT NULL COMMENT '图片JSON',
    is_urgent TINYINT NOT NULL DEFAULT 0 COMMENT '是否紧急',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态: 0-待派单 1-已派单 2-维修中 3-已完成 4-已评价',
    worker_id BIGINT DEFAULT NULL COMMENT '维修工ID',
    worker_name VARCHAR(50) DEFAULT NULL,
    worker_phone VARCHAR(20) DEFAULT NULL,
    dispatch_time DATETIME DEFAULT NULL COMMENT '派单时间',
    repair_time DATETIME DEFAULT NULL COMMENT '维修时间',
    finish_time DATETIME DEFAULT NULL COMMENT '完成时间',
    rating TINYINT DEFAULT NULL COMMENT '评价(1-5)',
    rating_content VARCHAR(255) DEFAULT NULL,
    rating_time DATETIME DEFAULT NULL,
    created_by BIGINT DEFAULT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT DEFAULT NULL,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    KEY idx_student_id (student_id),
    KEY idx_status (status),
    KEY idx_room_id (room_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报修表';

-- 7.5 晚归记录表
CREATE TABLE sm_late_return (
    id BIGINT NOT NULL AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    student_no VARCHAR(30) NOT NULL,
    room_id BIGINT NOT NULL,
    return_time DATETIME NOT NULL COMMENT '归寝时间',
    is_weekend TINYINT NOT NULL DEFAULT 0 COMMENT '是否周末',
    remark VARCHAR(255) DEFAULT NULL,
    created_by BIGINT DEFAULT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT DEFAULT NULL,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    KEY idx_student_id (student_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='晚归记录表';

-- ============================================================
-- 八、评优评先模块
-- ============================================================

-- 8.1 评优申请表
CREATE TABLE sm_honor_apply (
    id BIGINT NOT NULL AUTO_INCREMENT,
    applicant_type ENUM('STUDENT','CLASS') NOT NULL COMMENT '申请类型: 个人/班级',
    student_id BIGINT DEFAULT NULL COMMENT '学生ID(个人申请)',
    class_id BIGINT DEFAULT NULL COMMENT '班级ID(班级申请)',
    honor_type ENUM('THREE_GOOD','EXCELLENT_STUDENT','EXCELLENT_CADRE','ADVANCED_CLASS','OTHER') NOT NULL COMMENT '荣誉类型',
    academic_year VARCHAR(20) NOT NULL,
    reason VARCHAR(500) DEFAULT NULL COMMENT '申请理由',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态: 0-待推荐 1-班级推荐 2-院系初审 3-学工复审 4-公示 5-已驳回',
    class_recommend_time DATETIME DEFAULT NULL,
    dept_review_time DATETIME DEFAULT NULL,
    student_review_time DATETIME DEFAULT NULL,
    publicity_start DATETIME DEFAULT NULL,
    created_by BIGINT DEFAULT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT DEFAULT NULL,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    KEY idx_student_id (student_id),
    KEY idx_class_id (class_id),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评优申请表';

-- ============================================================
-- 九、第二课堂模块
-- ============================================================

-- 9.1 社团表
CREATE TABLE sm_club (
    id BIGINT NOT NULL AUTO_INCREMENT,
    club_code VARCHAR(20) NOT NULL COMMENT '社团编号',
    club_name VARCHAR(100) NOT NULL COMMENT '社团名称',
    club_type ENUM('ACADEMIC','ART','SPORTS','VOLUNTEER','INNOVATION','OTHER') NOT NULL COMMENT '社团类型',
    description TEXT DEFAULT NULL COMMENT '社团简介',
    advisor_id BIGINT DEFAULT NULL COMMENT '指导教师ID',
    president_id BIGINT DEFAULT NULL COMMENT '社长ID',
    member_count INT NOT NULL DEFAULT 0 COMMENT '成员数',
    status TINYINT NOT NULL DEFAULT 1,
    created_by BIGINT DEFAULT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT DEFAULT NULL,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    UNIQUE KEY uk_club_code (club_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='社团表';

-- 9.2 社团成员表
CREATE TABLE sm_club_member (
    id BIGINT NOT NULL AUTO_INCREMENT,
    club_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    role ENUM('PRESIDENT','VICE_PRESIDENT','MEMBER') NOT NULL DEFAULT 'MEMBER',
    join_date DATE NOT NULL,
    status TINYINT NOT NULL DEFAULT 1,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_club_student (club_id, student_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='社团成员表';

-- 9.3 活动表
CREATE TABLE sm_activity (
    id BIGINT NOT NULL AUTO_INCREMENT,
    activity_name VARCHAR(200) NOT NULL COMMENT '活动名称',
    activity_type ENUM('VOLUNTEER','PRACTICE','LECTURE','COMPETITION','OTHER') NOT NULL COMMENT '活动类型',
    club_id BIGINT DEFAULT NULL COMMENT '主办社团ID',
    organizer VARCHAR(100) DEFAULT NULL COMMENT '主办单位',
    start_time DATETIME NOT NULL COMMENT '开始时间',
    end_time DATETIME NOT NULL COMMENT '结束时间',
    location VARCHAR(200) DEFAULT NULL COMMENT '活动地点',
    description TEXT DEFAULT NULL COMMENT '活动描述',
    max_participants INT DEFAULT NULL COMMENT '最大参与人数',
    credit DECIMAL(4,2) DEFAULT 0 COMMENT '学分',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态: 0-待审核 1-报名中 2-进行中 3-已结束',
    created_by BIGINT DEFAULT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT DEFAULT NULL,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    KEY idx_club_id (club_id),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动表';

-- 9.4 活动参与表
CREATE TABLE sm_activity_participant (
    id BIGINT NOT NULL AUTO_INCREMENT,
    activity_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 0-取消 1-参与',
    credit DECIMAL(4,2) DEFAULT 0 COMMENT '获得学分',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_activity_student (activity_id, student_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动参与表';

-- 9.5 学分认定表
CREATE TABLE sm_credit_record (
    id BIGINT NOT NULL AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    source_type ENUM('CLUB','ACTIVITY','COMPETITION','CERTIFICATE','OTHER') NOT NULL COMMENT '来源类型',
    source_id BIGINT DEFAULT NULL COMMENT '来源ID',
    source_name VARCHAR(200) NOT NULL COMMENT '来源名称',
    credit DECIMAL(4,2) NOT NULL COMMENT '学分',
    academic_year VARCHAR(20) NOT NULL,
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态: 0-待认定 1-已认定 2-已驳回',
    auditor_id BIGINT DEFAULT NULL,
    audit_time DATETIME DEFAULT NULL,
    created_by BIGINT DEFAULT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT DEFAULT NULL,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    KEY idx_student_id (student_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学分认定表';

-- ============================================================
-- 十、毕业管理模块
-- ============================================================

-- 10.1 毕业审核表
CREATE TABLE sm_graduation_audit (
    id BIGINT NOT NULL AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    student_no VARCHAR(30) NOT NULL,
    academic_year VARCHAR(20) NOT NULL,
    -- 学业审核
    required_courses_pass TINYINT DEFAULT 0 COMMENT '必修课通过',
    total_credits DECIMAL(6,1) DEFAULT 0 COMMENT '总学分',
    required_credits DECIMAL(6,1) DEFAULT 0 COMMENT '要求学分',
    gpa DECIMAL(4,2) DEFAULT 0 COMMENT '平均学分绩点',
    thesis_pass TINYINT DEFAULT 0 COMMENT '论文通过',
    practice_pass TINYINT DEFAULT 0 COMMENT '实习通过',
    -- 素质拓展
    quality_credits DECIMAL(4,2) DEFAULT 0 COMMENT '素质拓展学分',
    -- 综合审核
    fees_cleared TINYINT DEFAULT 0 COMMENT '费用结清',
    library_cleared TINYINT DEFAULT 0 COMMENT '图书清退',
    dorm_cleared TINYINT DEFAULT 0 COMMENT '退宿完成',
    no_disciplinary TINYINT DEFAULT 0 COMMENT '无处分',
    -- 审核结果
    audit_result TINYINT NOT NULL DEFAULT 0 COMMENT '审核结果: 0-未通过 1-通过',
    audit_remark VARCHAR(500) DEFAULT NULL,
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态: 0-系统筛查 1-院系核对 2-教务处终审 3-公示 4-已发证',
    publicity_start DATETIME DEFAULT NULL,
    certificate_no VARCHAR(50) DEFAULT NULL COMMENT '证书编号',
    created_by BIGINT DEFAULT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT DEFAULT NULL,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    UNIQUE KEY uk_student_year (student_id, academic_year),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='毕业审核表';

-- 10.2 毕业预警表
CREATE TABLE sm_graduation_warning (
    id BIGINT NOT NULL AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    warning_type ENUM('CREDIT','GPA','COURSE','QUALITY','FEE','BOOK','DORM','DISCIPLINARY') NOT NULL,
    warning_content VARCHAR(500) NOT NULL,
    semester VARCHAR(20) NOT NULL,
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态: 0-未处理 1-已通知 2-已处理',
    created_by BIGINT DEFAULT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT DEFAULT NULL,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    KEY idx_student_id (student_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='毕业预警表';

-- ============================================================
-- 十一、就业服务模块
-- ============================================================

-- 11.1 招聘信息表
CREATE TABLE sm_job_posting (
    id BIGINT NOT NULL AUTO_INCREMENT,
    company_name VARCHAR(200) NOT NULL COMMENT '公司名称',
    company_type VARCHAR(50) DEFAULT NULL COMMENT '企业类型',
    company_scale VARCHAR(50) DEFAULT NULL COMMENT '企业规模',
    position_name VARCHAR(200) NOT NULL COMMENT '职位名称',
    position_type VARCHAR(50) DEFAULT NULL COMMENT '职位类别',
    salary_range VARCHAR(50) DEFAULT NULL COMMENT '薪资范围',
    work_location VARCHAR(200) DEFAULT NULL COMMENT '工作地点',
    description TEXT DEFAULT NULL COMMENT '职位描述',
    requirements TEXT DEFAULT NULL COMMENT '任职要求',
    contact_person VARCHAR(50) DEFAULT NULL COMMENT '联系人',
    contact_phone VARCHAR(20) DEFAULT NULL,
    contact_email VARCHAR(100) DEFAULT NULL,
    major_requirement VARCHAR(200) DEFAULT NULL COMMENT '专业要求',
    education_requirement VARCHAR(50) DEFAULT NULL COMMENT '学历要求',
    publish_date DATE NOT NULL COMMENT '发布日期',
    deadline DATE DEFAULT NULL COMMENT '截止日期',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 0-下架 1-招聘中 2-已结束',
    created_by BIGINT DEFAULT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT DEFAULT NULL,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    KEY idx_status (status),
    KEY idx_publish_date (publish_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='招聘信息表';

-- 11.2 就业推荐表
CREATE TABLE sm_employment_recommend (
    id BIGINT NOT NULL AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    job_id BIGINT NOT NULL,
    recommend_teacher_id BIGINT DEFAULT NULL COMMENT '推荐教师ID',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态: 0-待审核 1-已推荐 2-已录用 3-已驳回',
    remark VARCHAR(255) DEFAULT NULL,
    created_by BIGINT DEFAULT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT DEFAULT NULL,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    KEY idx_student_id (student_id),
    KEY idx_job_id (job_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='就业推荐表';

-- 11.3 就业信息表
CREATE TABLE sm_employment_info (
    id BIGINT NOT NULL AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    employment_type ENUM('SIGNED','FLEXIBLE','FURTHER_STUDY','ENTREPRENEURSHIP','UNEMPLOYED') NOT NULL COMMENT '就业类型',
    company_name VARCHAR(200) DEFAULT NULL,
    position VARCHAR(100) DEFAULT NULL,
    salary DECIMAL(10,2) DEFAULT NULL,
    work_location VARCHAR(200) DEFAULT NULL,
    contract_type VARCHAR(50) DEFAULT NULL COMMENT '合同类型',
    social_security TINYINT DEFAULT 0 COMMENT '五险一金',
    report_card_no VARCHAR(50) DEFAULT NULL COMMENT '报到证编号',
    status TINYINT NOT NULL DEFAULT 1,
    created_by BIGINT DEFAULT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT DEFAULT NULL,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    UNIQUE KEY uk_student_id (student_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='就业信息表';

-- 11.4 档案去向表
CREATE TABLE sm_archive_destination (
    id BIGINT NOT NULL AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    destination_type ENUM('COMPANY','TALENT_CENTER','HOMETOWN','FURTHER_STUDY','UNDETERMINED') NOT NULL COMMENT '去向类型',
    destination_address VARCHAR(300) DEFAULT NULL COMMENT '转递地址',
    recipient VARCHAR(50) DEFAULT NULL COMMENT '接收人',
    recipient_phone VARCHAR(20) DEFAULT NULL,
    tracking_no VARCHAR(50) DEFAULT NULL COMMENT '快递单号',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态: 0-待办理 1-已办理',
    created_by BIGINT DEFAULT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT DEFAULT NULL,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    UNIQUE KEY uk_student_id (student_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='档案去向表';

-- ============================================================
-- 十二、通知公告模块
-- ============================================================

-- 12.1 通知公告表
CREATE TABLE sm_notice (
    id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL COMMENT '标题',
    content LONGTEXT NOT NULL COMMENT '内容',
    notice_type ENUM('SCHOOL','DEPT','CLASS','DIRECTED') NOT NULL COMMENT '通知类型',
    level ENUM('NORMAL','URGENT','EMERGENCY') NOT NULL DEFAULT 'NORMAL' COMMENT '级别',
    publisher_id BIGINT NOT NULL COMMENT '发布人ID',
    publisher_name VARCHAR(50) DEFAULT NULL,
    dept_id BIGINT DEFAULT NULL COMMENT '发布院系',
    class_id BIGINT DEFAULT NULL COMMENT '发布班级',
    target_users TEXT DEFAULT NULL COMMENT '定向用户JSON',
    attachments TEXT DEFAULT NULL COMMENT '附件JSON',
    require_receipt TINYINT NOT NULL DEFAULT 0 COMMENT '是否需要回执',
    publish_time DATETIME DEFAULT NULL COMMENT '发布时间',
    expire_time DATETIME DEFAULT NULL COMMENT '过期时间',
    scheduled_time DATETIME DEFAULT NULL COMMENT '定时发布时间',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态: 0-草稿 1-已发布 2-已撤回',
    view_count INT NOT NULL DEFAULT 0 COMMENT '浏览次数',
    created_by BIGINT DEFAULT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT DEFAULT NULL,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    KEY idx_notice_type (notice_type),
    KEY idx_status (status),
    KEY idx_publish_time (publish_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通知公告表';

-- 12.2 通知回执表
CREATE TABLE sm_notice_receipt (
    id BIGINT NOT NULL AUTO_INCREMENT,
    notice_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    read_time DATETIME DEFAULT NULL COMMENT '阅读时间',
    receipt_time DATETIME DEFAULT NULL COMMENT '回执时间',
    receipt_content VARCHAR(500) DEFAULT NULL COMMENT '回执内容',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_notice_user (notice_id, user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通知回执表';

-- ============================================================
-- 十三、系统配置模块
-- ============================================================

-- 13.1 字典数据表
CREATE TABLE sm_dict_data (
    id BIGINT NOT NULL AUTO_INCREMENT,
    dict_type VARCHAR(50) NOT NULL COMMENT '字典类型',
    dict_label VARCHAR(100) NOT NULL COMMENT '字典标签',
    dict_value VARCHAR(100) NOT NULL COMMENT '字典值',
    sort_order INT NOT NULL DEFAULT 0,
    status TINYINT NOT NULL DEFAULT 1,
    remark VARCHAR(255) DEFAULT NULL,
    created_by BIGINT DEFAULT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT DEFAULT NULL,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    KEY idx_dict_type (dict_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典数据表';

-- 13.2 系统配置表
CREATE TABLE sm_system_config (
    id BIGINT NOT NULL AUTO_INCREMENT,
    config_key VARCHAR(100) NOT NULL COMMENT '配置键',
    config_value TEXT NOT NULL COMMENT '配置值',
    description VARCHAR(255) DEFAULT NULL,
    created_by BIGINT DEFAULT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT DEFAULT NULL,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    UNIQUE KEY uk_config_key (config_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统配置表';

-- ============================================================
-- 初始化数据
-- ============================================================

-- 初始化角色
INSERT INTO sm_role (role_code, role_name, description) VALUES
('ROLE_STUDENT', '学生', '在校学生'),
('ROLE_TEACHER', '任课教师', '授课教师'),
('ROLE_COUNSELOR', '辅导员', '班级辅导员'),
('ROLE_DEPT_ADMIN', '院系教务员', '院系教务管理员'),
('ROLE_ACADEMIC_ADMIN', '学工/教务管理员', '学校教务学工管理员'),
('ROLE_SYS_ADMIN', '系统管理员', '系统超级管理员'),
('ROLE_PARENT', '家长', '学生家长(受限)');

-- 初始化字典数据
INSERT INTO sm_dict_data (dict_type, dict_label, dict_value, sort_order) VALUES
('leave_type', '事假', 'PERSONAL', 1),
('leave_type', '病假', 'SICK', 2),
('leave_type', '公假', 'PUBLIC', 3),
('scholarship_type', '国家奖学金', 'NATIONAL', 1),
('scholarship_type', '国家励志奖学金', 'MOTIVATIONAL', 2),
('scholarship_type', '学院一等奖学金', 'COLLEGE_FIRST', 3),
('scholarship_type', '学院二等奖学金', 'COLLEGE_SECOND', 4),
('scholarship_type', '学院三等奖学金', 'COLLEGE_THIRD', 5),
('scholarship_type', '单项奖学金', 'SINGLE', 6),
('grant_type', '国家一等助学金', 'NATIONAL_FIRST', 1),
('grant_type', '国家二等助学金', 'NATIONAL_SECOND', 2),
('grant_type', '国家三等助学金', 'NATIONAL_THIRD', 3),
('grant_type', '困难补助', 'HARDSHIP', 4),
('grant_type', '勤工助学', 'WORK_STUDY', 5),
('honor_type', '三好学生', 'THREE_GOOD', 1),
('honor_type', '优秀学生', 'EXCELLENT_STUDENT', 2),
('honor_type', '优秀学生干部', 'EXCELLENT_CADRE', 3),
('honor_type', '先进班集体', 'ADVANCED_CLASS', 4),
('student_status', '在读', 'ENROLLED', 1),
('student_status', '休学', 'SUSPENDED', 2),
('student_status', '退学', 'WITHDRAWN', 3),
('student_status', '转专业', 'TRANSFERRED', 4),
('student_status', '已毕业', 'GRADUATED', 5);

-- 初始化系统配置
INSERT INTO sm_system_config (config_key, config_value, description) VALUES
('leave.personal.max_days', '30', '学期事假最大天数'),
('leave.exam_week_restrict', 'true', '考试周不批事假'),
('attendance.warning_rate', '85', '出勤率预警阈值(%)'),
('attendance.absent_warning_hours', '10', '旷课预警学时'),
('score.review_days', '5', '成绩复核申请期限(工作日)'),
('dorm.late_return_weekday', '23:00', '工作日晚归时间'),
('dorm.late_return_weekend', '23:30', '周末晚归时间'),
('dorm.late_return_warning_count', '3', '晚归通报次数'),
('scholarship.gpa_top_percent', '30', '奖学金成绩前百分比'),
('graduation.warning_semester', '1', '毕业预警提前学期数'),
('notice.attachment_max_size', '20971520', '通知附件最大大小(20MB)'),
('repair.urgent_response_minutes', '30', '紧急报修响应时间(分钟)'),
('work_study.hourly_rate', '15', '勤工助学小时工资'),
('work_study.max_hours_per_month', '40', '勤工助学月最大工时');

-- 初始化默认管理员账号 (密码: admin123, BCrypt加密)
INSERT INTO sm_user (username, password, real_name, role, status) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '系统管理员', 'SYS_ADMIN', 1);
