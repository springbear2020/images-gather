package edu.whut.bear.gather.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * @author Spring-_-Bear
 * @datetime 6/6/2022 9:43 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class QiniuUtilsTest {
    @Autowired
    private QiniuUtils qiniuUtils;

    @Test
    public void getImageUploadToken() {
        System.out.println(qiniuUtils.getImageUploadToken("1.png"));
    }
}