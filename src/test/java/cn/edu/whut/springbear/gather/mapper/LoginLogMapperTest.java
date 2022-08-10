package cn.edu.whut.springbear.gather.mapper;

import cn.edu.whut.springbear.gather.config.SpringConfiguration;
import cn.edu.whut.springbear.gather.pojo.LoginLog;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;


/**
 * @author Spring-_-Bear
 * @datetime 2022-08-08 22:03 Monday
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfiguration.class)
public class LoginLogMapperTest {
    @Autowired
    private LoginLogMapper loginLogMapper;

    @Test
    public void getUserLoginLog() {
        List<LoginLog> loginLogList = loginLogMapper.getUserLoginLog(108);
        loginLogList.forEach(System.out::println);
    }

    @Test
    public void getLatestLoginLog() {
        System.out.println(loginLogMapper.getLatestLoginLog(108));
    }
}