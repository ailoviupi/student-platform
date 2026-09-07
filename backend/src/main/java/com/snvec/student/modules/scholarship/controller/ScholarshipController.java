package com.snvec.student.modules.scholarship.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snvec.student.common.result.Result;
import com.snvec.student.modules.scholarship.dto.*;
import com.snvec.student.modules.scholarship.service.ScholarshipService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/scholarship")
@RequiredArgsConstructor
public class ScholarshipController {

    private final ScholarshipService scholarshipService;

    @PostMapping("/apply")
    @PreAuthorize("hasRole('STUDENT')")
    public Result<Void> apply(@Valid @RequestBody ScholarshipApplyDTO dto,
                               @AuthenticationPrincipal Long studentId) {
        scholarshipService.applyScholarship(dto, studentId);
        return Result.success("申请已提交", null);
    }

    @PostMapping("/class-review/{applyId}")
    @PreAuthorize("hasRole('COUNSELOR')")
    public Result<Void> classReview(@PathVariable Long applyId,
                                     @RequestParam boolean approved) {
        scholarshipService.classReview(applyId, approved);
        return Result.success("评议完成", null);
    }

    @PostMapping("/dept-publicity/{applyId}")
    @PreAuthorize("hasRole('DEPT_ADMIN')")
    public Result<Void> deptPublicity(@PathVariable Long applyId) {
        scholarshipService.deptPublicity(applyId);
        return Result.success("已公示", null);
    }

    @PostMapping("/college-approve/{applyId}")
    @PreAuthorize("hasRole('ACADEMIC_ADMIN')")
    public Result<Void> collegeApprove(@PathVariable Long applyId,
                                        @RequestParam boolean approved) {
        scholarshipService.collegeApprove(applyId, approved);
        return Result.success("审定完成", null);
    }

    @PostMapping("/college-publicity/{applyId}")
    @PreAuthorize("hasRole('ACADEMIC_ADMIN')")
    public Result<Void> collegePublicity(@PathVariable Long applyId) {
        scholarshipService.collegePublicity(applyId);
        return Result.success("已公示", null);
    }

    @GetMapping("/page")
    public Result<Page<ScholarshipVO>> page(ScholarshipQueryDTO query) {
        return Result.success(scholarshipService.pageScholarships(query));
    }
}
