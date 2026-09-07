package com.snvec.student.modules.dormitory.dto;

import com.snvec.student.common.enums.RepairTypeEnum;
import lombok.Data;

public class RepairQueryDTO {

    private Long studentId;
    private Integer status;
    private RepairTypeEnum repairType;
    private Integer isUrgent;
    private Integer pageNum = 1;
    private Integer pageSize = 20;

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public RepairTypeEnum getRepairType() { return repairType; }
    public void setRepairType(RepairTypeEnum repairType) { this.repairType = repairType; }
    public Integer getIsUrgent() { return isUrgent; }
    public void setIsUrgent(Integer isUrgent) { this.isUrgent = isUrgent; }

    public Integer getPageNum() { return pageNum; }
    public void setPageNum(Integer pageNum) { this.pageNum = pageNum; }
    public Integer getPageSize() { return pageSize; }
    public void setPageSize(Integer pageSize) { this.pageSize = pageSize; }
}
