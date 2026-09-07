package com.snvec.student.modules.dormitory.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snvec.student.common.result.Result;
import com.snvec.student.modules.dormitory.dto.*;
import com.snvec.student.modules.dormitory.service.RepairService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dormitory")
@RequiredArgsConstructor
public class RepairController {

    private final RepairService repairService;

    @PostMapping("/repair/apply")
    @PreAuthorize("hasRole('STUDENT')")
    public Result<Void> applyRepair(@Valid @RequestBody RepairApplyDTO dto,
                                     @AuthenticationPrincipal Long studentId) {
        repairService.applyRepair(dto, studentId);
        return Result.success("报修已提交", null);
    }

    @PostMapping("/repair/dispatch/{repairId}")
    @PreAuthorize("hasRole('ACADEMIC_ADMIN')")
    public Result<Void> dispatch(@PathVariable Long repairId,
                                  @RequestParam Long workerId,
                                  @RequestParam String workerName,
                                  @RequestParam String workerPhone) {
        repairService.dispatch(repairId, workerId, workerName, workerPhone);
        return Result.success("派单成功", null);
    }

    @PostMapping("/repair/start/{repairId}")
    @PreAuthorize("hasRole('ACADEMIC_ADMIN')")
    public Result<Void> startRepair(@PathVariable Long repairId) {
        repairService.startRepair(repairId);
        return Result.success("维修已开始", null);
    }

    @PostMapping("/repair/finish/{repairId}")
    @PreAuthorize("hasRole('ACADEMIC_ADMIN')")
    public Result<Void> finishRepair(@PathVariable Long repairId) {
        repairService.finishRepair(repairId);
        return Result.success("维修已完成", null);
    }

    @PostMapping("/repair/rating")
    @PreAuthorize("hasRole('STUDENT')")
    public Result<Void> ratingRepair(@Valid @RequestBody RepairRatingDTO dto,
                                      @AuthenticationPrincipal Long studentId) {
        repairService.ratingRepair(dto, studentId);
        return Result.success("评价成功", null);
    }

    @GetMapping("/repair/page")
    public Result<Page<RepairVO>> pageRepairs(RepairQueryDTO query) {
        return Result.success(repairService.pageRepairs(query));
    }
}
