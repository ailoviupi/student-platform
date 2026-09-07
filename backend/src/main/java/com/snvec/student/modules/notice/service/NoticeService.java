package com.snvec.student.modules.notice.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snvec.student.common.exception.BusinessException;
import com.snvec.student.modules.notice.dto.NoticeDTO;
import com.snvec.student.modules.notice.dto.NoticeQueryDTO;
import com.snvec.student.modules.notice.dto.NoticeVO;
import com.snvec.student.modules.notice.entity.Notice;
import com.snvec.student.modules.notice.mapper.NoticeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoticeService {

    private static final Logger log = LoggerFactory.getLogger(NoticeService.class);

    private final NoticeMapper noticeMapper;

    // 敏感词列表
    private static final List<String> SENSITIVE_WORDS = Arrays.asList("反动", "色情", "暴力", "赌博");

    public NoticeService(NoticeMapper noticeMapper) {
        this.noticeMapper = noticeMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    public void publishNotice(NoticeDTO dto, Long publisherId) {
        checkSensitiveWords(dto.getTitle());
        checkSensitiveWords(dto.getContent());

        Notice notice = new Notice();
        BeanUtils.copyProperties(dto, notice);
        notice.setPublisherId(publisherId);
        notice.setCreatedBy(publisherId);

        if (dto.getScheduledTime() != null && dto.getScheduledTime().isAfter(LocalDateTime.now())) {
            notice.setStatus(0);
        } else {
            notice.setStatus(1);
            notice.setPublishTime(LocalDateTime.now());
        }

        noticeMapper.insert(notice);
    }

    private void checkSensitiveWords(String content) {
        if (content == null) return;
        for (String word : SENSITIVE_WORDS) {
            if (content.contains(word)) {
                throw new BusinessException("内容包含敏感词: " + word);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void withdrawNotice(Long noticeId, Long publisherId) {
        Notice notice = noticeMapper.selectById(noticeId);
        if (notice == null) throw new BusinessException("通知不存在");
        if (!notice.getPublisherId().equals(publisherId)) {
            throw new BusinessException("无权撤回他人通知");
        }
        notice.setStatus(2);
        noticeMapper.updateById(notice);
    }

    public Page<NoticeVO> pageNotices(NoticeQueryDTO query) {
        Page<Notice> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<Notice> wrapper = new LambdaQueryWrapper<>();

        if (query.getNoticeType() != null) {
            wrapper.eq(Notice::getNoticeType, query.getNoticeType());
        }
        if (query.getStatus() != null) {
            wrapper.eq(Notice::getStatus, query.getStatus());
        }
        if (query.getDeptId() != null) {
            wrapper.eq(Notice::getDeptId, query.getDeptId());
        }
        if (query.getKeyword() != null) {
            wrapper.like(Notice::getTitle, query.getKeyword());
        }

        wrapper.eq(Notice::getStatus, 1);
        wrapper.orderByDesc(Notice::getPublishTime);
        Page<Notice> resultPage = noticeMapper.selectPage(page, wrapper);

        Page<NoticeVO> voPage = new Page<>();
        BeanUtils.copyProperties(resultPage, voPage, "records");
        voPage.setRecords(resultPage.getRecords().stream().map(n -> {
            NoticeVO vo = new NoticeVO();
            BeanUtils.copyProperties(n, vo);
            return vo;
        }).collect(Collectors.toList()));

        return voPage;
    }

    public void publishScheduledNotices() {
        List<Notice> scheduledNotices = noticeMapper.selectList(
            new LambdaQueryWrapper<Notice>()
                .eq(Notice::getStatus, 0)
                .le(Notice::getScheduledTime, LocalDateTime.now())
        );

        for (Notice notice : scheduledNotices) {
            notice.setStatus(1);
            notice.setPublishTime(LocalDateTime.now());
            noticeMapper.updateById(notice);
            log.info("定时发布通知: {}", notice.getTitle());
        }
    }

    public NoticeVO getById(Long noticeId) {
        Notice notice = noticeMapper.selectById(noticeId);
        if (notice == null) return null;
        NoticeVO vo = new NoticeVO();
        BeanUtils.copyProperties(notice, vo);
        return vo;
    }
}
