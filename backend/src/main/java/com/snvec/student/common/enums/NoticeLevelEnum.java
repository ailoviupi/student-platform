package com.snvec.student.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 通知级别枚举
 */
@Getter
public enum NoticeLevelEnum {

    NORMAL("NORMAL", "普通"),
    URGENT("URGENT", "紧急"),
    EMERGENCY("EMERGENCY", "特急");

    @EnumValue
    @JsonValue
    private final String code;
    private final String description;

    NoticeLevelEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }
}
