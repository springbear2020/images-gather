package cn.edu.whut.springbear.gather.service;

import cn.edu.whut.springbear.gather.pojo.Teacher;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-10 10:18 Wednesday
 */
public interface TeacherService {
    /**
     * Get teacher by user id
     */
    Teacher getTeacherByUserId(Integer userId);

    /**
     * Update the sex, phone and email info of the teacher by teacher id
     */
    boolean updateTeacherInfo(String newSex, String newPhone, String newEmail, Integer teacherId);
}
