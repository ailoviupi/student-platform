package com.snvec.student.modules.score.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.snvec.student.modules.score.entity.Score;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 成绩Mapper
 */
@Mapper
public interface ScoreMapper extends BaseMapper<Score> {

    /**
     * 查询学生某学期所有成绩
     */
    List<Score> selectByStudentAndSemester(@Param("studentId") Long studentId, 
                                            @Param("semester") String semester);

    /**
     * 查询某课程所有学生成绩
     */
    List<Score> selectByOffering(@Param("offeringId") Long offeringId);
}
