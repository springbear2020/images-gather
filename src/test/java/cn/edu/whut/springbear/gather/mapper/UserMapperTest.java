package cn.edu.whut.springbear.gather.mapper;

import cn.edu.whut.springbear.gather.config.SpringConfiguration;
import cn.edu.whut.springbear.gather.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;


/**
 * @author Spring-_-Bear
 * @datetime 2022-08-11 00:31 Thursday
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfiguration.class)
public class UserMapperTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void updateUserById() {
        User user = new User();
        user.setId(108);
        user.setPassword("123");
        user.setEmail("123");
        user.setPhone("123");
        user.setLastLoginDatetime(new Date());
        System.out.println(userMapper.updateUser(user));
    }

    @Test
    public void saveUser() {
        User user = new User();
        user.setUsername("123");
        user.setPassword("123");
        user.setEmail("123");
        user.setPhone("123");
        user.setLastLoginDatetime(new Date());
        System.out.println(userMapper.saveUser(user));
        System.out.println(user);
    }

    @Test
    public void listUsersOfClass() {
        List<User> userList = userMapper.listUsersOfClass(1);
        userList.forEach(System.out::println);
    }
}