package cn.edu.whut.springbear.gather.mapper;

import cn.edu.whut.springbear.gather.config.SpringConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * @author Spring-_-Bear
 * @datetime 2022-08-08 10:01 Monday
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfiguration.class)
public class UserMapperTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void getUserByUsernameAndPassword() {
        System.out.println(userMapper.getUserByUsernameAndPassword("bear", "bear"));
    }
}