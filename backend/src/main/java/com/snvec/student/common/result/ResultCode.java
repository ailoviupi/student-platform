package com.snvec.student.common.result;

public enum ResultCode {

    SUCCESS(200, "操作成功"),
    ERROR(500, "操作失败"),
    PARAM_ERROR(400, "参数错误"),
    UNAUTHORIZED(401, "未登录或登录已过期"),
    FORBIDDEN(403, "无访问权限"),
    NOT_FOUND(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "请求方法不允许"),

    USER_NOT_EXIST(1001, "用户不存在"),
    USER_PASSWORD_ERROR(1002, "密码错误"),
    USER_DISABLED(1003, "账号已被禁用"),
    USER_ALREADY_EXIST(1004, "用户已存在"),
    TOKEN_INVALID(1005, "Token无效"),
    TOKEN_EXPIRED(1006, "Token已过期"),

    STUDENT_NOT_EXIST(2001, "学生不存在"),
    STUDENT_CHANGE_FAILED(2002, "学籍异动失败"),
    TRANSFER_MAJOR_NOT_ALLOWED(2003, "不符合转专业条件"),
    SUSPEND_LIMIT_EXCEEDED(2004, "休学时间超过限制"),

    SCORE_NOT_EXIST(3001, "成绩不存在"),
    SCORE_LOCKED(3002, "成绩已锁定，需审批修改"),
    SCORE_REVIEW_EXPIRED(3003, "成绩复核申请已过期"),
    SCORE_REVIEW_NOT_ALLOWED(3004, "仅可申请录入/统分错误复核"),

    LEAVE_NOT_EXIST(4001, "请假记录不存在"),
    LEAVE_APPROVAL_FAILED(4002, "请假审批失败"),
    LEAVE_DAYS_EXCEEDED(4003, "请假天数超过限制"),
    LEAVE_EXAM_WEEK(4004, "考试周不批事假"),

    SCHOLARSHIP_NOT_EXIST(5001, "奖学金申请不存在"),
    SCHOLARSHIP_NOT_QUALIFIED(5002, "不符合申请条件"),
    SCHOLARSHIP_CONFLICT(5003, "国家奖学金与励志奖学金不可兼得"),

    DORM_NOT_EXIST(6001, "宿舍不存在"),
    DORM_FULL(6002, "宿舍已满"),
    REPAIR_NOT_EXIST(6003, "报修记录不存在"),

    GRADUATION_AUDIT_FAILED(7001, "毕业审核未通过"),
    GRADUATION_NOT_QUALIFIED(7002, "不符合毕业条件"),

    RATE_LIMIT(9001, "请求过于频繁，请稍后再试"),
    SYSTEM_BUSY(9002, "系统繁忙"),
    SYSTEM_ERROR(9003, "系统错误");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() { return code; }
    public String getMessage() { return message; }
}
