package edu.whut.bear.gather.dao;

import edu.whut.bear.gather.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

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

    /**
     * 修改用户密码
     *
     * @param newPassword 新密码
     * @param id          用户 ID
     * @return 1 - 修改成功
     */
    int updateUserPasswordById(@Param("newPassword") String newPassword, @Param("id") Integer id);

    /**
     * 更新用户上次记录上传时间
     *
     * @param date   java.util.Date
     * @param userId 用户 ID
     * @return 1 - 更新成功
     */
    int updateLastRecordCreatedDate(@Param("newDate") Date date, @Param("userId") Integer userId);
}
