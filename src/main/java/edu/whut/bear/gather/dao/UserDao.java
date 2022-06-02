package edu.whut.bear.gather.dao;

import edu.whut.bear.gather.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


/**
 * @author Spring-_-Bear
 * @datetime 6/2/2022 9:59 PM
 */
@Repository
public interface UserDao {
    /**
     * Get user by username and password
     *
     * @param username Username
     * @param password Password
     * @return User or null
     */
    User getUserByUsernameAndPassword(@Param("username") String username, @Param("password") String password);
}
