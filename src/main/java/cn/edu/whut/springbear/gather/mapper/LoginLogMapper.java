package cn.edu.whut.springbear.gather.mapper;

import cn.edu.whut.springbear.gather.pojo.LoginLog;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-11 00:08 Thursday
 */
@Repository
public interface LoginLogMapper {
    /**
     * Save the login log of user
     */
    @Insert("insert into log_login(ip, location, login_datetime, user_id) values (#{ip},#{location},#{loginDatetime},#{userId})")
    int saveLoginLog(LoginLog loginLog);
}