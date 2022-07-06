package edu.whut.springbear.gather.mapper;

import edu.whut.springbear.gather.pojo.Student;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author Spring-_-Bear
 * @datetime 2022-06-30 15:52 Thursday
 */
@Repository
public interface StudentMapper {
    @Select("select * from t_student where id = #{studentId}")
    Student getStudentById(@Param("studentId") Integer studentId);

    @Insert("insert into t_student(number, name, sex, phone, email, major, class_name, grade, college, school) " +
            "values (#{number},#{name},#{sex},#{phone},#{email},#{major},#{className},#{grade},#{college},#{school})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int saveStudent(Student student);

    @Update("update t_student set user_id = #{userId} where id = #{studentId}")
    int updateStudentUserId(@Param("userId") Integer userId, @Param("studentId") Integer studentId);

    /**
     * Get the student list of the class at the specified date who not sign in the system,
     * someone not sign in the system meaning his/her upload record was not created
     *
     * @param className     Name of the class
     * @param specifiedDate Specified date
     * @return Student list or null
     */
    @Select("select * from t_student where class_name = #{className} and " +
            "user_id not in (select user_id from t_upload where DATE_FORMAT(upload_date_time,'%Y-%m-%d') = DATE_FORMAT(#{specifiedDate},'%Y-%m-%d'))")
    List<Student> getClassStudentListNotSignInOnSpecifiedDay(@Param("className") String className, @Param("specifiedDate") Date specifiedDate);

    /**
     * Get the student list of the class at the specified date who signed in the system successfully,
     * filtered the results set by the status of the upload record
     *
     * @param uploadStatus  Status of the upload record
     * @param className     Name of class
     * @param specifiedDate SpecifiedDate
     * @return Student list or null
     */
    @Select("select * from t_student where class_name = #{className} and user_id in (select user_id from t_upload where upload_status = #{uploadStatus} and DATE_FORMAT(upload_date_time,'%Y-%m-%d') = DATE_FORMAT(#{specifiedDate},'%Y-%m-%d'))")
    List<Student> getClassStudentListOnSpecifiedDayByStatus(@Param("uploadStatus") Integer uploadStatus, @Param("className") String className, @Param("specifiedDate") Date specifiedDate);

    @Select("select * from t_student where user_id = #{userId}")
    Student getStudentByUserId(@Param("userId") Integer userId);
}



