package cn.edu.whut.springbear.gather.mapper;

import cn.edu.whut.springbear.gather.pojo.EmailLog;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-11 01:37 Thursday
 */
@Repository
public interface EmailLogMapper {
    /**
     * Save the verify code sent record
     */
    @Insert("insert into log_email(email, code, delivery_datetime, user_id) values (#{email},#{code},#{deliveryDatetime},#{userId})")
    int saveEmailLog(EmailLog emailLog);
}
