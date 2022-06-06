package edu.whut.bear.gather.service;

import edu.whut.bear.gather.pojo.User;
import org.springframework.stereotype.Service;

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
}
