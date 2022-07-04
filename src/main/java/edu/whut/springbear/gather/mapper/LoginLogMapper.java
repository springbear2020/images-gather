package edu.whut.springbear.gather.mapper;

import edu.whut.springbear.gather.pojo.LoginLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Spring-_-Bear
 * @datetime 2022-06-30 21:33 Thursday
 */
@Repository
public interface LoginLogMapper {
    @Insert("insert into log_login(ip, location, login_date_time, user_id) values (#{ip},#{location},#{loginDateTime},#{userId})")
    int saveLoginLog(LoginLog loginLog);

    @Select("select * from log_login where user_id = #{userId} order by login_date_time desc")
    List<LoginLog> getUserLoginLog(@Param("userId") Integer userId);
}
