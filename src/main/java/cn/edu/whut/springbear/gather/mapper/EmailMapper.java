package cn.edu.whut.springbear.gather.mapper;

import cn.edu.whut.springbear.gather.pojo.Email;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-11 01:37 Thursday
 */
@Repository
public interface EmailMapper {
    /**
     * Save the email verify code send log
     */
    @Insert("insert into log_email(email, code, delivery_datetime, user_id) values (#{email},#{code},#{deliveryDatetime},#{userId})")
    int saveEmailLog(Email email);
}
