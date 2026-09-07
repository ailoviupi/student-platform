package com.snvec.student.modules.attendance.dto;

import com.snvec.student.common.enums.LeaveTypeEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 请假响应VO
 */
public class LeaveVO {

    private Long id;
    private Long studentId;
    private String studentNo;
    private String studentName;
    private LeaveTypeEnum leaveType;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal days;
    private String reason;
    private String attachments;
    private Integer status;
    private Integer currentLevel;
    private Long counselorId;
    private String counselorName;
    private LocalDateTime counselorAuditTime;
    private String counselorRemark;
    private Long deptAdminId;
    private LocalDateTime deptAuditTime;
    private String deptRemark;
    private Long studentAdminId;
    private LocalDateTime studentAuditTime;
    private String studentRemark;
    private LocalDateTime createdTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public String getStudentNo() { return studentNo; }
    public void setStudentNo(String studentNo) { this.studentNo = studentNo; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public LeaveTypeEnum getLeaveType() { return leaveType; }
    public void setLeaveType(LeaveTypeEnum leaveType) { this.leaveType = leaveType; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public BigDecimal getDays() { return days; }
    public void setDays(BigDecimal days) { this.days = days; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getAttachments() { return attachments; }
    public void setAttachments(String attachments) { this.attachments = attachments; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Integer getCurrentLevel() { return currentLevel; }
    public void setCurrentLevel(Integer currentLevel) { this.currentLevel = currentLevel; }
    public Long getCounselorId() { return counselorId; }
    public void setCounselorId(Long counselorId) { this.counselorId = counselorId; }
    public String getCounselorName() { return counselorName; }
    public void setCounselorName(String counselorName) { this.counselorName = counselorName; }
    public LocalDateTime getCounselorAuditTime() { return counselorAuditTime; }
    public void setCounselorAuditTime(LocalDateTime counselorAuditTime) { this.counselorAuditTime = counselorAuditTime; }
    public String getCounselorRemark() { return counselorRemark; }
    public void setCounselorRemark(String counselorRemark) { this.counselorRemark = counselorRemark; }
    public Long getDeptAdminId() { return deptAdminId; }
    public void setDeptAdminId(Long deptAdminId) { this.deptAdminId = deptAdminId; }
    public LocalDateTime getDeptAuditTime() { return deptAuditTime; }
    public void setDeptAuditTime(LocalDateTime deptAuditTime) { this.deptAuditTime = deptAuditTime; }
    public String getDeptRemark() { return deptRemark; }
    public void setDeptRemark(String deptRemark) { this.deptRemark = deptRemark; }
    public Long getStudentAdminId() { return studentAdminId; }
    public void setStudentAdminId(Long studentAdminId) { this.studentAdminId = studentAdminId; }
    public LocalDateTime getStudentAuditTime() { return studentAuditTime; }
    public void setStudentAuditTime(LocalDateTime studentAuditTime) { this.studentAuditTime = studentAuditTime; }
    public String getStudentRemark() { return studentRemark; }
    public void setStudentRemark(String studentRemark) { this.studentRemark = studentRemark; }
    public LocalDateTime getCreatedTime() { return createdTime; }
    public void setCreatedTime(LocalDateTime createdTime) { this.createdTime = createdTime; }
}
