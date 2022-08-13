package cn.edu.whut.springbear.gather.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-13 08:53 Saturday
 */
@Repository
public interface GradeMapper {
    /**
     * Get the all class number id of current grade
     */
    @Select("select class_id from t_grade_class where grade_id = #{gradeId}")
    List<Integer> getClassesOfGrade(@Param("gradeId") Integer gradeId);
}
