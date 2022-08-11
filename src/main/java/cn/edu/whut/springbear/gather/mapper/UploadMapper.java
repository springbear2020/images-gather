package cn.edu.whut.springbear.gather.mapper;

import cn.edu.whut.springbear.gather.pojo.Upload;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-11 00:55 Thursday
 */
@Repository
public interface UploadMapper {
    /**
     * Save upload of user
     */
    @Insert("insert into t_upload(upload_status, upload_datetime, local_health_url, local_schedule_url, local_closed_url, cloud_health_url, cloud_schedule_url, cloud_closed_url, user_id) " +
            "values (#{uploadStatus},#{uploadDatetime},#{localHealthUrl},#{localScheduleUrl},#{localClosedUrl},#{cloudHealthUrl},#{cloudScheduleUrl},#{cloudClosedUrl},#{userId})")
    int saveUpload(Upload upload);

    /**
     * Get the upload record of the user by id of user at the specified date,
     * and filter the results by the upload record status
     */
    @Select("select * from t_upload where user_id = #{userId} and upload_status = #{uploadStatus} and DATE_FORMAT(upload_datetime,'%Y-%m-%d') = #{date,jdbcType=DATE}")
    Upload getUploadOfUserFilterByStatusAndDate(@Param("userId") Integer userId, @Param("uploadStatus") Integer uploadStatus, @Param("date") Date date);
}
