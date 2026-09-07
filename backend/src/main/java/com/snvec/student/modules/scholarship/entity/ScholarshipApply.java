package com.snvec.student.modules.scholarship.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.snvec.student.common.enums.ScholarshipTypeEnum;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("sm_scholarship_apply")
public class ScholarshipApply implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long studentId;
    private String studentNo;
    private ScholarshipTypeEnum scholarshipType;
    private String academicYear;
    private BigDecimal gpa;
    private Integer ranking;
    private Integer totalStudents;
    private String reason;
    private Integer status;
    private LocalDateTime classReviewTime;
    private LocalDateTime deptReviewTime;
    private LocalDateTime deptPublicityStart;
    private LocalDateTime collegeReviewTime;
    private LocalDateTime collegePublicityStart;
    private BigDecimal amount;

    @TableField(fill = FieldFill.INSERT)
    private Long createdBy;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updatedBy;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;
    @TableLogic
    private Integer deleted;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public String getStudentNo() { return studentNo; }
    public void setStudentNo(String studentNo) { this.studentNo = studentNo; }
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
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public LocalDateTime getClassReviewTime() { return classReviewTime; }
    public void setClassReviewTime(LocalDateTime classReviewTime) { this.classReviewTime = classReviewTime; }
    public LocalDateTime getDeptReviewTime() { return deptReviewTime; }
    public void setDeptReviewTime(LocalDateTime deptReviewTime) { this.deptReviewTime = deptReviewTime; }
    public LocalDateTime getDeptPublicityStart() { return deptPublicityStart; }
    public void setDeptPublicityStart(LocalDateTime deptPublicityStart) { this.deptPublicityStart = deptPublicityStart; }
    public LocalDateTime getCollegeReviewTime() { return collegeReviewTime; }
    public void setCollegeReviewTime(LocalDateTime collegeReviewTime) { this.collegeReviewTime = collegeReviewTime; }
    public LocalDateTime getCollegePublicityStart() { return collegePublicityStart; }
    public void setCollegePublicityStart(LocalDateTime collegePublicityStart) { this.collegePublicityStart = collegePublicityStart; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    public LocalDateTime getCreatedTime() { return createdTime; }
    public void setCreatedTime(LocalDateTime createdTime) { this.createdTime = createdTime; }
    public Long getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(Long updatedBy) { this.updatedBy = updatedBy; }
    public LocalDateTime getUpdatedTime() { return updatedTime; }
    public void setUpdatedTime(LocalDateTime updatedTime) { this.updatedTime = updatedTime; }
    public Integer getDeleted() { return deleted; }
    public void setDeleted(Integer deleted) { this.deleted = deleted; }
}
