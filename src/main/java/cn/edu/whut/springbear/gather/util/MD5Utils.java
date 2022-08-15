package cn.edu.whut.springbear.gather.util;

import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-15 21:43 Monday
 */
public class MD5Utils {
    /**
     * MD5 encrypt, encrypt the source string, then reverse the encoded string, encrypt again.
     */
    public static String md5Encrypt(String srcStr) {
        StringBuilder sb = new StringBuilder(DigestUtils.md5DigestAsHex(srcStr.getBytes(StandardCharsets.UTF_8)));
        sb.reverse();
        return DigestUtils.md5DigestAsHex(sb.toString().getBytes(StandardCharsets.UTF_8));
    }
}
