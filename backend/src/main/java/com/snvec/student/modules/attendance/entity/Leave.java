package com.snvec.student.modules.attendance.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.snvec.student.common.enums.LeaveTypeEnum;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("sm_leave")
public class Leave implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long studentId;
    private String studentNo;
    private LeaveTypeEnum leaveType;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal days;
    private String reason;
    private String attachments;
    private Integer status;
    private Integer currentLevel;
    private Long counselorId;
    private LocalDateTime counselorAuditTime;
    private String counselorRemark;
    private Long deptAdminId;
    private LocalDateTime deptAuditTime;
    private String deptRemark;
    private Long studentAdminId;
    private LocalDateTime studentAuditTime;
    private String studentRemark;

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
