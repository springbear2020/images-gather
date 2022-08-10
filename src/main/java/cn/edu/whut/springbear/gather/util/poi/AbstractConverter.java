package cn.edu.whut.springbear.gather.util.poi;

import cn.edu.whut.springbear.gather.util.poi.annation.ExcelSheetName;
import cn.edu.whut.springbear.gather.util.poi.annation.SheetColumnName;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-09 15:55 Tuesday
 */
public abstract class AbstractConverter {
    /**
     * Get the sheet name though the @ExcelSheetName annotation decorated on the bean class
     *
     * @param clazz Class object of the bean class
     * @return Sheet name or default sheet name named 'sheet1'
     */
    protected <T> String getSheetName(Class<T> clazz) {
        ExcelSheetName sheetNameAnnotation = clazz.getDeclaredAnnotation(ExcelSheetName.class);
        return sheetNameAnnotation == null ? "sheet1" : sheetNameAnnotation.value();
    }

    /**
     * Get the valid field though @SheetColumnName decorated on the java bean field,
     * for example, there is a bean define like this,
     *      public class User implements Serializable {
     *          @SheetColumnName("用户名")
     *          private String username;
     *      }
     * that is meaning the username field is valid and it's relevant column name in the sheet is '用户名'
     * @return Valid map including <columnName, field>
     */
    protected <T> Map<String, Field> validFieldsMapScan(Class<T> clazz) {
        HashMap<String, Field> validFieldMap = new HashMap<>();
        Field[] declaredFields = clazz.getDeclaredFields();

        // Traverse all declared fields and just find the field decorated by the @SheetColumnName annotation
        for (Field field : declaredFields) {
            SheetColumnName fieldAnnotation = field.getAnnotation(SheetColumnName.class);
            if (fieldAnnotation != null) {
                // Sheet column name corresponding to the current field
                String columnName = fieldAnnotation.value();
                if (columnName.length() > 0) {
                    validFieldMap.put(columnName, field);
                }
            }
        }

        return validFieldMap;
    }
}
