package cn.edu.whut.springbear.gather.mapper;

import cn.edu.whut.springbear.gather.pojo.Grade;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
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
    @Select("select class_id from r_grade_class where grade_id = #{gradeId}")
    List<Integer> getClassesOfGrade(@Param("gradeId") Integer gradeId);

    /**
     * Save grade
     */
    @Insert("insert into t_grade(grade) values (#{grade})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int saveGrade(Grade grade);

    /**
     * Save the correspondence between schools and grades
     */
    @Insert("insert into r_school_grade(school_id, grade_id) values (#{schoolId},#{gradeId})")
    int saveSchoolGrade(@Param("schoolId") Integer schoolId, @Param("gradeId") Integer gradeId);

    /**
     * Get the grade info from the school and grade relation table,
     * to verify the existences of current grade about the school
     */
    @Select("select t_grade.* from t_grade, r_school_grade where id = grade_id and grade = #{gradeName} and school_id = #{schoolId}")
    Grade getGradeOfSchool(@Param("gradeName") String gradeName, @Param("schoolId") Integer schoolId);

    /**
     * Get all grade list of the school
     */
    @Select("select * from t_grade where id in (select grade_id from r_school_grade WHERE school_id = #{schoolId})")
    List<Grade> getGradesOfSchool(@Param("schoolId") Integer schoolId);
}
