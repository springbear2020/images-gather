package edu.whut.bear.gather.util;

import org.junit.Test;

/**
 * @author Spring-_-Bear
 * @datetime 6/6/2022 8:00 PM
 */
public class WebUtilsTest {
    @Test
    public void baiduMapIpAddressAccess() {
        System.out.println(WebUtils.parseIp("124.221.120.56"));
    }
}