package edu.whut.springbear.gather.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Spring-_-Bear
 * @datetime 2022-06-30 22:12 Thursday
 */
public class DateUtils {
    /**
     * Parse the java.util.Date into 2022-06-30 format
     *
     * @param date java.util.Date
     * @return Date after formatting
     */
    public static String parseDateWithHyphen(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    /**
     * Parse java.util.Date into 20220606122345 format
     *
     * @param date java.util.Date
     * @return Datetime in format string
     */
    public static String parseDatetimeNoHyphen(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(date);
    }

    /**
     * Parse java.util.Date into 2022-06-06 12:23:45 format
     *
     * @param date java.util.Date
     * @return Datetime in format string
     */
    public static String parseDatetimeWithHyphen(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }

    /**
     * Judge the specified date is today
     *
     * @param date java.util.Date
     * @return true - Yes, it is today
     */
    public static boolean isToday(Date date) {
        String specifiedDate = parseDateWithHyphen(date);
        String today = parseDateWithHyphen(new Date());
        return specifiedDate.equals(today);
    }

    /**
     * Parse the date in string format into java.util.Date,
     * if parse then return the default Date given by user
     *
     * @param dateStr     Date in string format
     * @param defaultDate Default date when parse in exception was thrown
     * @return java.util.Date
     */
    public static Date parseStringWithHyphen(String dateStr, Date defaultDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return defaultDate;
        }
    }
}
