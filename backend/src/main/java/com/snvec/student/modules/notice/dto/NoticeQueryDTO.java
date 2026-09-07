package com.snvec.student.modules.notice.dto;

import com.snvec.student.common.enums.NoticeTypeEnum;

public class NoticeQueryDTO {

    private NoticeTypeEnum noticeType;
    private Integer status;
    private Long deptId;
    private String keyword;
    private Integer pageNum = 1;
    private Integer pageSize = 20;

    public NoticeTypeEnum getNoticeType() { return noticeType; }
    public void setNoticeType(NoticeTypeEnum noticeType) { this.noticeType = noticeType; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Long getDeptId() { return deptId; }
    public void setDeptId(Long deptId) { this.deptId = deptId; }
    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) { this.keyword = keyword; }
    public Integer getPageNum() { return pageNum; }
    public void setPageNum(Integer pageNum) { this.pageNum = pageNum; }
    public Integer getPageSize() { return pageSize; }
    public void setPageSize(Integer pageSize) { this.pageSize = pageSize; }
}
