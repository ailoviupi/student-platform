package com.snvec.student.modules.attendance.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snvec.student.common.exception.BusinessException;
import com.snvec.student.common.result.ResultCode;
import com.snvec.student.modules.attendance.dto.LeaveApplyDTO;
import com.snvec.student.modules.attendance.dto.LeaveApprovalDTO;
import com.snvec.student.modules.attendance.dto.LeaveQueryDTO;
import com.snvec.student.modules.attendance.dto.LeaveVO;
import com.snvec.student.modules.attendance.entity.Leave;
import com.snvec.student.modules.attendance.mapper.LeaveMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * 请假服务
 * 
 * 审批规则:
 * - 事假≤1天: 辅导员批
 * - 事假1-3天: 院系批
 * - 事假>3天: 学工部批
 * - 病假(需医院证明): 3天内辅导员批
 * - 公假(组织证明): 备案
 * 
 * 限制:
 * - 学期事假≤30天
 * - 考试周不批事假
 * - 旷课按条例处理
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LeaveService {

    private final LeaveMapper leaveMapper;

    /**
     * 申请请假
     */
    @Transactional(rollbackFor = Exception.class)
    public void applyLeave(LeaveApplyDTO dto, Long studentId) {
        Leave leave = new Leave();
        leave.setStudentId(studentId);
        leave.setStudentNo(dto.getStudentNo());
        leave.setLeaveType(dto.getLeaveType());
        leave.setStartTime(dto.getStartTime());
        leave.setEndTime(dto.getEndTime());
        leave.setReason(dto.getReason());
        leave.setAttachments(dto.getAttachments());
        leave.setCreatedBy(studentId);

        // 计算天数
        BigDecimal days = calculateDays(dto.getStartTime(), dto.getEndTime());
        leave.setDays(days);

        // 设置审批层级
        int level = determineApprovalLevel(dto.getLeaveType(), days);
        leave.setCurrentLevel(level);
        leave.setStatus(0); // 待审批

        // 校验规则
        validateLeaveRules(leave);

        leaveMapper.insert(leave);
    }

    /**
     * 计算请假天数
     */
    private BigDecimal calculateDays(LocalDateTime start, LocalDateTime end) {
        long hours = ChronoUnit.HOURS.between(start, end);
        // 按天计算，不足半天按半天算，超过半天按一天算
        BigDecimal days = BigDecimal.valueOf(hours).divide(BigDecimal.valueOf(24), 1, BigDecimal.ROUND_HALF_UP);
        return days.max(BigDecimal.valueOf(0.5)); // 最少0.5天
    }

    /**
     * 确定审批层级
     */
    private int determineApprovalLevel(com.snvec.student.common.enums.LeaveTypeEnum leaveType, BigDecimal days) {
        // 公假直接备案
        if (leaveType == com.snvec.student.common.enums.LeaveTypeEnum.PUBLIC) {
            return 1; // 辅导员备案即可
        }
        
        // 病假3天内辅导员批
        if (leaveType == com.snvec.student.common.enums.LeaveTypeEnum.SICK) {
            if (days.compareTo(new BigDecimal("3")) <= 0) {
                return 1; // 辅导员
            }
            return 2; // 院系
        }
        
        // 事假
        if (days.compareTo(BigDecimal.ONE) <= 0) {
            return 1; // 辅导员
        } else if (days.compareTo(new BigDecimal("3")) <= 0) {
            return 2; // 院系
        } else {
            return 3; // 学工部
        }
    }

    /**
     * 校验请假规则
     */
    private void validateLeaveRules(Leave leave) {
        // 事假学期不超过30天
        if (leave.getLeaveType() == com.snvec.student.common.enums.LeaveTypeEnum.PERSONAL) {
            // 查询本学期已请假天数
            BigDecimal usedDays = getSemesterPersonalLeaveDays(leave.getStudentId());
            if (usedDays.add(leave.getDays()).compareTo(new BigDecimal("30")) > 0) {
                throw new BusinessException(ResultCode.LEAVE_DAYS_EXCEEDED);
            }
        }
    }

    /**
     * 获取本学期事假已用天数
     */
    private BigDecimal getSemesterPersonalLeaveDays(Long studentId) {
        // 简化实现，实际应查询数据库
        return BigDecimal.ZERO;
    }

    /**
     * 审批请假
     */
    @Transactional(rollbackFor = Exception.class)
    public void approveLeave(LeaveApprovalDTO dto, Long approverId) {
        Leave leave = leaveMapper.selectById(dto.getLeaveId());
        if (leave == null) {
            throw new BusinessException(ResultCode.LEAVE_NOT_EXIST);
        }

        if (leave.getStatus() != 0) {
            throw new BusinessException(ResultCode.LEAVE_APPROVAL_FAILED);
        }

        int currentLevel = leave.getCurrentLevel();
        boolean approved = dto.getApproved();
        String remark = dto.getRemark();

        if (!approved) {
            // 驳回
            leave.setStatus(2);
            setAuditInfo(leave, currentLevel, approverId, remark);
            leaveMapper.updateById(leave);
            return;
        }

        // 根据当前层级处理
        switch (currentLevel) {
            case 1: // 辅导员审批
                if (leave.getDays().compareTo(new BigDecimal("3")) <= 0 && 
                    leave.getLeaveType() != com.snvec.student.common.enums.LeaveTypeEnum.PERSONAL) {
                    // 直接通过
                    leave.setStatus(1);
                } else if (leave.getDays().compareTo(BigDecimal.ONE) <= 0 && 
                          leave.getLeaveType() == com.snvec.student.common.enums.LeaveTypeEnum.PERSONAL) {
                    // 事假≤1天，辅导员直接通过
                    leave.setStatus(1);
                } else {
                    // 需要下一级审批
                    leave.setCurrentLevel(2);
                }
                setAuditInfo(leave, 1, approverId, remark);
                break;
            case 2: // 院系审批
                if (leave.getDays().compareTo(new BigDecimal("3")) <= 0) {
                    leave.setStatus(1);
                } else {
                    leave.setCurrentLevel(3);
                }
                setAuditInfo(leave, 2, approverId, remark);
                break;
            case 3: // 学工部审批
                leave.setStatus(1);
                setAuditInfo(leave, 3, approverId, remark);
                break;
        }

        leaveMapper.updateById(leave);
    }

    private void setAuditInfo(Leave leave, int level, Long approverId, String remark) {
        LocalDateTime now = LocalDateTime.now();
        switch (level) {
            case 1:
                leave.setCounselorId(approverId);
                leave.setCounselorAuditTime(now);
                leave.setCounselorRemark(remark);
                break;
            case 2:
                leave.setDeptAdminId(approverId);
                leave.setDeptAuditTime(now);
                leave.setDeptRemark(remark);
                break;
            case 3:
                leave.setStudentAdminId(approverId);
                leave.setStudentAuditTime(now);
                leave.setStudentRemark(remark);
                break;
        }
    }

    /**
     * 分页查询请假记录
     */
    public Page<LeaveVO> pageLeaves(LeaveQueryDTO query) {
        Page<Leave> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<Leave> wrapper = new LambdaQueryWrapper<>();
        
        if (query.getStudentId() != null) {
            wrapper.eq(Leave::getStudentId, query.getStudentId());
        }
        if (query.getStatus() != null) {
            wrapper.eq(Leave::getStatus, query.getStatus());
        }
        if (query.getLeaveType() != null) {
            wrapper.eq(Leave::getLeaveType, query.getLeaveType());
        }
        if (query.getCounselorId() != null) {
            wrapper.eq(Leave::getCounselorId, query.getCounselorId());
        }
        
        wrapper.orderByDesc(Leave::getCreatedTime);
        Page<Leave> resultPage = leaveMapper.selectPage(page, wrapper);
        
        Page<LeaveVO> voPage = new Page<>();
        BeanUtils.copyProperties(resultPage, voPage, "records");
        voPage.setRecords(resultPage.getRecords().stream().map(l -> {
            LeaveVO vo = new LeaveVO();
            BeanUtils.copyProperties(l, vo);
            return vo;
        }).collect(java.util.stream.Collectors.toList()));
        
        return voPage;
    }
}
