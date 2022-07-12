package edu.whut.springbear.gather.service;

import edu.whut.springbear.gather.pojo.User;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Spring-_-Bear
 * @datetime 2022-06-30 19:04 Thursday
 */
@Service
public interface UserService {
    /**
     * Get the use by username and password,
     * verify the correctness of it
     *
     * @return User or null
     */
    User queryUserByUsernameAndPassword(String username, String password);

    /**
     * Update the login date of the user by user id
     *
     * @param userId       Id of user
     * @param newLoginDate The new login date
     * @return true - Update successfully
     */
    boolean updateUserLastLoginDate(Integer userId, Date newLoginDate);

    /**
     * Update the login password of the user
     *
     * @param newPassword New password
     * @param userId      Id of user
     * @return true - Update successfully
     */
    boolean updateUserPassword(String newPassword, Integer userId);

    /**
     * Get the user info with the student info
     *
     * @param userId Id of user
     * @return User with student property
     */
    User getUserWithStudentInfo(Integer userId);

    /**
     * Update the login password of the user by username
     *
     * @param username    Username of user
     * @param newPassword New password
     * @return true - Update successfully
     */
    boolean updateUserPassword(String username, String newPassword);
}
