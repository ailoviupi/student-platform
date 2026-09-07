package com.snvec.student.modules.score.dto;

import java.math.BigDecimal;

public class GpaVO {

    private Long studentId;
    private String semester;
    private BigDecimal gpa;
    private BigDecimal totalCredits;
    private Integer courseCount;

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }
    public BigDecimal getGpa() { return gpa; }
    public void setGpa(BigDecimal gpa) { this.gpa = gpa; }
    public BigDecimal getTotalCredits() { return totalCredits; }
    public void setTotalCredits(BigDecimal totalCredits) { this.totalCredits = totalCredits; }
    public Integer getCourseCount() { return courseCount; }
    public void setCourseCount(Integer courseCount) { this.courseCount = courseCount; }

    public static GpaVOBuilder builder() {
        return new GpaVOBuilder();
    }

    public static class GpaVOBuilder {
        private final GpaVO vo = new GpaVO();

        public GpaVOBuilder studentId(Long studentId) { vo.setStudentId(studentId); return this; }
        public GpaVOBuilder semester(String semester) { vo.setSemester(semester); return this; }
        public GpaVOBuilder gpa(BigDecimal gpa) { vo.setGpa(gpa); return this; }
        public GpaVOBuilder totalCredits(BigDecimal totalCredits) { vo.setTotalCredits(totalCredits); return this; }
        public GpaVOBuilder courseCount(Integer courseCount) { vo.setCourseCount(courseCount); return this; }
        public GpaVO build() { return vo; }
    }
}
