package edu.whut.bear.gather.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Spring-_-Bear
 * @datetime 6/4/2022 11:15 AM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class PropertyUtilsTest {
    @Autowired
    private PropertyUtils propertyUtils;

    @Test
    public void test() {
        System.out.println(propertyUtils.getContextUrl());
    }
}
