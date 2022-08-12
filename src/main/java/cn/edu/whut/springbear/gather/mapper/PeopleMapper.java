package cn.edu.whut.springbear.gather.mapper;

import cn.edu.whut.springbear.gather.pojo.People;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-11 15:15 Thursday
 */
@Repository
public interface PeopleMapper {
    /**
     * Get people info by the user id
     */
    @Select("select * from t_people where user_id = #{userId}")
    People getPeopleByUserId(@Param("userId") Integer userId);

    /**
     * Update the people info if it not null
     */
    int updatePeople(People people);

    /**
     * Get the people name list who not sign in the system at specified date
     */
    @Select("select t_people.name from t_people,t_user where class_name = #{className} " +
            "and user_id = t_user.id and user_type <= #{userType} " +
            "and user_id not in (select id from t_user where DATE_FORMAT(last_login_datetime,'%Y-%m-%d') = #{specifiedDate,jdbcType=DATE})")
    List<String> getNotLoginNamesOfClass(@Param("className") String className, @Param("specifiedDate") Date specifiedDate, @Param("userType") Integer userType);

    /**
     * Get the people name list who sign in the system successfully,
     * filter the result by the upload status and upload datetime
     * Attention: only the student and monitor have the upload record
     */
    @Select("select t_people.name from t_people where class_name = #{className} " +
            "and user_id in (select user_id from t_upload where upload_status = #{uploadStatus} and DATE_FORMAT(upload_datetime,'%Y-%m-%d') = #{specifiedDate,jdbcType=DATE})")
    List<String> getNamesOfClassByUploadStatus(@Param("className") String className, @Param("specifiedDate") Date specifiedDate, @Param("uploadStatus") Integer uploadStatus);
}
