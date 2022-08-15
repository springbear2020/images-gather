package cn.edu.whut.springbear.gather.mapper;

import cn.edu.whut.springbear.gather.pojo.Class;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-13 08:58 Saturday
 */
@Repository
public interface ClassMapper {
    /**
     * Get the people name list who not sign in the system at specified date
     */
    @Select("select t_people.name from t_people,t_user where class_id = #{classId} " +
            "and user_id = t_user.id and user_type <= #{userType} " +
            "and user_id not in (select id from t_user where DATE_FORMAT(last_login_datetime,'%Y-%m-%d') = #{specifiedDate,jdbcType=DATE})")
    List<String> getNotLoginNamesOfClass(@Param("classId") Integer classId, @Param("specifiedDate") Date specifiedDate, @Param("userType") Integer userType);

    /**
     * Get the people name list who sign in the system successfully,
     * filter the result by the upload status and upload datetime
     * Attention: only the student and monitor have the upload record
     */
    @Select("select t_people.name from t_people where class_id = #{classId} " +
            "and user_id in (select user_id from t_upload where upload_status = #{uploadStatus} and DATE_FORMAT(upload_datetime,'%Y-%m-%d') = #{specifiedDate,jdbcType=DATE})")
    List<String> getNamesOfClassByUploadStatus(@Param("classId") Integer classId, @Param("specifiedDate") Date specifiedDate, @Param("uploadStatus") Integer uploadStatus);

    /**
     * Get class information by class id
     */
    @Select("select * from t_class where id = #{classId}")
    Class getClassById(@Param("classId") Integer classId);

    /**
     * Save class
     */
    @Insert("insert into t_class(class_name) values (#{className})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    int saveClass(Class aClass);

    /**
     * Save the correspondence between grades and classes
     */
    @Insert("insert into r_grade_class(grade_id, class_id) values (#{gradeId},#{classId})")
    int saveGradeClass(@Param("gradeId") Integer gradeId, @Param("classId") Integer classId);

    /**
     * Get the class info from the grade and class relation table,
     * to verify the existences of current class about the grade
     */
    @Select("select t_class.* from t_class, r_grade_class where id = class_id and class_name = #{className} and grade_id = #{gradeId}")
    Class getClassOfGrade(@Param("className") String className, @Param("gradeId") Integer gradeId);

    /**
     * Get all classes list of the grade
     */
    @Select("select * from t_class where id in (select class_id  from r_grade_class WHERE grade_id = #{gradeId})")
    List<Class> getClassesOfSchool(@Param("gradeId") Integer gradeId);
}
