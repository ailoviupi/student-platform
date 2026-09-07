package com.snvec.student.modules.attendance.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snvec.student.common.result.Result;
import com.snvec.student.modules.attendance.dto.*;
import com.snvec.student.modules.attendance.service.LeaveService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 请假管理控制器
 */
@RestController
@RequestMapping("/attendance")
@RequiredArgsConstructor
public class LeaveController {

    private final LeaveService leaveService;

    @PostMapping("/leave/apply")
    @PreAuthorize("hasRole('STUDENT')")
    public Result<Void> applyLeave(@Valid @RequestBody LeaveApplyDTO dto,
                                    @AuthenticationPrincipal Long studentId) {
        leaveService.applyLeave(dto, studentId);
        return Result.success("请假申请已提交", null);
    }

    @PostMapping("/leave/approve")
    @PreAuthorize("hasRole('COUNSELOR') or hasRole('DEPT_ADMIN') or hasRole('ACADEMIC_ADMIN')")
    public Result<Void> approveLeave(@Valid @RequestBody LeaveApprovalDTO dto,
                                      @AuthenticationPrincipal Long approverId) {
        leaveService.approveLeave(dto, approverId);
        return Result.success("审批完成", null);
    }

    @GetMapping("/leave/page")
    public Result<Page<LeaveVO>> pageLeaves(LeaveQueryDTO query) {
        return Result.success(leaveService.pageLeaves(query));
    }

    @GetMapping("/leave/pending")
    @PreAuthorize("hasRole('COUNSELOR') or hasRole('DEPT_ADMIN') or hasRole('ACADEMIC_ADMIN')")
    public Result<Page<LeaveVO>> pendingLeaves(LeaveQueryDTO query,
                                               @AuthenticationPrincipal Long approverId) {
        query.setCounselorId(approverId);
        query.setStatus(0);
        return Result.success(leaveService.pageLeaves(query));
    }
}
