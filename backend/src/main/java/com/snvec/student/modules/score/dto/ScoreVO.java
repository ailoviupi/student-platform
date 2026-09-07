package com.snvec.student.modules.score.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 成绩响应VO
 */
public class ScoreVO {

    private Long id;
    private Long studentId;
    private String studentNo;
    private Long courseId;
    private String courseName;
    private Long offeringId;
    private String semester;
    private BigDecimal usualScore;
    private BigDecimal midtermScore;
    private BigDecimal finalScore;
    private BigDecimal totalScore;
    private BigDecimal point;
    private String gradeLevel;
    private Integer isPass;
    private Integer isLocked;
    private LocalDateTime lockTime;
    private String remark;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public String getStudentNo() { return studentNo; }
    public void setStudentNo(String studentNo) { this.studentNo = studentNo; }
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public Long getOfferingId() { return offeringId; }
    public void setOfferingId(Long offeringId) { this.offeringId = offeringId; }
    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }
    public BigDecimal getUsualScore() { return usualScore; }
    public void setUsualScore(BigDecimal usualScore) { this.usualScore = usualScore; }
    public BigDecimal getMidtermScore() { return midtermScore; }
    public void setMidtermScore(BigDecimal midtermScore) { this.midtermScore = midtermScore; }
    public BigDecimal getFinalScore() { return finalScore; }
    public void setFinalScore(BigDecimal finalScore) { this.finalScore = finalScore; }
    public BigDecimal getTotalScore() { return totalScore; }
    public void setTotalScore(BigDecimal totalScore) { this.totalScore = totalScore; }
    public BigDecimal getPoint() { return point; }
    public void setPoint(BigDecimal point) { this.point = point; }
    public String getGradeLevel() { return gradeLevel; }
    public void setGradeLevel(String gradeLevel) { this.gradeLevel = gradeLevel; }
    public Integer getIsPass() { return isPass; }
    public void setIsPass(Integer isPass) { this.isPass = isPass; }
    public Integer getIsLocked() { return isLocked; }
    public void setIsLocked(Integer isLocked) { this.isLocked = isLocked; }
    public LocalDateTime getLockTime() { return lockTime; }
    public void setLockTime(LocalDateTime lockTime) { this.lockTime = lockTime; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
