package com.snvec.student.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 请假类型枚举
 */
@Getter
public enum LeaveTypeEnum {

    PERSONAL("PERSONAL", "事假"),
    SICK("SICK", "病假"),
    PUBLIC("PUBLIC", "公假");

    @EnumValue
    @JsonValue
    private final String code;
    private final String description;

    LeaveTypeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }
}
