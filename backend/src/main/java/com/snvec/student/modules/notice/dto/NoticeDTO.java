package com.snvec.student.modules.notice.dto;

import com.snvec.student.common.enums.NoticeTypeEnum;
import com.snvec.student.common.enums.NoticeLevelEnum;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

public class NoticeDTO {

    @NotBlank(message = "标题不能为空")
    private String title;

    @NotBlank(message = "内容不能为空")
    private String content;

    @NotNull(message = "通知类型不能为空")
    private NoticeTypeEnum noticeType;

    private NoticeLevelEnum level = NoticeLevelEnum.NORMAL;

    private Long deptId;
    private Long classId;
    private String targetUsers;
    private String attachments;
    private Integer requireReceipt = 0;
    private LocalDateTime expireTime;
    private LocalDateTime scheduledTime;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public NoticeTypeEnum getNoticeType() { return noticeType; }
    public void setNoticeType(NoticeTypeEnum noticeType) { this.noticeType = noticeType; }
    public Long getDeptId() { return deptId; }
    public void setDeptId(Long deptId) { this.deptId = deptId; }
    public Long getClassId() { return classId; }
    public void setClassId(Long classId) { this.classId = classId; }
    public String getTargetUsers() { return targetUsers; }
    public void setTargetUsers(String targetUsers) { this.targetUsers = targetUsers; }
    public String getAttachments() { return attachments; }
    public void setAttachments(String attachments) { this.attachments = attachments; }
    public LocalDateTime getExpireTime() { return expireTime; }
    public void setExpireTime(LocalDateTime expireTime) { this.expireTime = expireTime; }
    public LocalDateTime getScheduledTime() { return scheduledTime; }
    public void setScheduledTime(LocalDateTime scheduledTime) { this.scheduledTime = scheduledTime; }

    public NoticeLevelEnum getLevel() { return level; }
    public void setLevel(NoticeLevelEnum level) { this.level = level; }
    public Integer getRequireReceipt() { return requireReceipt; }
    public void setRequireReceipt(Integer requireReceipt) { this.requireReceipt = requireReceipt; }
}
