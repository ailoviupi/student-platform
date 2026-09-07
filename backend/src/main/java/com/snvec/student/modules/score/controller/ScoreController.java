package com.snvec.student.modules.score.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snvec.student.common.result.Result;
import com.snvec.student.modules.score.dto.*;
import com.snvec.student.modules.score.service.ScoreService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 成绩管理控制器
 */
@RestController
@RequestMapping("/score")
@RequiredArgsConstructor
public class ScoreController {

    private final ScoreService scoreService;

    @PostMapping("/entry")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ACADEMIC_ADMIN')")
    public Result<Void> entryScore(@Valid @RequestBody ScoreEntryDTO dto, 
                                    @AuthenticationPrincipal Long teacherId) {
        scoreService.entryScore(dto, teacherId);
        return Result.success("成绩录入成功", null);
    }

    @PostMapping("/batch-entry")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ACADEMIC_ADMIN')")
    public Result<Void> batchEntryScore(@Valid @RequestBody java.util.List<ScoreEntryDTO> dtoList,
                                         @AuthenticationPrincipal Long teacherId) {
        for (ScoreEntryDTO dto : dtoList) {
            scoreService.entryScore(dto, teacherId);
        }
        return Result.success("批量录入成功，共" + dtoList.size() + "条", null);
    }

    @GetMapping("/page")
    public Result<Page<ScoreVO>> pageScores(ScoreQueryDTO query) {
        return Result.success(scoreService.pageScores(query));
    }

    @GetMapping("/gpa/{semester}")
    public Result<GpaVO> calculateGpa(@AuthenticationPrincipal Long studentId,
                                       @PathVariable String semester) {
        return Result.success(scoreService.calculateSemesterGpa(studentId, semester));
    }

    @PutMapping("/lock/{offeringId}")
    @PreAuthorize("hasRole('ACADEMIC_ADMIN')")
    public Result<Void> lockScores(@PathVariable Long offeringId) {
        scoreService.lockScores(offeringId);
        return Result.success("成绩已锁定", null);
    }
}
