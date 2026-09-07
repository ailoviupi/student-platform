package com.snvec.student.modules.notice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.snvec.student.modules.notice.entity.Notice;
import org.apache.ibatis.annotations.Mapper;

/**
 * 通知公告Mapper
 */
@Mapper
public interface NoticeMapper extends BaseMapper<Notice> {
}
