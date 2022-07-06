package edu.whut.springbear.gather.service.impl;

import edu.whut.springbear.converter.Converter;
import edu.whut.springbear.converter.SheetBeanConverter;
import edu.whut.springbear.gather.config.SpringConfiguration;
import edu.whut.springbear.gather.pojo.Student;
import edu.whut.springbear.gather.service.StudentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;


/**
 * @author Spring-_-Bear
 * @datetime 2022-07-05 21:09 Tuesday
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfiguration.class)
public class StudentServiceImplTest {
    @Autowired
    private StudentService studentService;

    @Test
    public void saveStudent() {
        Converter converter = new SheetBeanConverter("C:\\Users\\Admin\\Desktop\\2019.xlsx");
        List<Student> studentList = converter.excelConvertBean(Student.class);
        int studentSize = 0;
        int result = 0;
        if (studentList != null) {
            studentSize = studentList.size();
            for (Student student : studentList) {
                result += studentService.saveStudent(student);
            }
        }
        if (result == studentSize) {
            System.out.println("All students info save successfully, number of students is " + studentSize);
        } else {
            System.out.println("Number of students is " + studentSize + " , number of save successfully is " + result);
        }
    }
}