package edu.whut.bear.gather.dao;

import edu.whut.bear.gather.pojo.Login;
import org.springframework.stereotype.Repository;

/**
 * @author Spring-_-Bear
 * @datetime 6/2/2022 11:51 PM
 */
@Repository
public interface LoginDao {
    /**
     * Save the log record of user login
     *
     * @param login Record of user login
     * @return 1 - Save successfully
     */
    int saveLogin(Login login);
}
