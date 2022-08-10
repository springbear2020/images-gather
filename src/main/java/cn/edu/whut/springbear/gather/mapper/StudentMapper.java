package cn.edu.whut.springbear.gather.mapper;

import cn.edu.whut.springbear.gather.pojo.Student;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-08 16:24 Monday
 */
@Repository
public interface StudentMapper {
    /**
     * Get student by user id
     */
    @Select("select * from t_student where user_id = #{userId}")
    Student getStudentByUserId(@Param("userId") Integer userId);

    /**
     * Update student info, including sex,phone number and email address
     */
    @Update("update t_student set sex = #{newSex}, phone = #{newPhone}, email = #{newEmail} where id = #{stuId}")
    int updateStudentInfo(@Param("newSex") String newSex, @Param("newPhone") String newPhone, @Param("newEmail") String newEmail, @Param("stuId") Integer stuId);

    /**
     * Get the student list of the class at the specified date who not sign in the system,
     * someone not sign in the system meaning his/her upload record was not created
     */
    @Select("select * from t_student where class_name = #{className} and " +
            "user_id not in (select user_id from t_upload where DATE_FORMAT(upload_datetime,'%Y-%m-%d') = DATE_FORMAT(#{specifiedDate},'%Y-%m-%d'))")
    List<Student> getClassStudentListNotSignIn(@Param("className") String className, @Param("specifiedDate") Date specifiedDate);

    /**
     * Get the student list of the class at the specified date who signed in the system successfully,
     * filtered the results set by the status of the upload record
     */
    @Select("select * from t_student where class_name = #{className} and user_id in (select user_id from t_upload where upload_status = #{uploadStatus} and DATE_FORMAT(upload_datetime,'%Y-%m-%d') = DATE_FORMAT(#{specifiedDate},'%Y-%m-%d'))")
    List<Student> getClassStudentListByUploadStatus(@Param("uploadStatus") Integer uploadStatus, @Param("className") String className, @Param("specifiedDate") Date specifiedDate);

    /**
     * Get the student name list of the class at the specified date who not sign in the system,
     * someone not sign in the system meaning his/her upload record was not created
     */
    @Select("select t_student.name from t_student where class_name = #{className} and " +
            "user_id not in (select user_id from t_upload where DATE_FORMAT(upload_datetime,'%Y-%m-%d') = DATE_FORMAT(#{specifiedDate},'%Y-%m-%d'))")
    List<String> getClassStudentNameListNotLogin(@Param("className") String className, @Param("specifiedDate") Date specifiedDate);

    /**
     * Get the student name list of the class at the specified date who signed in the system successfully,
     * filtered the results set by the status of the upload record
     */
    @Select("select t_student.name from t_student where class_name = #{className} and user_id in (select user_id from t_upload where upload_status = #{uploadStatus} and DATE_FORMAT(upload_datetime,'%Y-%m-%d') = DATE_FORMAT(#{specifiedDate},'%Y-%m-%d'))")
    List<String> getClassStudentNameListByUploadStatus(@Param("uploadStatus") Integer uploadStatus, @Param("className") String className, @Param("specifiedDate") Date specifiedDate);
}
