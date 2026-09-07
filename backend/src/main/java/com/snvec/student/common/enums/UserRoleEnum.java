package com.snvec.student.common.enums;

public enum UserRoleEnum {

    STUDENT("STUDENT", "学生"),
    TEACHER("TEACHER", "任课教师"),
    COUNSELOR("COUNSELOR", "辅导员"),
    DEPT_ADMIN("DEPT_ADMIN", "院系教务员"),
    ACADEMIC_ADMIN("ACADEMIC_ADMIN", "学工/教务管理员"),
    SYS_ADMIN("SYS_ADMIN", "系统管理员"),
    PARENT("PARENT", "家长");

    private final String code;
    private final String description;

    UserRoleEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() { return code; }
    public String getDescription() { return description; }
}
