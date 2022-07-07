package edu.whut.springbear.gather.service.impl;

import edu.whut.springbear.gather.mapper.UserMapper;
import edu.whut.springbear.gather.pojo.User;
import edu.whut.springbear.gather.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author Spring-_-Bear
 * @datetime 2022-06-30 19:05 Thursday
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public User queryUserByUsernameAndPassword(String username, String password) {
        return userMapper.getUserWithStudentByUsernameAndPassword(username, password);
    }

    @Override
    public boolean updateUserLastLoginDate(Integer userId, Date newLoginDate) {
        return userMapper.updateLastLoginDate(userId, newLoginDate) == 1;
    }

    @Override
    public boolean updateUserPassword(String newPassword, Integer userId) {
        return userMapper.updateUserPassword(newPassword, userId) == 1;
    }

    @Override
    public User getUserWithStudentInfo(Integer userId) {
        return userMapper.getUserWithStudentByUserId(userId);
    }

    @Override
    public boolean updateUserPassword(String username, String newPassword) {
        return userMapper.updatePasswordByUsername(username, newPassword) == 1;
    }
}