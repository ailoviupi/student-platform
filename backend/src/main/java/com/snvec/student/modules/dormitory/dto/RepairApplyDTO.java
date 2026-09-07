package com.snvec.student.modules.dormitory.dto;

import com.snvec.student.common.enums.RepairTypeEnum;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

public class RepairApplyDTO {

    @NotBlank(message = "学号不能为空")
    private String studentNo;

    @NotNull(message = "房间ID不能为空")
    private Long roomId;

    @NotNull(message = "报修类型不能为空")
    private RepairTypeEnum repairType;

    @NotBlank(message = "故障描述不能为空")
    private String description;

    private String images;

    private Integer isUrgent = 0;

    public String getStudentNo() { return studentNo; }
    public void setStudentNo(String studentNo) { this.studentNo = studentNo; }
    public Long getRoomId() { return roomId; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }
    public RepairTypeEnum getRepairType() { return repairType; }
    public void setRepairType(RepairTypeEnum repairType) { this.repairType = repairType; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getImages() { return images; }
    public void setImages(String images) { this.images = images; }

    public Integer getIsUrgent() { return isUrgent; }
    public void setIsUrgent(Integer isUrgent) { this.isUrgent = isUrgent; }
}
