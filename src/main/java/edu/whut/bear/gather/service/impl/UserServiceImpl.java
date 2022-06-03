package edu.whut.bear.gather.service.impl;

import edu.whut.bear.gather.dao.UserDao;
import edu.whut.bear.gather.pojo.User;
import edu.whut.bear.gather.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Spring-_-Bear
 * @datetime 6/2/2022 10:38 PM
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public User verifyUsernameAndPassword(String username, String password) {
        return userDao.getUserByUsernameAndPassword(username, password);
    }

    @Override
    public int updateLastLoginDate(Date newDate, Integer userId) {
        return userDao.updateLastLoginDateById(newDate, userId);
    }

    @Override
    public boolean updatePasswordById(String newPassword, Integer id) {
        return userDao.updatePasswordById(newPassword, id) == 1;
    }
}
