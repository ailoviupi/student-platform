package com.snvec.student.modules.scholarship.dto;

import com.snvec.student.common.enums.ScholarshipTypeEnum;
import lombok.Data;

public class ScholarshipQueryDTO {

    private Long studentId;
    private Integer status;
    private ScholarshipTypeEnum scholarshipType;
    private String academicYear;
    private Integer pageNum = 1;
    private Integer pageSize = 20;

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public ScholarshipTypeEnum getScholarshipType() { return scholarshipType; }
    public void setScholarshipType(ScholarshipTypeEnum scholarshipType) { this.scholarshipType = scholarshipType; }
    public String getAcademicYear() { return academicYear; }
    public void setAcademicYear(String academicYear) { this.academicYear = academicYear; }

    public Integer getPageNum() { return pageNum; }
    public void setPageNum(Integer pageNum) { this.pageNum = pageNum; }
    public Integer getPageSize() { return pageSize; }
    public void setPageSize(Integer pageSize) { this.pageSize = pageSize; }
}
