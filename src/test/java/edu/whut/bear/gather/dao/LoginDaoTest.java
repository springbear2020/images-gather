package edu.whut.bear.gather.dao;

import edu.whut.bear.gather.pojo.Login;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;


/**
 * @author Spring-_-Bear
 * @datetime 6/2/2022 11:55 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class LoginDaoTest {
    @Autowired
    private LoginDao loginDao;

    @Test
    public void saveLogin() {
        System.out.println(loginDao.saveLogin(new Login(1, "192.168.43.225", "湖北省武汉市", new Date())));
    }
}