package edu.whut.bear.gather.service;

import edu.whut.bear.gather.pojo.Login;
import org.springframework.stereotype.Service;

/**
 * @author Spring-_-Bear
 * @datetime 6/3/2022 12:01 AM
 */
@Service
public interface LoginService {
    /**
     * Save the log record of user login
     *
     * @param login Record of user login
     * @return true - Save successfully
     */
    boolean saveLogin(Login login);
}
