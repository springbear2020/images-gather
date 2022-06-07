package edu.whut.bear.gather.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

/**
 * @author Spring-_-Bear
 * @datetime 6/6/2022 1:06 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class UserDaoTest {
    @Autowired
    private UserDao userDao;

    @Test
    public void getUserByUsernameAndPassword() {
        System.out.println(userDao.getUserByUsernameAndPassword("0121910870705", "bear"));
    }

    @Test
    public void updateUserPasswordById() {
        System.out.println(userDao.updateUserPasswordById("b", 108));
    }

    @Test
    public void updateLastRecordCreatedDate() {
        System.out.println(userDao.updateLastRecordCreatedDate(new Date(), 1));
    }

    @Test
    public void getClassUserListNotLogin() {
        System.out.println(userDao.getClassUserListNotLogin(4,new Date()));
    }
}