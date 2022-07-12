package edu.whut.springbear.gather.mapper;

import edu.whut.springbear.converter.Converter;
import edu.whut.springbear.converter.SheetBeanConverter;
import edu.whut.springbear.gather.config.SpringConfiguration;
import edu.whut.springbear.gather.pojo.LoginLog;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;


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

    @Test
    public void getUserLoginLog() {
        List<LoginLog> loginLogList = loginLogMapper.getUserLoginLog(1);
        loginLogList.forEach(System.out::println);
    }

    @Test
    public void saveLoginLogFromExcel() {
        Converter converter = new SheetBeanConverter("C:/Users/Admin/Desktop/log_login.xlsx");
        List<LoginLog> loginLogs = converter.excelConvertBean(LoginLog.class);
        int sum   = 0;
        if (loginLogs != null) {
            for (LoginLog loginLog : loginLogs) {
                int i = loginLogMapper.saveLoginLog(loginLog);
                sum += i;
            }
        }
        System.out.println(sum);
    }
}