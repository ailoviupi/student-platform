package com.snvec.student.modules.scholarship.dto;

import com.snvec.student.common.enums.ScholarshipTypeEnum;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

public class ScholarshipApplyDTO {

    @NotNull(message = "奖学金类型不能为空")
    private ScholarshipTypeEnum scholarshipType;

    @NotBlank(message = "学年不能为空")
    private String academicYear;

    private BigDecimal gpa;
    private Integer ranking;
    private Integer totalStudents;
    private String reason;

    public ScholarshipTypeEnum getScholarshipType() { return scholarshipType; }
    public void setScholarshipType(ScholarshipTypeEnum scholarshipType) { this.scholarshipType = scholarshipType; }
    public String getAcademicYear() { return academicYear; }
    public void setAcademicYear(String academicYear) { this.academicYear = academicYear; }
    public BigDecimal getGpa() { return gpa; }
    public void setGpa(BigDecimal gpa) { this.gpa = gpa; }
    public Integer getRanking() { return ranking; }
    public void setRanking(Integer ranking) { this.ranking = ranking; }
    public Integer getTotalStudents() { return totalStudents; }
    public void setTotalStudents(Integer totalStudents) { this.totalStudents = totalStudents; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
