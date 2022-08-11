package cn.edu.whut.springbear.gather.service.impl;

import cn.edu.whut.springbear.gather.mapper.UserMapper;
import cn.edu.whut.springbear.gather.pojo.User;
import cn.edu.whut.springbear.gather.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-11 00:00 Thursday
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User queryUser(String username, String password) {
        return userMapper.getUserByUsernameAndPassword(username, password);
    }

    @Override
    public boolean updateLoginDatetime(Integer userId, Date date) {
        User user = new User();
        user.setId(userId);
        user.setLastLoginDatetime(date);
        return userMapper.updateUserById(user) == 1;
    }

    @Override
    public User queryUserByUsernameAndEmail(String username, String email) {
        return userMapper.getUserByUsernameAndEmail(username, email);
    }

    @Override
    public boolean updateUserPassword(Integer userId, String newPassword) {
        User user = new User();
        user.setId(userId);
        user.setPassword(newPassword);
        return userMapper.updateUserById(user) == 1;
    }
}
