package cn.edu.whut.springbear.gather.service.impl;

import cn.edu.whut.springbear.gather.config.SpringConfiguration;
import cn.edu.whut.springbear.gather.service.EmailService;
import cn.edu.whut.springbear.gather.util.NumberUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * @author Spring-_-Bear
 * @datetime 2022-08-09 14:50 Tuesday
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfiguration.class)
public class EmailServiceImplTest {
    @Autowired
    private EmailService emailService;

    @Test
    public void sendEmail() {
        System.out.println(NumberUtils.generateDigitalCode(6));
        System.out.println(emailService.sendEmail("springbear2020@163.com"));
    }
}