package cn.edu.whut.springbear.gather.util;

import org.junit.Test;


/**
 * @author Spring-_-Bear
 * @datetime 2022-08-15 21:48 Monday
 */
public class MD5UtilsTest {

    @Test
    public void md5Encrypt() {
        System.out.println(MD5Utils.md5Encrypt("root"));
    }
}