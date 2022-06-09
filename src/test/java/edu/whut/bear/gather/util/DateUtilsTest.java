package edu.whut.bear.gather.util;

import org.junit.Test;

import java.util.Date;


/**
 * @author Spring-_-Bear
 * @datetime 6/8/2022 11:42 AM
 */
public class DateUtilsTest {

    @Test
    public void parseDateNoHyphen() {
        System.out.println(DateUtils.parseDateNoHyphenDatetime(new Date()));
    }
}