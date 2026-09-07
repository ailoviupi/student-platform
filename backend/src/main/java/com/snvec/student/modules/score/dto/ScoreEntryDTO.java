package com.snvec.student.modules.score.dto;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ScoreEntryDTO {

    @NotNull(message = "学生ID不能为空")
    private Long studentId;
    private String studentNo;

    @NotNull(message = "课程ID不能为空")
    private Long courseId;

    private Long offeringId;

    @NotNull(message = "学期不能为空")
    private String semester;

    private BigDecimal usualScore;
    private BigDecimal midtermScore;
    private BigDecimal finalScore;

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public String getStudentNo() { return studentNo; }
    public void setStudentNo(String studentNo) { this.studentNo = studentNo; }
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
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
}
