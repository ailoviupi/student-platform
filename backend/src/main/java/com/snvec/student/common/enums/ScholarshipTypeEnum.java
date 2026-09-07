package com.snvec.student.common.enums;

import java.math.BigDecimal;

public enum ScholarshipTypeEnum {

    NATIONAL("NATIONAL", "国家奖学金", new BigDecimal("8000")),
    MOTIVATIONAL("MOTIVATIONAL", "国家励志奖学金", new BigDecimal("5000")),
    COLLEGE_FIRST("COLLEGE_FIRST", "学院一等奖学金", new BigDecimal("2000")),
    COLLEGE_SECOND("COLLEGE_SECOND", "学院二等奖学金", new BigDecimal("1000")),
    COLLEGE_THIRD("COLLEGE_THIRD", "学院三等奖学金", new BigDecimal("500")),
    SINGLE("SINGLE", "单项奖学金", new BigDecimal("300"));

    private final String code;
    private final String description;
    private final BigDecimal amount;

    ScholarshipTypeEnum(String code, String description, BigDecimal amount) {
        this.code = code;
        this.description = description;
        this.amount = amount;
    }

    public String getCode() { return code; }
    public String getDescription() { return description; }
    public BigDecimal getAmount() { return amount; }
}
