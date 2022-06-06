package edu.whut.bear.gather.service;

import edu.whut.bear.gather.pojo.Login;
import edu.whut.bear.gather.pojo.Record;
import org.springframework.stereotype.Service;

/**
 * @author Spring-_-Bear
 * @datetime 6/6/2022 8:11 PM
 */
@Service
public interface RecordService {
    /**
     * 保存用户登录记录
     *
     * @param login 登录记录
     * @return true - 保存成功
     */
    boolean saveLogin(Login login);

    /**
     * 保存用户记录（每日一条）
     *
     * @param record Record
     * @return true - 保存成功
     */
    boolean saveRecord(Record record);
}
