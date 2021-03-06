package edu.whut.springbear.gather.mapper;

import edu.whut.springbear.gather.config.SpringConfiguration;
import edu.whut.springbear.gather.util.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;


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
    public void getUserByUsernameAndPassword() {
        System.out.println(userMapper.getUserWithStudentByUsernameAndPassword("admin", "admin"));
    }

    @Test
    public void updateLastLoginDate() {
        System.out.println(userMapper.updateLastLoginDate(1, DateUtils.parseStringWithHyphen("1970-01-01", new Date())));
    }

    @Test
    public void updateUserPassword() {
        System.out.println(userMapper.updateUserPassword("bear",1));
    }

    @Test
    public void getUserStudent() {
        System.out.println(userMapper.getUserWithStudentByUserId(1));
    }

    @Test
    public void updatePasswordByUsername() {
        System.out.println(userMapper.updatePasswordByUsername("0121910870705", "bear"));
    }
}