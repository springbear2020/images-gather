package cn.edu.whut.springbear.gather.util.poi;

import java.util.List;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-09 15:55 Tuesday
 */
public interface Converter {
    /**
     * Convert the row data in the sheet row into java bean object
     */
    <T> List<T> sheetConvertBean(Class<T> clazz);
}
