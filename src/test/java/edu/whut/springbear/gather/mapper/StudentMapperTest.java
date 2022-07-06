package edu.whut.springbear.gather.mapper;

import edu.whut.springbear.converter.Converter;
import edu.whut.springbear.converter.SheetBeanConverter;
import edu.whut.springbear.gather.config.SpringConfiguration;
import edu.whut.springbear.gather.pojo.Student;
import edu.whut.springbear.gather.pojo.User;
import edu.whut.springbear.gather.util.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
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
    @Autowired
    private UserMapper userMapper;

    @Test
    public void getStudentById() {
        System.out.println(studentMapper.getStudentById(109));
    }

    @Test
    public void saveStudent() {
        Converter converter = new SheetBeanConverter("C:\\Users\\Admin\\Desktop\\2019.xlsx");
        List<Student> userList = converter.excelConvertBean(Student.class);
        if (userList != null) {
            for (Student student : userList) {
                studentMapper.saveStudent(student);
                User user = new User();
                user.setUsername(student.getNumber());
                user.setPassword(student.getNumber());
                user.setLastLoginDate(DateUtils.parseStringWithHyphen("1970-01-01", new Date()));
                user.setUserType(User.TYPE_USER);
                user.setUserStatus(User.STATUS_NORMAL);
                user.setStudentId(student.getId());
                userMapper.saveUser(user);
                studentMapper.updateStudentUserId(user.getId(), student.getId());
            }
        }
    }

    @Test
    public void getClassStudentListNotLogin() {
        List<Student> studentList = studentMapper.getClassStudentListNotSignInOnSpecifiedDay("软件zy1901", new Date());
        System.out.println(studentList.size());
    }

    @Test
    public void getClassStudentListNotUploadOnSpecifiedDay() {
        List<Student> studentList = studentMapper.getClassStudentListOnSpecifiedDayByStatus(1, "软件zy1901", DateUtils.parseStringWithHyphen("2022-07-05", new Date()));
        studentList.forEach(System.out::println);
    }

    @Test
    public void getStudentByUserId() {
        System.out.println(studentMapper.getStudentByUserId(109));
    }
}