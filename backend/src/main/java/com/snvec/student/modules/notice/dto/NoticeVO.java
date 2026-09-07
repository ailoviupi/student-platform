package com.snvec.student.modules.notice.dto;

import com.snvec.student.common.enums.NoticeTypeEnum;
import com.snvec.student.common.enums.NoticeLevelEnum;
import lombok.Data;

import java.time.LocalDateTime;

public class NoticeVO {

    private Long id;
    private String title;
    private String content;
    private NoticeTypeEnum noticeType;
    private NoticeLevelEnum level;
    private Long publisherId;
    private String publisherName;
    private Long deptId;
    private Long classId;
    private String targetUsers;
    private String attachments;
    private Integer requireReceipt;
    private LocalDateTime publishTime;
    private LocalDateTime expireTime;
    private LocalDateTime scheduledTime;
    private Integer status;
    private Integer viewCount;
    private LocalDateTime createdTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public NoticeTypeEnum getNoticeType() { return noticeType; }
    public void setNoticeType(NoticeTypeEnum noticeType) { this.noticeType = noticeType; }
    public NoticeLevelEnum getLevel() { return level; }
    public void setLevel(NoticeLevelEnum level) { this.level = level; }
    public Long getPublisherId() { return publisherId; }
    public void setPublisherId(Long publisherId) { this.publisherId = publisherId; }
    public String getPublisherName() { return publisherName; }
    public void setPublisherName(String publisherName) { this.publisherName = publisherName; }
    public Long getDeptId() { return deptId; }
    public void setDeptId(Long deptId) { this.deptId = deptId; }
    public Long getClassId() { return classId; }
    public void setClassId(Long classId) { this.classId = classId; }
    public String getTargetUsers() { return targetUsers; }
    public void setTargetUsers(String targetUsers) { this.targetUsers = targetUsers; }
    public String getAttachments() { return attachments; }
    public void setAttachments(String attachments) { this.attachments = attachments; }
    public Integer getRequireReceipt() { return requireReceipt; }
    public void setRequireReceipt(Integer requireReceipt) { this.requireReceipt = requireReceipt; }
    public LocalDateTime getPublishTime() { return publishTime; }
    public void setPublishTime(LocalDateTime publishTime) { this.publishTime = publishTime; }
    public LocalDateTime getExpireTime() { return expireTime; }
    public void setExpireTime(LocalDateTime expireTime) { this.expireTime = expireTime; }
    public LocalDateTime getScheduledTime() { return scheduledTime; }
    public void setScheduledTime(LocalDateTime scheduledTime) { this.scheduledTime = scheduledTime; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Integer getViewCount() { return viewCount; }
    public void setViewCount(Integer viewCount) { this.viewCount = viewCount; }
    public LocalDateTime getCreatedTime() { return createdTime; }
    public void setCreatedTime(LocalDateTime createdTime) { this.createdTime = createdTime; }
}
