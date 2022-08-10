package cn.edu.whut.springbear.gather.service.impl;

import cn.edu.whut.springbear.gather.mapper.TeacherMapper;
import cn.edu.whut.springbear.gather.pojo.Teacher;
import cn.edu.whut.springbear.gather.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-10 10:18 Wednesday
 */
@Service
public class TeacherServiceImpl implements TeacherService {
    @Autowired
    private TeacherMapper teacherMapper;

    @Override
    public Teacher getTeacherByUserId(Integer userId) {
        return teacherMapper.getTeacherByUserId(userId);
    }

    @Override
    public boolean updateTeacherInfo(String newSex, String newPhone, String newEmail, Integer teacherId) {
        return teacherMapper.updateTeacherInfo(newSex, newPhone, newEmail, teacherId) == 1;
    }
}
