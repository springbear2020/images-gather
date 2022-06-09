package edu.whut.bear.gather.util;

import java.text.ParseException;
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

    /**
     * 将 java.util.Date 解析为 20220606122345 的格式
     *
     * @param date java.util.Date
     * @return 20220606122345
     */
    public static String parseDateNoHyphenDatetime(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(date);
    }


    /**
     * 将 java.util.Date 解析为 20220606 的格式
     *
     * @param date java.util.Date
     * @return 20220606
     */
    public static String parseDateNoHyphenDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        return dateFormat.format(date);
    }

    /**
     * 将字符串类型时间解析为 java.util.Date
     *
     * @param date 字符串格式时间
     * @return java.util.Date
     */
    public static Date parseString(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }
}
