package com.snvec.student.modules.dormitory.dto;

import com.snvec.student.common.enums.RepairTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;

public class RepairVO {

    private Long id;
    private Long studentId;
    private String studentNo;
    private String studentName;
    private Long roomId;
    private String roomCode;
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
    private LocalDateTime createdTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public String getStudentNo() { return studentNo; }
    public void setStudentNo(String studentNo) { this.studentNo = studentNo; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public Long getRoomId() { return roomId; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }
    public String getRoomCode() { return roomCode; }
    public void setRoomCode(String roomCode) { this.roomCode = roomCode; }
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
    public LocalDateTime getCreatedTime() { return createdTime; }
    public void setCreatedTime(LocalDateTime createdTime) { this.createdTime = createdTime; }
}
