package edu.whut.bear.gather.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Spring-_-Bear
 * @datetime 6/6/2022 8:29 PM
 */
public class DateUtils {
    /**
     * 将 java.util.Date 解析成 2022-06-06 格式
     *
     * @param date java.util.Date
     * @return 中国习惯日期格式
     */
    public static String parseDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    /**
     * 判断传入的日期是否是今天
     *
     * @param date java.util.Date
     * @return true - 是
     */
    public static boolean isToday(Date date) {
        String specifiedDate = parseDate(date);
        String today = parseDate(new Date());
        return specifiedDate.equals(today);
    }
}
