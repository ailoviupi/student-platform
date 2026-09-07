package com.snvec.student.modules.dormitory.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.snvec.student.common.enums.RepairTypeEnum;
import java.io.Serializable;
import java.time.LocalDateTime;

@TableName("sm_repair")
public class Repair implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long studentId;
    private String studentNo;
    private Long roomId;
    private RepairTypeEnum repairType;
    private String description;
    private String images;
    private Integer isUrgent;
    private Integer status;
    private Long workerId;
    private String workerName;
    private String workerPhone;
    private LocalDateTime dispatchTime;
    private LocalDateTime repairTime;
    private LocalDateTime finishTime;
    private Integer rating;
    private String ratingContent;
    private LocalDateTime ratingTime;

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
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Long getWorkerId() { return workerId; }
    public void setWorkerId(Long workerId) { this.workerId = workerId; }
    public String getWorkerName() { return workerName; }
    public void setWorkerName(String workerName) { this.workerName = workerName; }
    public String getWorkerPhone() { return workerPhone; }
    public void setWorkerPhone(String workerPhone) { this.workerPhone = workerPhone; }
    public LocalDateTime getDispatchTime() { return dispatchTime; }
    public void setDispatchTime(LocalDateTime dispatchTime) { this.dispatchTime = dispatchTime; }
    public LocalDateTime getRepairTime() { return repairTime; }
    public void setRepairTime(LocalDateTime repairTime) { this.repairTime = repairTime; }
    public LocalDateTime getFinishTime() { return finishTime; }
    public void setFinishTime(LocalDateTime finishTime) { this.finishTime = finishTime; }
    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }
    public String getRatingContent() { return ratingContent; }
    public void setRatingContent(String ratingContent) { this.ratingContent = ratingContent; }
    public LocalDateTime getRatingTime() { return ratingTime; }
    public void setRatingTime(LocalDateTime ratingTime) { this.ratingTime = ratingTime; }
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

