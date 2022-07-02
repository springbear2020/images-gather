package edu.whut.springbear.gather.mapper;

import edu.whut.springbear.gather.config.SpringConfiguration;
import edu.whut.springbear.gather.pojo.User;
import edu.whut.springbear.gather.util.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;


/**
 * @author Spring-_-Bear
 * @datetime 2022-06-30 16:39 Thursday
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfiguration.class)
public class UserMapperTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void getAllUsers() {
        List<User> userList = userMapper.getAllUsers();
        userList.forEach(System.out::println);
    }

    @Test
    public void getUserByUsernameAndPassword() {
        System.out.println(userMapper.getUserByUsernameAndPassword("admin", "admin"));
    }

    @Test
    public void updateLastLoginDate() {
        System.out.println(userMapper.updateLastLoginDate(1, DateUtils.parseString("1970-01-01", new Date())));
    }

    @Test
    public void updateUserPassword() {
        System.out.println(userMapper.updateUserPassword("bear",1));
    }

    @Test
    public void getUserStudent() {
        System.out.println(userMapper.getUserStudent(1));
    }
}