package edu.whut.bear.gather.util;

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
}
