package edu.whut.bear.gather.dao;

import edu.whut.bear.gather.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Spring-_-Bear
 * @datetime 6/6/2022 12:36 PM
 */
@Repository
public interface UserDao {
    /**
     * 通过用户名和密码查询用户信息
     *
     * @param username 用户名
     * @param password 密码
     * @return User or null
     */
    User getUserByUsernameAndPassword(@Param("username") String username, @Param("password") String password);
}
