package cn.edu.whut.springbear.gather.mapper;

import cn.edu.whut.springbear.gather.pojo.Upload;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-11 00:55 Thursday
 */
@Repository
public interface UploadMapper {
    /**
     * Save upload
     */
    @Insert("insert into t_upload(upload_status, upload_datetime, local_health_url, local_schedule_url, local_closed_url, cloud_health_url, cloud_schedule_url, cloud_closed_url, user_id, create_datetime) " +
            "values (#{uploadStatus},#{uploadDatetime},#{localHealthUrl},#{localScheduleUrl},#{localClosedUrl},#{cloudHealthUrl},#{cloudScheduleUrl},#{cloudClosedUrl},#{userId},#{createDatetime})")
    int saveUpload(Upload upload);

    /**
     * Get the upload of the user at the specified date,
     * filter the result by the upload status
     */
    @Select("select * from t_upload where user_id = #{userId} and upload_status = #{uploadStatus} and DATE_FORMAT(upload_datetime,'%Y-%m-%d') = #{uploadDatetime,jdbcType=DATE}")
    Upload getUpload(@Param("userId") Integer userId, @Param("uploadStatus") Integer uploadStatus, @Param("uploadDatetime") Date uploadDatetime);

    /**
     * Update upload if the field is not null
     */
    int updateUpload(Upload upload);

    /**
     * Get the all upload of the user,
     * filter the result by upload status
     */
    @Select("select * from t_upload where user_id = #{userId} and upload_status = #{uploadStatus} order by upload_datetime desc")
    List<Upload> listUploadsOfUser(@Param("userId") Integer userId, @Param("uploadStatus") Integer uploadStatus);

    /**
     * Get the uploads of class contains the relevant user real name at specified date
     */
    @Select("select t_upload.*, t_user.name from t_upload, t_user where class_id = #{classId} and t_user.id = t_upload.user_id and upload_status = #{uploadStatus} and DATE_FORMAT(upload_datetime,'%Y-%m-%d') = #{uploadDatetime,jdbcType=DATE}")
    List<Upload> listUploadsOfClassWithName(@Param("classId") Integer classId, @Param("uploadStatus") Integer uploadStatus, @Param("uploadDatetime") Date date);
}
