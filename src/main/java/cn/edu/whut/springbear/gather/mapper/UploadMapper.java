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
     * Save upload of user
     */
    @Insert("insert into t_upload(upload_status,upload_datetime,user_id) values (#{uploadStatus},#{uploadDatetime},#{userId})")
    int saveUpload(Upload upload);

    /**
     * Get the upload record of the user by id of user at the specified date,
     * and filter the results by the upload record status
     */
    @Select("select * from t_upload where user_id = #{userId} and upload_status = #{uploadStatus} and DATE_FORMAT(upload_datetime,'%Y-%m-%d') = #{date,jdbcType=DATE}")
    Upload getUploadOfUser(@Param("userId") Integer userId, @Param("uploadStatus") Integer uploadStatus, @Param("date") Date date);

    /**
     * Update the upload record of user in specified user,
     * including uploadStatus, uploadDateTime, and three images' local and cloud access url
     */
    int updateUploadImagesUrl(Upload upload);

    /**
     * Get the user's all upload record
     */
    @Select("select * from t_upload where user_id = #{userId} and upload_status = #{uploadStatus} order by upload_datetime desc")
    List<Upload> getUserUploads(@Param("userId") Integer userId, @Param("uploadStatus") Integer uploadStatus);

    /**
     * Get the upload list of class contains the relevant people name at specified date
     * Attention: only the student and monitor have the upload record
     */
    @Select("select t_upload.*, t_people.name from t_upload, t_people where class_id = #{classId}  " +
            "and t_people.user_id = t_upload.user_id " +
            "and upload_status = #{uploadStatus} and DATE_FORMAT(upload_datetime,'%Y-%m-%d') = #{date,jdbcType=DATE}")
    List<Upload> getUploadsOfClassWithName(@Param("classId") Integer classId, @Param("uploadStatus") Integer uploadStatus, @Param("date") Date date);
}
