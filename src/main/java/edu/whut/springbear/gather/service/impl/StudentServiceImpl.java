package edu.whut.springbear.gather.service.impl;

import edu.whut.springbear.gather.mapper.StudentMapper;
import edu.whut.springbear.gather.mapper.UserMapper;
import edu.whut.springbear.gather.pojo.Student;
import edu.whut.springbear.gather.pojo.User;
import edu.whut.springbear.gather.service.StudentService;
import edu.whut.springbear.gather.util.DateUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author Spring-_-Bear
 * @datetime 2022-07-05 21:06 Tuesday
 */
@Repository
public class StudentServiceImpl implements StudentService {
    @Resource
    private StudentMapper studentMapper;
    @Resource
    private UserMapper userMapper;

    @Override
    public int saveStudent(Student student) {
        // Save the student info to the database table
        if (studentMapper.saveStudent(student) == 1) {
            // Save the number of student as username and password to save to the t_user database table
            User user = new User();
            user.setUsername(student.getNumber());
            user.setPassword(student.getNumber());
            user.setLastLoginDate(DateUtils.parseStringWithHyphen("1970-01-01", new Date()));
            user.setUserType(User.TYPE_USER);
            user.setUserStatus(User.STATUS_NORMAL);
            user.setStudentId(student.getId());
            if (userMapper.saveUser(user) == 1) {
                // Update the user id in the t_student record
                if (studentMapper.updateStudentUserId(user.getId(), student.getId()) == 1) {
                    return 1;
                }
            }
        }
        return 0;
    }

    @Override
    public List<Student> getClassStudentsNotSignInOnDate(String className, Date specifiedDate) {
        return studentMapper.getClassStudentListNotSignInOnSpecifiedDay(className, specifiedDate);
    }

    @Override
    public List<Student> getClassStudentsNotUploadOnDate(Integer uploadStatus, String className, Date specifiedDate) {
        return studentMapper.getClassStudentListOnSpecifiedDayByStatus(uploadStatus, className, specifiedDate);
    }

    @Override
    public Student getStudentByStudentNumberAndEmail(String studentNumber, String email) {
        return studentMapper.getStudentByNumberAndEmail(studentNumber, email);
    }
}
