package edu.whut.bear.gather.dao;

import edu.whut.bear.gather.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


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

    /**
     * Update the user's last login date by user id
     *
     * @param date   New login Date
     * @param userId Id of user
     * @return 1 - Update successfully
     */
    int updateLastLoginDateById(@Param("newDate") Date date, @Param("id") Integer userId);

    /**
     * Update the user's password
     *
     * @param newPassword New password
     * @param id          Id of user
     * @return 1 - Update successfully
     */
    int updatePasswordById(@Param("newPassword") String newPassword, @Param("id") Integer id);

    /**
     * Get the user that not created the upload record
     *
     * @param classNumber Number of class
     * @param date        Date
     * @return User list or null
     */
    List<User> getUserRecordNotCreated(@Param("classNumber") Integer classNumber, @Param("date") Date date);
}
