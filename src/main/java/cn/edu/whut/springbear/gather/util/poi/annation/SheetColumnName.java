package cn.edu.whut.springbear.gather.util.poi.annation;

import java.lang.annotation.*;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-09 15:54 Tuesday
 */
@Inherited
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SheetColumnName {
    String value() default "";
}
