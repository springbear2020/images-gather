package edu.whut.bear.gather.util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Spring-_-Bear
 * @datetime 6/2/2022 11:48 PM
 */
public class WebUtilsTest {

    @Test
    public void parseIp() {
        System.out.println(WebUtils.parseIp("124.221.120.56"));
    }
}