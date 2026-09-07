package com.snvec.student.modules.dormitory.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snvec.student.common.exception.BusinessException;
import com.snvec.student.common.result.ResultCode;
import com.snvec.student.modules.dormitory.dto.RepairApplyDTO;
import com.snvec.student.modules.dormitory.dto.RepairQueryDTO;
import com.snvec.student.modules.dormitory.dto.RepairRatingDTO;
import com.snvec.student.modules.dormitory.dto.RepairVO;
import com.snvec.student.modules.dormitory.entity.Repair;
import com.snvec.student.modules.dormitory.mapper.RepairMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 报修服务
 * 紧急报修30分钟响应
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RepairService {

    private final RepairMapper repairMapper;

    /**
     * 提交报修
     */
    @Transactional(rollbackFor = Exception.class)
    public void applyRepair(RepairApplyDTO dto, Long studentId) {
        Repair repair = new Repair();
        BeanUtils.copyProperties(dto, repair);
        repair.setStudentId(studentId);
        repair.setStatus(0); // 待派单
        repair.setCreatedBy(studentId);
        repairMapper.insert(repair);
    }

    /**
     * 派单
     */
    @Transactional(rollbackFor = Exception.class)
    public void dispatch(Long repairId, Long workerId, String workerName, String workerPhone) {
        Repair repair = repairMapper.selectById(repairId);
        if (repair == null) throw new BusinessException(ResultCode.REPAIR_NOT_EXIST);
        
        repair.setStatus(1);
        repair.setWorkerId(workerId);
        repair.setWorkerName(workerName);
        repair.setWorkerPhone(workerPhone);
        repair.setDispatchTime(LocalDateTime.now());
        repairMapper.updateById(repair);
    }

    /**
     * 开始维修
     */
    @Transactional(rollbackFor = Exception.class)
    public void startRepair(Long repairId) {
        Repair repair = repairMapper.selectById(repairId);
        if (repair == null) throw new BusinessException(ResultCode.REPAIR_NOT_EXIST);
        
        repair.setStatus(2);
        repair.setRepairTime(LocalDateTime.now());
        repairMapper.updateById(repair);
    }

    /**
     * 完成维修
     */
    @Transactional(rollbackFor = Exception.class)
    public void finishRepair(Long repairId) {
        Repair repair = repairMapper.selectById(repairId);
        if (repair == null) throw new BusinessException(ResultCode.REPAIR_NOT_EXIST);
        
        repair.setStatus(3);
        repair.setFinishTime(LocalDateTime.now());
        repairMapper.updateById(repair);
    }

    /**
     * 评价
     */
    @Transactional(rollbackFor = Exception.class)
    public void ratingRepair(RepairRatingDTO dto, Long studentId) {
        Repair repair = repairMapper.selectById(dto.getRepairId());
        if (repair == null) throw new BusinessException(ResultCode.REPAIR_NOT_EXIST);
        if (!repair.getStudentId().equals(studentId)) {
            throw new BusinessException("无权评价他人报修");
        }
        
        repair.setStatus(4);
        repair.setRating(dto.getRating());
        repair.setRatingContent(dto.getRatingContent());
        repair.setRatingTime(LocalDateTime.now());
        repairMapper.updateById(repair);
    }

    /**
     * 分页查询
     */
    public Page<RepairVO> pageRepairs(RepairQueryDTO query) {
        Page<Repair> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<Repair> wrapper = new LambdaQueryWrapper<>();
        
        if (query.getStudentId() != null) {
            wrapper.eq(Repair::getStudentId, query.getStudentId());
        }
        if (query.getStatus() != null) {
            wrapper.eq(Repair::getStatus, query.getStatus());
        }
        if (query.getRepairType() != null) {
            wrapper.eq(Repair::getRepairType, query.getRepairType());
        }
        if (query.getIsUrgent() != null) {
            wrapper.eq(Repair::getIsUrgent, query.getIsUrgent());
        }
        
        wrapper.orderByDesc(Repair::getCreatedTime);
        Page<Repair> resultPage = repairMapper.selectPage(page, wrapper);
        
        Page<RepairVO> voPage = new Page<>();
        BeanUtils.copyProperties(resultPage, voPage, "records");
        voPage.setRecords(resultPage.getRecords().stream().map(r -> {
            RepairVO vo = new RepairVO();
            BeanUtils.copyProperties(r, vo);
            return vo;
        }).collect(java.util.stream.Collectors.toList()));
        
        return voPage;
    }
}
