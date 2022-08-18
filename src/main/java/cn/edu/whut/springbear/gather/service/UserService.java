package cn.edu.whut.springbear.gather.service;

import cn.edu.whut.springbear.gather.pojo.User;

import java.util.List;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-10 23:59 Wednesday
 */
public interface UserService {
    /**
     * Verify the correctness of user by password and (username,phone,email)
     */
    User getUser(String condition, String password);

    /**
     * Update user's information of the field is not null
     */
    boolean updateUser(User user);

    /**
     * Get user by username and email
     */
    User getUserByUsernameAndEmail(String username, String email);

    /**
     * Save users in batch
     */
    int saveUsersBatch(List<User> userList);

    /**
     * Get all user list of the class
     */
    List<User> listUsersOfClass(Integer classId);

    /**
     * Save user
     */
    boolean saveUser(User user);

    /**
     * Get user by id
     */
    User getUser(Integer userId);
}
