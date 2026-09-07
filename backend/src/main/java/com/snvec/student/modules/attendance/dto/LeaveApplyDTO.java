package com.snvec.student.modules.attendance.dto;

import com.snvec.student.common.enums.LeaveTypeEnum;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 请假申请DTO
 */
public class LeaveApplyDTO {

    @NotBlank(message = "学号不能为空")
    private String studentNo;

    @NotNull(message = "请假类型不能为空")
    private LeaveTypeEnum leaveType;

    @NotNull(message = "开始时间不能为空")
    private LocalDateTime startTime;

    @NotNull(message = "结束时间不能为空")
    private LocalDateTime endTime;

    @NotBlank(message = "请假事由不能为空")
    private String reason;

    private String attachments;

    public String getStudentNo() { return studentNo; }
    public void setStudentNo(String studentNo) { this.studentNo = studentNo; }
    public LeaveTypeEnum getLeaveType() { return leaveType; }
    public void setLeaveType(LeaveTypeEnum leaveType) { this.leaveType = leaveType; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getAttachments() { return attachments; }
    public void setAttachments(String attachments) { this.attachments = attachments; }
}
