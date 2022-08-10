package cn.edu.whut.springbear.gather.service.impl;

import cn.edu.whut.springbear.gather.mapper.UserMapper;
import cn.edu.whut.springbear.gather.pojo.User;
import cn.edu.whut.springbear.gather.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-08 14:14 Monday
 */
@Service

public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User queryUserByUsernameAndPassword(String username, String password) {
        return userMapper.getUserByUsernameAndPassword(username, password);
    }

    @Override
    public boolean updateUserLastLoginDate(Integer userId, Date newLoginDate) {
        return userMapper.updateLastLoginDate(userId, newLoginDate) == 1;
    }

    @Override
    public boolean updateUserPassword(String username, String newPassword) {
        return userMapper.updatePasswordByUsername(username, newPassword) == 1;
    }

    @Override
    public User getUserByUsernameAndEmail(String username, String email) {
        return userMapper.getUserByUsernameAndEmail(username, email);
    }

    @Override
    public boolean updateUserInfo(String newPhone, String newEmail, Integer userId) {
        return userMapper.updateUserInfo(newPhone, newEmail, userId) == 1;
    }
}
