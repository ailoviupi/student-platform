package com.snvec.student.modules.score.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snvec.student.common.exception.BusinessException;
import com.snvec.student.common.result.ResultCode;
import com.snvec.student.modules.score.dto.ScoreEntryDTO;
import com.snvec.student.modules.score.dto.ScoreQueryDTO;
import com.snvec.student.modules.score.dto.ScoreVO;
import com.snvec.student.modules.score.dto.GpaVO;
import com.snvec.student.modules.score.entity.Score;
import com.snvec.student.modules.score.mapper.ScoreMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 成绩服务
 * 
 * 成绩构成: 平时30% + 期中20% + 期末50%
 * 绩点计算: ≥60分, 绩点=(成绩-50)/10; <60绩点=0
 * GPA = Σ(绩点×学分) / Σ学分
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ScoreService {

    private final ScoreMapper scoreMapper;

    /**
     * 录入成绩
     */
    @Transactional(rollbackFor = Exception.class)
    public void entryScore(ScoreEntryDTO dto, Long teacherId) {
        // 查询是否已有成绩记录
        Score score = scoreMapper.selectOne(
            new LambdaQueryWrapper<Score>()
                .eq(Score::getStudentId, dto.getStudentId())
                .eq(Score::getCourseId, dto.getCourseId())
                .eq(Score::getSemester, dto.getSemester())
        );

        if (score == null) {
            score = new Score();
            score.setStudentId(dto.getStudentId());
            score.setStudentNo(dto.getStudentNo());
            score.setCourseId(dto.getCourseId());
            score.setOfferingId(dto.getOfferingId());
            score.setSemester(dto.getSemester());
            score.setCreatedBy(teacherId);
        } else {
            // 检查是否已锁定
            if (score.getIsLocked() != null && score.getIsLocked() == 1) {
                throw new BusinessException(ResultCode.SCORE_LOCKED);
            }
            score.setUpdatedBy(teacherId);
        }

        // 设置各分项成绩
        score.setUsualScore(dto.getUsualScore());
        score.setMidtermScore(dto.getMidtermScore());
        score.setFinalScore(dto.getFinalScore());

        // 计算总评成绩
        BigDecimal totalScore = calculateTotalScore(dto.getUsualScore(), dto.getMidtermScore(), dto.getFinalScore());
        score.setTotalScore(totalScore);

        // 计算绩点
        BigDecimal point = calculatePoint(totalScore);
        score.setPoint(point);

        // 计算等级
        score.setGradeLevel(calculateGradeLevel(totalScore));

        // 是否及格
        score.setIsPass(totalScore.compareTo(new BigDecimal("60")) >= 0 ? 1 : 0);

        if (score.getId() == null) {
            scoreMapper.insert(score);
        } else {
            scoreMapper.updateById(score);
        }
    }

    /**
     * 计算总评成绩
     * 平时30% + 期中20% + 期末50%
     */
    public BigDecimal calculateTotalScore(BigDecimal usual, BigDecimal midterm, BigDecimal fin) {
        BigDecimal total = BigDecimal.ZERO;
        if (usual != null) {
            total = total.add(usual.multiply(new BigDecimal("0.3")));
        }
        if (midterm != null) {
            total = total.add(midterm.multiply(new BigDecimal("0.2")));
        }
        if (fin != null) {
            total = total.add(fin.multiply(new BigDecimal("0.5")));
        }
        return total.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * 计算绩点
     * ≥60分: 绩点=(成绩-50)/10
     * <60分: 绩点=0
     */
    public BigDecimal calculatePoint(BigDecimal totalScore) {
        if (totalScore == null || totalScore.compareTo(new BigDecimal("60")) < 0) {
            return BigDecimal.ZERO;
        }
        return totalScore.subtract(new BigDecimal("50"))
                .divide(new BigDecimal("10"), 2, RoundingMode.HALF_UP);
    }

    /**
     * 计算等级
     * A: 90-100, B: 80-89, C: 70-79, D: 60-69, F: <60
     */
    public String calculateGradeLevel(BigDecimal totalScore) {
        if (totalScore == null) return "F";
        int score = totalScore.intValue();
        if (score >= 90) return "A";
        if (score >= 80) return "B";
        if (score >= 70) return "C";
        if (score >= 60) return "D";
        return "F";
    }

    /**
     * 计算学期GPA
     * GPA = Σ(绩点×学分) / Σ学分
     */
    public GpaVO calculateSemesterGpa(Long studentId, String semester) {
        List<Score> scores = scoreMapper.selectByStudentAndSemester(studentId, semester);
        
        if (scores == null || scores.isEmpty()) {
            return GpaVO.builder()
                    .studentId(studentId)
                    .semester(semester)
                    .gpa(BigDecimal.ZERO)
                    .totalCredits(BigDecimal.ZERO)
                    .courseCount(0)
                    .build();
        }

        BigDecimal totalPointCredits = BigDecimal.ZERO;
        BigDecimal totalCredits = BigDecimal.ZERO;

        for (Score score : scores) {
            if (score.getPoint() != null && score.getIsPass() != null && score.getIsPass() == 1) {
                // 这里简化处理，实际应从课程表获取学分
                BigDecimal credits = new BigDecimal("3.0"); // 默认学分
                totalPointCredits = totalPointCredits.add(score.getPoint().multiply(credits));
                totalCredits = totalCredits.add(credits);
            }
        }

        BigDecimal gpa = totalCredits.compareTo(BigDecimal.ZERO) > 0
                ? totalPointCredits.divide(totalCredits, 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        return GpaVO.builder()
                .studentId(studentId)
                .semester(semester)
                .gpa(gpa)
                .totalCredits(totalCredits)
                .courseCount(scores.size())
                .build();
    }

    /**
     * 锁定成绩
     */
    @Transactional(rollbackFor = Exception.class)
    public void lockScores(Long offeringId) {
        List<Score> scores = scoreMapper.selectByOffering(offeringId);
        for (Score score : scores) {
            score.setIsLocked(1);
            score.setLockTime(LocalDateTime.now());
            scoreMapper.updateById(score);
        }
    }

    /**
     * 分页查询成绩
     */
    public Page<ScoreVO> pageScores(ScoreQueryDTO query) {
        Page<Score> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<Score> wrapper = new LambdaQueryWrapper<>();
        
        if (query.getStudentId() != null) {
            wrapper.eq(Score::getStudentId, query.getStudentId());
        }
        if (query.getCourseId() != null) {
            wrapper.eq(Score::getCourseId, query.getCourseId());
        }
        if (query.getSemester() != null) {
            wrapper.eq(Score::getSemester, query.getSemester());
        }
        if (query.getOfferingId() != null) {
            wrapper.eq(Score::getOfferingId, query.getOfferingId());
        }
        
        wrapper.orderByDesc(Score::getCreatedTime);
        Page<Score> resultPage = scoreMapper.selectPage(page, wrapper);
        
        // 转换为VO
        Page<ScoreVO> voPage = new Page<>();
        BeanUtils.copyProperties(resultPage, voPage, "records");
        voPage.setRecords(resultPage.getRecords().stream().map(s -> {
            ScoreVO vo = new ScoreVO();
            BeanUtils.copyProperties(s, vo);
            return vo;
        }).collect(java.util.stream.Collectors.toList()));
        
        return voPage;
    }
}
