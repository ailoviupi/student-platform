package com.snvec.student.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 报修类型枚举
 */
@Getter
public enum RepairTypeEnum {

    ELECTRIC("ELECTRIC", "电路"),
    WATER("WATER", "水路"),
    DOOR("DOOR", "门锁"),
    WINDOW("WINDOW", "门窗"),
    AIR_CONDITION("AIR_CONDITION", "空调"),
    OTHER("OTHER", "其他");

    @EnumValue
    @JsonValue
    private final String code;
    private final String description;

    RepairTypeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }
}
