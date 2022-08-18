package cn.edu.whut.springbear.gather.util;

import java.util.Random;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-11 01:30 Thursday
 */
public class NumberUtils {
    /**
     * Generate the digital code string in length
     */
    public static String digitalCodeString(int len) {
        int[] nums = new int[]{1, 7, 9, 3, 5, 0, 6, 8, 2, 4};
        StringBuilder sb = new StringBuilder();
        for (int j = 1; j <= len; j++) {
            sb.append(nums[new Random().nextInt(10)]);
        }
        return sb.toString();
    }
}
