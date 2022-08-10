package cn.edu.whut.springbear.gather.mapper;

import cn.edu.whut.springbear.gather.config.SpringConfiguration;
import cn.edu.whut.springbear.gather.pojo.Student;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;


/**
 * @author Spring-_-Bear
 * @datetime 2022-08-08 16:26 Monday
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfiguration.class)
public class StudentMapperTest {
    @Autowired
    private StudentMapper studentMapper;

    @Test
    public void getStudentByUserId() {
        System.out.println(studentMapper.getStudentByUserId(107));
    }

    @Test
    public void getClassStudentListNotSignIn() {
        List<Student> studentList = studentMapper.getClassStudentListNotSignIn("软件zy1901", new Date());
        studentList.forEach(System.out::println);
    }
}