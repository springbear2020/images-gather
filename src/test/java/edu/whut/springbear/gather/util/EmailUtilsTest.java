package edu.whut.springbear.gather.util;

import edu.whut.springbear.gather.config.SpringConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;


/**
 * @author Spring-_-Bear
 * @datetime 2022-07-06 22:46 Wednesday
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfiguration.class)
public class EmailUtilsTest {
    @Autowired
    private EmailUtils emailUtils;

    @Test
    public void sendEmail() {
        try {
            emailUtils.sendEmail("springbear2020@163.com","123321");
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}