package cn.edu.whut.springbear.gather.service;


import cn.edu.whut.springbear.gather.pojo.User;

import java.util.Date;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-08 14:14 Monday
 */
public interface UserService {
    /**
     * Verify the correctness of user by username and password
     */
    User queryUserByUsernameAndPassword(String username, String password);

    /**
     * Update the login date of the user by user id
     */
    boolean updateUserLastLoginDate(Integer userId, Date newLoginDate);

    /**
     * Update the login password of the user by username
     */
    boolean updateUserPassword(String username, String newPassword);

    /**
     * Get user by username and email address
     */
    User getUserByUsernameAndEmail(String username, String email);

    /**
     * Update the user info, including user email and phone number
     */
    boolean updateUserInfo(String newPhone, String newEmail, Integer userId);
}
