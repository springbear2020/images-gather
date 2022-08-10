package cn.edu.whut.springbear.gather.mapper;

import cn.edu.whut.springbear.gather.pojo.EmailLog;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-09 15:28 Tuesday
 */
@Repository
public interface EmailLogMapper {
    /**
     * Save the verify code sent record
     */
    @Insert("insert into log_email(email, code, datetime) values (#{email},#{code},#{datetime})")
    int saveEmailLog(EmailLog emailLog);
}
