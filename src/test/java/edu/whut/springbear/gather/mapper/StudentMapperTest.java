package edu.whut.springbear.gather.mapper;

import edu.whut.springbear.gather.config.SpringConfiguration;
import edu.whut.springbear.gather.pojo.Student;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;


/**
 * @author Spring-_-Bear
 * @datetime 2022-06-30 15:54 Thursday
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfiguration.class)
public class StudentMapperTest {
    @Autowired
    private StudentMapper studentMapper;

    @Test
    public void getAllStudents() {
        List<Student> studentList = studentMapper.getAllStudents();
        studentList.forEach(System.out::println);
    }

    @Test
    public void getStudentById() {
        System.out.println(studentMapper.getStudentById(1));
    }
}