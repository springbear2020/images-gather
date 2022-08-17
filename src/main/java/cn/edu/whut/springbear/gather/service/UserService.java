package cn.edu.whut.springbear.gather.service;

import cn.edu.whut.springbear.gather.pojo.User;

import java.util.Date;
import java.util.List;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-10 23:59 Wednesday
 */
public interface UserService {
    /**
     * Verify the correctness of user by username and password
     */
    User queryUser(String username, String password);

    /**
     * Update the login datetime of the user
     */
    boolean updateLoginDatetime(Integer userId, Date date);

    /**
     * Get user by username and email
     */
    User queryUserByUsernameAndEmail(String username, String email);

    /**
     * Update the login password of the user by user id
     */
    boolean updateUserPassword(Integer userId, String newPassword);

    /**
     * Update the email and phone number of the user
     */
    boolean updateUserEmailAndPhone(String newEmail, String newPhone, Integer userId);

    /**
     * Get user by user id
     */
    User queryUser(Integer userId);

    /**
     * Update the user type of user
     */
    boolean updateUserType(Integer userId, Integer userType);

    /**
     * Save user
     */
    boolean saveUser(User user);
}
