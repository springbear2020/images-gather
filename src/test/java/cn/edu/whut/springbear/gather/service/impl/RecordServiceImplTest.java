package cn.edu.whut.springbear.gather.service.impl;

import cn.edu.whut.springbear.gather.config.SpringConfiguration;
import cn.edu.whut.springbear.gather.pojo.LoginLog;
import cn.edu.whut.springbear.gather.service.RecordService;
import com.github.pagehelper.PageInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;


/**
 * @author Spring-_-Bear
 * @datetime 2022-08-08 22:09 Monday
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfiguration.class)
public class RecordServiceImplTest {
    @Autowired
    private RecordService recordService;

    @Test
    public void getUserLoginPageData() {
        PageInfo<LoginLog> loginLogPageInfo = recordService.getUserLoginLogPageData(108, 2);
        List<LoginLog> loginLogList = loginLogPageInfo.getList();
        loginLogList.forEach(System.out::println);
    }
}