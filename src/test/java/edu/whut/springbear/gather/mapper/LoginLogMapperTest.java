package edu.whut.springbear.gather.mapper;

import edu.whut.springbear.gather.config.SpringConfiguration;
import edu.whut.springbear.gather.pojo.LoginLog;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;


/**
 * @author Spring-_-Bear
 * @datetime 2022-06-30 21:52 Thursday
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfiguration.class)
public class LoginLogMapperTest {
    @Autowired
    private LoginLogMapper loginLogMapper;

    @Test
    public void saveLoginLog() {
        System.out.println(loginLogMapper.saveLoginLog(new LoginLog(null, "127.0.0.1", "湖北省武汉市", new Date(), 1)));
    }
}