package edu.whut.bear.gather.service;

import edu.whut.bear.gather.pojo.User;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Spring-_-Bear
 * @datetime 6/6/2022 1:07 PM
 */
@Service
public interface UserService {
    /**
     * 验证用户名密码正确性
     *
     * @param username 用户名
     * @param password 密码
     * @return User or null
     */
    User verifyUsernameAndPassword(String username, String password);

    /**
     * 更新用户密码
     *
     * @param newPassword 新密码
     * @param id          用户 ID
     * @return 1 - 修改成功
     */
    boolean updateUserPassword(String newPassword, Integer id);

    /**
     * 更新用户上次记录创建时间
     *
     * @param newDate 新的时间
     * @param userId  用户 ID
     * @return true - 更新成功
     */
    boolean updateRecordCreateDate(Date newDate, Integer userId);
}
