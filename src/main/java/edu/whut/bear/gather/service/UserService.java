package edu.whut.bear.gather.service;

import edu.whut.bear.gather.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Spring-_-Bear
 * @datetime 6/2/2022 10:36 PM
 */
@Service
public interface UserService {
    /**
     * Verify the correctness of the username and password when user login
     *
     * @param username Username
     * @param password Password
     * @return User or null
     */
    User verifyUsernameAndPassword(String username, String password);

    /**
     * Update the user's last login date before user's session was destroyed
     *
     * @param newDate New login date
     * @param userId      Id of user
     * @return true - Update successfully
     */
    boolean updateLastLoginDate(Date newDate, Integer userId);

    /**
     * Update the user's password
     *
     * @param newPassword New password
     * @param id          Id of user
     * @return true - Update successfully
     */
    boolean updatePasswordById(@Param("newPassword") String newPassword, @Param("id") Integer id);
}
