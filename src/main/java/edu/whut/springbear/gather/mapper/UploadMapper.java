package edu.whut.springbear.gather.mapper;

import edu.whut.springbear.gather.pojo.Upload;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;


/**
 * @author Spring-_-Bear
 * @datetime 2022-06-30 22:37 Thursday
 */
@Repository
public interface UploadMapper {
    @Insert("insert into t_upload(upload_status, upload_date_time, local_health_url, local_schedule_url, local_closed_url, cloud_health_url, cloud_schedule_url, cloud_closed_url, user_id) " +
            "VALUES(#{uploadStatus},#{uploadDatetime},#{localHealthUrl},#{localScheduleUrl},#{localClosedUrl},#{cloudHealthUrl},#{cloudScheduleUrl},#{cloudClosedUrl},#{userId})")
    int saveUpload(Upload upload);


    /**
     * Update the upload record of user in specified user,
     * including uploadStatus, uploadDateTime, and three images' local and cloud access url
     *
     * @param upload Upload record
     * @return 1 - Update successfully
     */
    @Update("update t_upload set upload_status = #{uploadStatus}, upload_date_time = #{uploadDatetime}," +
            "local_health_url = #{localHealthUrl}, local_schedule_url = #{localScheduleUrl}, local_closed_url = #{localClosedUrl}, " +
            "cloud_health_url = #{cloudHealthUrl}, cloud_schedule_url = #{cloudScheduleUrl}, cloud_closed_url = #{cloudClosedUrl} " +
            "where user_id = #{userId} and DATE_FORMAT(upload_date_time,'%Y-%m-%d') = #{uploadDatetime,jdbcType=DATE}")
    int updateUserUploadImagesUrl(Upload upload);
}
