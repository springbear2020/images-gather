package cn.edu.whut.springbear.gather.util.poi.annation;

import java.lang.annotation.*;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-09 15:54 Tuesday
 */
@Inherited
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelSheetName {
    String value() default "sheet1";
}
