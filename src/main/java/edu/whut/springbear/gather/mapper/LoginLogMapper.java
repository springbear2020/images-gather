package edu.whut.springbear.gather.mapper;

import edu.whut.springbear.gather.pojo.LoginLog;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

/**
 * @author Spring-_-Bear
 * @datetime 2022-06-30 21:33 Thursday
 */
@Repository
public interface LoginLogMapper {
    @Insert("insert into log_login(ip, location, login_date_time, user_id) values (#{ip},#{location},#{loginDateTime},#{userId})")
    int saveLoginLog(LoginLog loginLog);
}
