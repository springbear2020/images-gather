package cn.edu.whut.springbear.gather.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-11 00:45 Thursday
 */
public class DateUtils {
    /**
     * Parse java.util.Date like this format (2022-06-30)
     */
    public static String parseDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    /**
     * Parse java.util.Date into 2022-06-06 12:23:45 format
     *
     * @param date java.util.Date
     * @return Datetime in format string
     */
    public static String parseDatetime(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }

    /**
     * Parse java.util.Date into 20220606122345 format
     */
    public static String parseDatetimeNoHyphen(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(date);
    }

    /**
     * Parse the date in string format into java.util.Date,
     * if parse failed then return null
     */
    public static Date parseString(String dateStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Judge the specified date is today
     */
    public static boolean isToday(Date date) {
        String specifiedDate = parseDate(date);
        String today = parseDate(new Date());
        return specifiedDate.equals(today);
    }
}
