package cn.edu.whut.springbear.gather.util;

import org.junit.Test;


/**
 * @author Spring-_-Bear
 * @datetime 2022-08-15 21:48 Monday
 */
public class MD5UtilsTest {

    @Test
    public void md5Encrypt() {
        // Output: 0fe067dba5a97e46939e21a33b50dd99
        System.out.println(MD5Utils.md5Encrypt("root"));
    }
}