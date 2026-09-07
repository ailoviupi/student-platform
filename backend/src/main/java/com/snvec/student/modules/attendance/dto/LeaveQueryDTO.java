package com.snvec.student.modules.attendance.dto;

import com.snvec.student.common.enums.LeaveTypeEnum;

public class LeaveQueryDTO {

    private Long studentId;
    private Integer status;
    private LeaveTypeEnum leaveType;
    private Long counselorId;
    private Integer pageNum = 1;
    private Integer pageSize = 20;

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public LeaveTypeEnum getLeaveType() { return leaveType; }
    public void setLeaveType(LeaveTypeEnum leaveType) { this.leaveType = leaveType; }
    public Long getCounselorId() { return counselorId; }
    public void setCounselorId(Long counselorId) { this.counselorId = counselorId; }
    public Integer getPageNum() { return pageNum; }
    public void setPageNum(Integer pageNum) { this.pageNum = pageNum; }
    public Integer getPageSize() { return pageSize; }
    public void setPageSize(Integer pageSize) { this.pageSize = pageSize; }
}
