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
     * Get the all class number ids of current grade
     */
    @Select("select class_id from r_grade_class where grade_id = #{gradeId}")
    List<Integer> listClassIdsOfGrade(@Param("gradeId") Integer gradeId);

    /**
     * Save grade
     */
    @Insert("insert into t_grade(grade) values (#{grade})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int saveGrade(Grade grade);

    /**
     * Save the correspondence between school and grade
     */
    @Insert("insert into r_school_grade(school_id, grade_id) values (#{schoolId},#{gradeId})")
    int saveSchoolGrade(@Param("schoolId") Integer schoolId, @Param("gradeId") Integer gradeId);

    /**
     * Get the grade information from the school and grade relation table
     */
    @Select("select t_grade.* from t_grade, r_school_grade where grade = #{gradeName} and id = grade_id and school_id = #{schoolId}")
    Grade getGradeOfSchool(@Param("gradeName") String gradeName, @Param("schoolId") Integer schoolId);

    /**
     * Get all grades of the school
     */
    @Select("select * from t_grade where id in (select grade_id from r_school_grade WHERE school_id = #{schoolId})")
    List<Grade> listGradesOfSchool(@Param("schoolId") Integer schoolId);

    /**
     * Get grade name by grade id
     */
    @Select("select grade from t_grade where id = #{gradeId}")
    String getGradeNameById(@Param("gradeId") Integer gradeId);
}
