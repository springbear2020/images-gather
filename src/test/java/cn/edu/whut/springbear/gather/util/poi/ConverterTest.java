package cn.edu.whut.springbear.gather.util.poi;

import cn.edu.whut.springbear.gather.pojo.People;
import org.junit.Test;

import java.util.List;


/**
 * @author Spring-_-Bear
 * @datetime 2022-08-14 07:17 Sunday
 */
public class ConverterTest {

    @Test
    public void sheetConvertBean() {
        Converter converter = new SheetBeanConverter("C:/Users/Admin/Desktop/2019.xlsx");
        List<People> peopleList = converter.sheetConvertBean(People.class);
        peopleList.forEach(System.out::println);
    }
}