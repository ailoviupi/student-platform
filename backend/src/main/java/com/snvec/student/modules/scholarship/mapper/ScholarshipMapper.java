package com.snvec.student.modules.scholarship.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.snvec.student.modules.scholarship.entity.ScholarshipApply;
import org.apache.ibatis.annotations.Mapper;

/**
 * 奖学金Mapper
 */
@Mapper
public interface ScholarshipMapper extends BaseMapper<ScholarshipApply> {
}
