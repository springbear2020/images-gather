package edu.whut.bear.gather.service.impl;

import edu.whut.bear.gather.dao.UserDao;
import edu.whut.bear.gather.pojo.User;
import edu.whut.bear.gather.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Spring-_-Bear
 * @datetime 6/6/2022 1:09 PM
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public User verifyUsernameAndPassword(String username, String password) {
        return userDao.getUserByUsernameAndPassword(username, password);
    }
}
