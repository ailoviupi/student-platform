package com.snvec.student.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 通知类型枚举
 */
@Getter
public enum NoticeTypeEnum {

    SCHOOL("SCHOOL", "校级"),
    DEPT("DEPT", "院系"),
    CLASS("CLASS", "班级"),
    DIRECTED("DIRECTED", "定向");

    @EnumValue
    @JsonValue
    private final String code;
    private final String description;

    NoticeTypeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }
}
