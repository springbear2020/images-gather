package cn.edu.whut.springbear.gather.mapper;

import cn.edu.whut.springbear.gather.pojo.Student;
import cn.edu.whut.springbear.gather.pojo.Upload;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-08 22:52 Monday
 */
@Repository
public interface UploadMapper {
    /**
     * Save the upload record of user
     */
    @Insert("insert into t_upload(upload_status, upload_datetime, local_health_url, local_schedule_url, local_closed_url, cloud_health_url, cloud_schedule_url, cloud_closed_url, user_id) " +
            "values (#{uploadStatus},#{uploadDatetime},#{localHealthUrl},#{localScheduleUrl},#{localClosedUrl},#{cloudHealthUrl},#{cloudScheduleUrl},#{cloudClosedUrl},#{userId})")
    int saveUpload(Upload upload);

    /**
     * Update the upload record of user in specified user,
     * including uploadStatus, uploadDateTime, and three images' local and cloud access url
     */
    @Update("update t_upload set upload_status = #{uploadStatus}, upload_datetime = #{uploadDatetime}," +
            "local_health_url = #{localHealthUrl}, local_schedule_url = #{localScheduleUrl}, local_closed_url = #{localClosedUrl}, " +
            "cloud_health_url = #{cloudHealthUrl}, cloud_schedule_url = #{cloudScheduleUrl}, cloud_closed_url = #{cloudClosedUrl} " +
            "where user_id = #{userId} and DATE_FORMAT(upload_datetime,'%Y-%m-%d') = #{uploadDatetime,jdbcType=DATE}")
    int updateUserUploadImagesUrl(Upload upload);

    /**
     * Get the user's all upload record
     */
    @Select("select * from t_upload where user_id = #{userId} and upload_status = #{uploadStatus} order by upload_datetime desc")
    List<Upload> getUserAllUploads(@Param("userId") Integer userId, @Param("uploadStatus") Integer uploadStatus);

    /**
     * Get the upload record of the user by id of user in the specified date,
     * and filter the results by the upload record status
     */
    @Select("select * from t_upload where user_id = #{userId} and upload_status = #{uploadStatus} and DATE_FORMAT(upload_datetime,'%Y-%m-%d') = #{specifiedDate,jdbcType=DATE}")
    Upload getUserUploadAtSpecifiedDate(@Param("userId") Integer userId, @Param("uploadStatus") Integer uploadStatus, @Param("specifiedDate") Date specifiedDate);

    /**
     * Get the upload list of the class with student information at the specified date who signed in the system successfully,
     * filtered the results set by the status of the upload record
     */
    @Select("select t_upload.* from t_upload where t_upload.upload_status = #{uploadStatus} and DATE_FORMAT(upload_datetime,'%Y-%m-%d') = DATE_FORMAT(#{specifiedDate},'%Y-%m-%d') " +
            "and t_upload.user_id in (select t_student.user_id from t_student where class_name = #{className}) order by upload_datetime desc")
    @Results({
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "student", column = "user_id",
                    javaType = Student.class, one = @One(select = "cn.edu.whut.springbear.gather.mapper.StudentMapper.getStudentByUserId"))
    })
    List<Upload> getClassUploadListWithStudent(@Param("uploadStatus") Integer uploadStatus, @Param("specifiedDate") Date specifiedDate, @Param("className") String className);
}
