package edu.whut.springbear.gather.service.impl;

import edu.whut.springbear.gather.config.SpringConfiguration;
import edu.whut.springbear.gather.service.RecordService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * @author Spring-_-Bear
 * @datetime 2022-07-03 19:14 Sunday
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfiguration.class)
public class RecordServiceImplTest {
    @Autowired
    private RecordService recordService;

    @Test
    public void getLoginPageData() {
        System.out.println(recordService.getUserLoginPageData(1, 1));
    }
}