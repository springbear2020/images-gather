package cn.edu.whut.springbear.gather.util.poi;

import cn.edu.whut.springbear.gather.util.poi.exception.ConverterException;
import cn.edu.whut.springbear.gather.pojo.Student;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.List;


/**
 * @author Spring-_-Bear
 * @datetime 2022-08-09 21:00 Tuesday
 */
public class ConverterTest {

    @Test
    public void sheetConvertBean() throws ConverterException, FileNotFoundException {
        Converter converter = new SheetBeanConverter("C:\\Users\\Admin\\Desktop\\student.xlsx");
        List<Student> studentList = converter.sheetConvertBean(Student.class, 3);
        studentList.forEach(System.out::println);
    }
}