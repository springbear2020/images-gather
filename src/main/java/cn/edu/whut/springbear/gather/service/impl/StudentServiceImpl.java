package cn.edu.whut.springbear.gather.service.impl;

import cn.edu.whut.springbear.gather.mapper.StudentMapper;
import cn.edu.whut.springbear.gather.pojo.Student;
import cn.edu.whut.springbear.gather.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-08 16:28 Monday
 */
@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentMapper studentMapper;

    @Override
    public Student getStudentByUserId(Integer userId) {
        return studentMapper.getStudentByUserId(userId);
    }

    @Override
    public boolean updateStudentInfo(String newSex, String newPhone, String newEmail, Integer id) {
        return studentMapper.updateStudentInfo(newSex, newPhone, newEmail, id) == 1;
    }

    @Override
    public List<Student> getClassNotLoginList(String className, Date specifiedDate) {
        return studentMapper.getClassStudentListNotSignIn(className, specifiedDate);
    }

    @Override
    public List<Student> getClassUploadList(Integer uploadStatus, String className, Date specifiedDate) {
        return studentMapper.getClassStudentListByUploadStatus(uploadStatus, className, specifiedDate);
    }

    @Override
    public List<String> getClassStudentNameListNotLogin(String className, Date specifiedDate) {
        return studentMapper.getClassStudentNameListNotLogin(className, specifiedDate);
    }

    @Override
    public List<String> getClassStudentNameListByUploadStatus(Integer uploadStatus, String className, Date specifiedDate) {
        return studentMapper.getClassStudentNameListByUploadStatus(uploadStatus, className, specifiedDate);
    }
}
