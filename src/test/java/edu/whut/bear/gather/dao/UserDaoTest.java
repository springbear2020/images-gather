package edu.whut.bear.gather.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

/**
 * @author Spring-_-Bear
 * @datetime 6/2/2022 10:07 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class UserDaoTest {
    @Autowired
    private UserDao userDao;

    @Test
    public void getUserByUsernameAndPassword() {
        System.out.println(userDao.getUserByUsernameAndPassword("0121910870705", "0121910870705"));
    }

    @Test
    public void updateLastLoginDateById() {
        System.out.println(userDao.updateLastLoginDateById(new Date(), 108));
    }
}