package edu.whut.bear.gather.util;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Spring-_-Bear
 * @datetime 6/3/2022 12:39 AM
 */
public class DateUtils {
    /**
     * java.util.Date format to Chinese date style like 2022-04-22
     *
     * @param date java.util.Date
     * @return Chinese style datetime like 2022-04-22 23:02
     */
    public static String parseDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    /**
     * Judge the date whether it is today date
     *
     * @param date Specified date
     * @return true - Yes, it is today
     */
    public static boolean isToday(Date date) {
        String specifiedDate = parseDate(date);
        String today = parseDate(new Date());
        return specifiedDate.equals(today);
    }

    /**
     * Convert String type date to java.util.Date
     *
     * @param date Date in string format
     * @return Date
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

    /**
     * Parse the java.util.date to datetime in chinese format
     *
     * @param date Date
     * @return Datetime
     */
    public static String parseDateToDatetime(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }
}
