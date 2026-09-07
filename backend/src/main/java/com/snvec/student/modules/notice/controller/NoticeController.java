package com.snvec.student.modules.notice.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snvec.student.common.result.Result;
import com.snvec.student.modules.notice.dto.*;
import com.snvec.student.modules.notice.service.NoticeService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @PostMapping("/publish")
    @PreAuthorize("hasRole('COUNSELOR') or hasRole('DEPT_ADMIN') or hasRole('ACADEMIC_ADMIN') or hasRole('SYS_ADMIN')")
    public Result<Void> publish(@Valid @RequestBody NoticeDTO dto,
                                 @AuthenticationPrincipal Long publisherId) {
        noticeService.publishNotice(dto, publisherId);
        return Result.success("通知发布成功", null);
    }

    @PutMapping("/withdraw/{noticeId}")
    @PreAuthorize("hasRole('COUNSELOR') or hasRole('DEPT_ADMIN') or hasRole('ACADEMIC_ADMIN') or hasRole('SYS_ADMIN')")
    public Result<Void> withdraw(@PathVariable Long noticeId,
                                  @AuthenticationPrincipal Long publisherId) {
        noticeService.withdrawNotice(noticeId, publisherId);
        return Result.success("通知已撤回", null);
    }

    @GetMapping("/page")
    public Result<Page<NoticeVO>> page(NoticeQueryDTO query) {
        return Result.success(noticeService.pageNotices(query));
    }

    @GetMapping("/{noticeId}")
    public Result<NoticeVO> getById(@PathVariable Long noticeId) {
        // 增加浏览次数
        return Result.success(noticeService.getById(noticeId));
    }
}
