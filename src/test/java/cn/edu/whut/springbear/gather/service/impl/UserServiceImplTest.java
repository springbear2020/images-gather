package cn.edu.whut.springbear.gather.service.impl;

import cn.edu.whut.springbear.gather.config.SpringConfiguration;
import cn.edu.whut.springbear.gather.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * @author Spring-_-Bear
 * @datetime 2022-08-08 14:24 Monday
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfiguration.class)
public class UserServiceImplTest {
    @Autowired
    private UserService userService;

    @Test
    public void userLogin() {
        System.out.println(userService.queryUserByUsernameAndPassword("bear", "bear"));
    }
}