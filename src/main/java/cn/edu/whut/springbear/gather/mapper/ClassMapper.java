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
     * Get the user real name list who not sign in the system at specified date,
     * filter the result list by the user type and last login datetime
     */
    @Select("select name from t_user where class_id = #{classId} and user_type <= #{userType} and DATE_FORMAT(last_login_datetime,'%Y-%m-%d')  != #{lastLoginDatetime, jdbcType=DATE}")
    List<String> listNotLoginNamesOfClass(@Param("classId") Integer classId, @Param("lastLoginDatetime") Date lastLoginDatetime, @Param("userType") Integer userType);

    /**
     * Get the user real name list who sign in the system successfully (meaning his/her upload record is created),
     * filter the result by the upload status and upload datetime
     */
    @Select("select name from t_user where class_id = #{classId} and id in (select user_id from t_upload where upload_status = #{uploadStatus} and DATE_FORMAT(upload_datetime,'%Y-%m-%d') = #{uploadDatetime, jdbcType=DATE})")
    List<String> listUploadNamesOfClass(@Param("classId") Integer classId, @Param("uploadDatetime") Date uploadDatetime, @Param("uploadStatus") Integer uploadStatus);

    /**
     * Get class information by id
     */
    @Select("select * from t_class where id = #{classId}")
    Class getClass(@Param("classId") Integer classId);

    /**
     * Save class
     */
    @Insert("insert into t_class(class_name) values (#{className})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    int saveClass(Class aClass);

    /**
     * Save the correspondence between grade and class
     */
    @Insert("insert into r_grade_class(grade_id, class_id) values (#{gradeId},#{classId})")
    int saveGradeClass(@Param("gradeId") Integer gradeId, @Param("classId") Integer classId);

    /**
     * Get the class information from the grade and class relation table
     */
    @Select("select t_class.* from t_class, r_grade_class where class_name = #{className} and id = class_id and grade_id = #{gradeId}")
    Class getClassOfGrade(@Param("className") String className, @Param("gradeId") Integer gradeId);

    /**
     * Get all classes of the grade
     */
    @Select("select * from t_class where id in (select class_id  from r_grade_class WHERE grade_id = #{gradeId})")
    List<Class> listClassesOfGrade(@Param("gradeId") Integer gradeId);

    /**
     * Get class name by class id
     */
    @Select("select class_name from t_class where id = #{classId}")
    String getClassNameById(@Param("classId") Integer classId);
}
