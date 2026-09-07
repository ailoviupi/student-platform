package com.snvec.student.modules.scholarship.service;
import java.util.Arrays;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snvec.student.common.enums.ScholarshipTypeEnum;
import com.snvec.student.common.exception.BusinessException;
import com.snvec.student.common.result.ResultCode;
import com.snvec.student.modules.scholarship.dto.ScholarshipApplyDTO;
import com.snvec.student.modules.scholarship.dto.ScholarshipQueryDTO;
import com.snvec.student.modules.scholarship.dto.ScholarshipVO;
import com.snvec.student.modules.scholarship.entity.ScholarshipApply;
import com.snvec.student.modules.scholarship.mapper.ScholarshipMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 奖学金服务
 * 
 * 流程: 申请→班级评议→院系公示3天→学院审定→全校公示5天
 * 限制: 处分未解除、欠费、造假不得申请; 国奖与励志不可兼得
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ScholarshipService {

    private final ScholarshipMapper scholarshipMapper;

    /**
     * 申请奖学金
     */
    @Transactional(rollbackFor = Exception.class)
    public void applyScholarship(ScholarshipApplyDTO dto, Long studentId) {
        // 检查是否已申请同类型
        ScholarshipApply existing = scholarshipMapper.selectOne(
            new LambdaQueryWrapper<ScholarshipApply>()
                .eq(ScholarshipApply::getStudentId, studentId)
                .eq(ScholarshipApply::getScholarshipType, dto.getScholarshipType())
                .eq(ScholarshipApply::getAcademicYear, dto.getAcademicYear())
        );
        if (existing != null) {
            throw new BusinessException("本学期已申请过该奖学金");
        }

        // 国奖与励志不可兼得
        if (dto.getScholarshipType() == ScholarshipTypeEnum.NATIONAL || 
            dto.getScholarshipType() == ScholarshipTypeEnum.MOTIVATIONAL) {
            ScholarshipApply conflict = scholarshipMapper.selectOne(
                new LambdaQueryWrapper<ScholarshipApply>()
                    .eq(ScholarshipApply::getStudentId, studentId)
                    .eq(ScholarshipApply::getAcademicYear, dto.getAcademicYear())
                    .in(ScholarshipApply::getScholarshipType, 
                        Arrays.asList(ScholarshipTypeEnum.NATIONAL, ScholarshipTypeEnum.MOTIVATIONAL))
            );
            if (conflict != null) {
                throw new BusinessException(ResultCode.SCHOLARSHIP_CONFLICT);
            }
        }

        // 检查成绩条件 (前30%)
        if (dto.getTotalStudents() != null && dto.getRanking() != null) {
            BigDecimal percent = new BigDecimal(dto.getRanking())
                    .divide(new BigDecimal(dto.getTotalStudents()), 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal("100"));
            if (percent.compareTo(new BigDecimal("30")) > 0) {
                throw new BusinessException(ResultCode.SCHOLARSHIP_NOT_QUALIFIED);
            }
        }

        ScholarshipApply apply = new ScholarshipApply();
        BeanUtils.copyProperties(dto, apply);
        apply.setStudentId(studentId);
        apply.setStatus(0); // 待评议
        apply.setCreatedBy(studentId);
        scholarshipMapper.insert(apply);
    }

    /**
     * 班级评议通过
     */
    @Transactional(rollbackFor = Exception.class)
    public void classReview(Long applyId, boolean approved) {
        ScholarshipApply apply = scholarshipMapper.selectById(applyId);
        if (apply == null) throw new BusinessException(ResultCode.SCHOLARSHIP_NOT_EXIST);
        
        if (approved) {
            apply.setStatus(1);
            apply.setClassReviewTime(LocalDateTime.now());
        } else {
            apply.setStatus(5);
        }
        scholarshipMapper.updateById(apply);
    }

    /**
     * 院系公示
     */
    @Transactional(rollbackFor = Exception.class)
    public void deptPublicity(Long applyId) {
        ScholarshipApply apply = scholarshipMapper.selectById(applyId);
        if (apply == null) throw new BusinessException(ResultCode.SCHOLARSHIP_NOT_EXIST);
        
        apply.setStatus(2);
        apply.setDeptPublicityStart(LocalDateTime.now());
        apply.setDeptReviewTime(LocalDateTime.now());
        scholarshipMapper.updateById(apply);
    }

    /**
     * 学院审定
     */
    @Transactional(rollbackFor = Exception.class)
    public void collegeApprove(Long applyId, boolean approved) {
        ScholarshipApply apply = scholarshipMapper.selectById(applyId);
        if (apply == null) throw new BusinessException(ResultCode.SCHOLARSHIP_NOT_EXIST);
        
        if (approved) {
            apply.setStatus(3);
            apply.setCollegeReviewTime(LocalDateTime.now());
            // 设置金额
            apply.setAmount(apply.getScholarshipType().getAmount());
        } else {
            apply.setStatus(5);
        }
        scholarshipMapper.updateById(apply);
    }

    /**
     * 全校公示
     */
    @Transactional(rollbackFor = Exception.class)
    public void collegePublicity(Long applyId) {
        ScholarshipApply apply = scholarshipMapper.selectById(applyId);
        if (apply == null) throw new BusinessException(ResultCode.SCHOLARSHIP_NOT_EXIST);
        
        apply.setStatus(4);
        apply.setCollegePublicityStart(LocalDateTime.now());
        scholarshipMapper.updateById(apply);
    }

    /**
     * 分页查询
     */
    public Page<ScholarshipVO> pageScholarships(ScholarshipQueryDTO query) {
        Page<ScholarshipApply> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<ScholarshipApply> wrapper = new LambdaQueryWrapper<>();
        
        if (query.getStudentId() != null) {
            wrapper.eq(ScholarshipApply::getStudentId, query.getStudentId());
        }
        if (query.getStatus() != null) {
            wrapper.eq(ScholarshipApply::getStatus, query.getStatus());
        }
        if (query.getScholarshipType() != null) {
            wrapper.eq(ScholarshipApply::getScholarshipType, query.getScholarshipType());
        }
        if (query.getAcademicYear() != null) {
            wrapper.eq(ScholarshipApply::getAcademicYear, query.getAcademicYear());
        }
        
        wrapper.orderByDesc(ScholarshipApply::getCreatedTime);
        Page<ScholarshipApply> resultPage = scholarshipMapper.selectPage(page, wrapper);
        
        Page<ScholarshipVO> voPage = new Page<>();
        BeanUtils.copyProperties(resultPage, voPage, "records");
        voPage.setRecords(resultPage.getRecords().stream().map(s -> {
            ScholarshipVO vo = new ScholarshipVO();
            BeanUtils.copyProperties(s, vo);
            return vo;
        }).collect(java.util.stream.Collectors.toList()));
        
        return voPage;
    }
}
