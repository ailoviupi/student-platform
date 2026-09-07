package com.snvec.student.modules.attendance.dto;

import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 请假审批DTO
 */
public class LeaveApprovalDTO {

    @NotNull(message = "请假ID不能为空")
    private Long leaveId;

    @NotNull(message = "审批结果不能为空")
    private Boolean approved;

    private String remark;

    public Long getLeaveId() { return leaveId; }
    public void setLeaveId(Long leaveId) { this.leaveId = leaveId; }
    public Boolean getApproved() { return approved; }
    public void setApproved(Boolean approved) { this.approved = approved; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
