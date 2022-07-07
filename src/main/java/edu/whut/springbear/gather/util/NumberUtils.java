package edu.whut.springbear.gather.util;

import java.util.Random;

/**
 * @author Spring-_-Bear
 * @datetime 2022-07-07 07:01 Thursday
 */
public class NumberUtils {
    /**
     * Generate the digital code string in length
     *
     * @param maxLength The max length of the code
     * @return Digital code string
     */
    public static String generateDigitalCode(int maxLength) {
        StringBuilder builder = new StringBuilder();
        for (int j = 1; j <= maxLength; j++) {
            int randomNum = new Random().nextInt(10);
            builder.append("1793506824".charAt(randomNum));
        }
        return builder.toString();
    }
}
