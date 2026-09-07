package com.snvec.student.modules.attendance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.snvec.student.modules.attendance.entity.Leave;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 请假Mapper
 */
@Mapper
public interface LeaveMapper extends BaseMapper<Leave> {

    /**
     * 查询辅导员待审批列表
     */
    List<Leave> selectCounselorPending(@Param("counselorId") Long counselorId);

    /**
     * 查询学生请假记录
     */
    List<Leave> selectByStudent(@Param("studentId") Long studentId);
}
