package edu.whut.springbear.gather.mapper;

import edu.whut.springbear.gather.pojo.Student;
import edu.whut.springbear.gather.pojo.Upload;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


/**
 * @author Spring-_-Bear
 * @datetime 2022-06-30 22:37 Thursday
 */
@Repository
public interface UploadMapper {
    @Insert("insert into t_upload(upload_status, upload_date_time, local_health_url, local_schedule_url, local_closed_url, cloud_health_url, cloud_schedule_url, cloud_closed_url, user_id) " +
            "values (#{uploadStatus},#{uploadDateTime},#{localHealthUrl},#{localScheduleUrl},#{localClosedUrl},#{cloudHealthUrl},#{cloudScheduleUrl},#{cloudClosedUrl},#{userId})")
    int saveUpload(Upload upload);

    /**
     * Update the upload record of user in specified user,
     * including uploadStatus, uploadDateTime, and three images' local and cloud access url
     *
     * @param upload Upload record
     * @return 1 - Update successfully
     */
    @Update("update t_upload set upload_status = #{uploadStatus}, upload_date_time = #{uploadDateTime}," +
            "local_health_url = #{localHealthUrl}, local_schedule_url = #{localScheduleUrl}, local_closed_url = #{localClosedUrl}, " +
            "cloud_health_url = #{cloudHealthUrl}, cloud_schedule_url = #{cloudScheduleUrl}, cloud_closed_url = #{cloudClosedUrl} " +
            "where user_id = #{userId} and DATE_FORMAT(upload_date_time,'%Y-%m-%d') = #{uploadDateTime,jdbcType=DATE}")
    int updateUserUploadImagesUrl(Upload upload);

    /**
     * Get the upload record of the user by id of user in the specified date,
     * and filter the results by the upload record status
     *
     * @param userId        Id of user
     * @param uploadStatus  Status of upload
     * @param specifiedDate Specified date
     * @return Upload or null
     */
    @Select("select * from t_upload where user_id = #{userId} and upload_status = #{uploadStatus} and DATE_FORMAT(upload_date_time,'%Y-%m-%d') = #{specifiedDate,jdbcType=DATE}")
    Upload getUserUploadAtSpecifiedDate(@Param("userId") Integer userId, @Param("uploadStatus") Integer uploadStatus, @Param("specifiedDate") Date specifiedDate);

    /**
     * Get the user's all upload record
     *
     * @param userId       Id of user
     * @param uploadStatus Status of upload
     * @return User upload list or null
     */
    @Select("select * from t_upload where user_id = #{userId} and upload_status = #{uploadStatus} order by upload_date_time desc")
    List<Upload> getUserAllUploads(@Param("userId") Integer userId, @Param("uploadStatus") Integer uploadStatus);

    /**
     * Get the upload list of the class with student information at the specified date who signed in the system successfully,
     * filtered the results set by the status of the upload record
     *
     * @param uploadStatus  Status of the upload record
     * @param className     Name of class
     * @param specifiedDate SpecifiedDate
     * @return Upload list with student or null
     */
    @Select("select t_upload.* from t_upload where t_upload.upload_status = #{uploadStatus} and DATE_FORMAT(upload_date_time,'%Y-%m-%d') = DATE_FORMAT(#{specifiedDate},'%Y-%m-%d') " +
            "and t_upload.user_id in (select t_student.user_id from t_student where class_name = #{className}) order by upload_date_time desc")
    @Results({
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "student", column = "user_id",
                    javaType = Student.class, one = @One(select = "edu.whut.springbear.gather.mapper.StudentMapper.getStudentByUserId"))
    })
    List<Upload> getClassUploadListWithStudentByStatusOnSpecifiedDay(@Param("uploadStatus") Integer uploadStatus, @Param("specifiedDate") Date specifiedDate, @Param("className") String className);
}