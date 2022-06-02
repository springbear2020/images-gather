package edu.whut.bear.gather.service;

import edu.whut.bear.gather.pojo.User;
import org.springframework.stereotype.Service;

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
}
