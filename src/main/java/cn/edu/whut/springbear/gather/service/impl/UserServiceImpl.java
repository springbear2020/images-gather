package cn.edu.whut.springbear.gather.service.impl;

import cn.edu.whut.springbear.gather.mapper.ClassMapper;
import cn.edu.whut.springbear.gather.mapper.GradeMapper;
import cn.edu.whut.springbear.gather.mapper.SchoolMapper;
import cn.edu.whut.springbear.gather.mapper.UserMapper;
import cn.edu.whut.springbear.gather.pojo.Class;
import cn.edu.whut.springbear.gather.pojo.Grade;
import cn.edu.whut.springbear.gather.pojo.School;
import cn.edu.whut.springbear.gather.pojo.User;
import cn.edu.whut.springbear.gather.service.UserService;
import cn.edu.whut.springbear.gather.util.DateUtils;
import cn.edu.whut.springbear.gather.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-11 00:00 Thursday
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SchoolMapper schoolMapper;
    @Autowired
    private GradeMapper gradeMapper;
    @Autowired
    private ClassMapper classMapper;

    @Override
    public User getUser(String condition, String password) {
        return userMapper.getUser(condition, password);
    }

    @Override
    public boolean updateUser(User user) {
        return userMapper.updateUser(user) == 1;
    }

    @Override
    public User getUserByUsernameAndEmail(String username, String email) {
        return userMapper.getUserByUsernameAndEmail(username, email);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveUsersBatch(List<User> userList) {
        User userOne = userList.get(1);
        // Verify the existences of the school, grade and class
        School school = schoolMapper.getSchool(userOne.getSchool());
        if (school == null) {
            // Create school, return the generated auto increment key
            school = new School();
            school.setSchool(userOne.getSchool());
            schoolMapper.saveSchool(school);
        }
        Grade grade = gradeMapper.getGradeOfSchool(userOne.getGrade(), school.getId());
        if (grade == null) {
            // Create grade and save the correspond relation between school and grade
            grade = new Grade();
            grade.setGrade(userOne.getGrade());
            gradeMapper.saveGrade(grade);
            gradeMapper.saveSchoolGrade(school.getId(), grade.getId());
        }
        Class classObj = classMapper.getClassOfGrade(userOne.getClassName(), grade.getId());
        if (classObj == null) {
            // Create class  and save correspond relation between grade and class
            classObj = new Class();
            classObj.setClassName(userOne.getClassName());
            classMapper.saveClass(classObj);
            classMapper.saveGradeClass(grade.getId(), classObj.getId());
        }

        int affectedRows = 0;
        // Traverse the all people list
        for (User user : userList) {
            // Register user, encrypt the password
            user.setPassword(MD5Utils.md5Encrypt(user.getUsername()));
            user.setClassId(classObj.getId());
            user.setGradeId(grade.getId());
            user.setSchoolId(school.getId());
            user.setUserType(User.TYPE_STUDENT);
            user.setUserStatus(User.STATUS_NORMAL);
            user.setCreateDatetime(new Date());
            user.setLastLoginDatetime(DateUtils.parseString("1970-01-01"));
            if (userMapper.saveUser(user) == 1) {
                affectedRows++;
            }
        }
        return affectedRows;
    }


    @Override
    public List<User> listUsersOfClass(Integer classId) {
        return userMapper.listUsersOfClass(classId);
    }

    @Override
    public boolean saveUser(User user) {
        user.setClassId(0);
        user.setPassword(MD5Utils.md5Encrypt(user.getUsername()));
        user.setUserType(User.TYPE_GRADE_TEACHER);
        user.setUserType(User.TYPE_GRADE_TEACHER);
        user.setUserStatus(User.STATUS_NORMAL);
        user.setCreateDatetime(new Date());
        user.setLastLoginDatetime(DateUtils.parseString("1970-01-01"));
        return userMapper.saveUser(user) == 1;
    }
}
