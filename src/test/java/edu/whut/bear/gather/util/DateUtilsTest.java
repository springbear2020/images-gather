package edu.whut.bear.gather.util;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * @author Spring-_-Bear
 * @datetime 6/3/2022 12:40 AM
 */
public class DateUtilsTest {

    @Test
    public void parseDate() {
        System.out.println(DateUtils.parseDate(new Date()));
    }

    @Test
    public void isToday() {
        System.out.println(DateUtils.isToday(new Date()));
    }
}