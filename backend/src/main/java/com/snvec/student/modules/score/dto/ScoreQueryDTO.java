package com.snvec.student.modules.score.dto;

import lombok.Data;

/**
 * 成绩查询DTO
 */
public class ScoreQueryDTO {

    private Long studentId;

    private Long courseId;

    private Long offeringId;

    private String semester;

    private Integer pageNum = 1;

    private Integer pageSize = 20;

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
    public Long getOfferingId() { return offeringId; }
    public void setOfferingId(Long offeringId) { this.offeringId = offeringId; }
    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public Integer getPageNum() { return pageNum; }
    public void setPageNum(Integer pageNum) { this.pageNum = pageNum; }
    public Integer getPageSize() { return pageSize; }
    public void setPageSize(Integer pageSize) { this.pageSize = pageSize; }
}
