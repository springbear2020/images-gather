package cn.edu.whut.springbear.gather.service;

import cn.edu.whut.springbear.gather.pojo.Student;

import java.util.Date;
import java.util.List;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-08 16:28 Monday
 */
public interface StudentService {
    /**
     * Get student by user id
     */
    Student getStudentByUserId(Integer userId);

    /**
     * Update the sex, phone and email info of the student by student id
     */
    boolean updateStudentInfo(String newSex, String newPhone, String newEmail, Integer id);

    /**
     * Get the student list of the class at the specified date who not sign in the system,
     * someone not sign in the system meaning his/her upload record was not created
     */
    List<Student> getClassNotLoginList(String className, Date specifiedDate);

    /**
     * Get the student list of the class at the specified date who signed in the system successfully,
     * filtered the results set by the status of the upload record
     */
    List<Student> getClassUploadList(Integer uploadStatus, String className, Date specifiedDate);

    /**
     * Get the student name list of the class at the specified date who not sign in the system,
     * someone not sign in the system meaning his/her upload record was not created
     */
    List<String> getClassStudentNameListNotLogin(String className, Date specifiedDate);

    /**
     * Get the student name list of the class at the specified date who signed in the system successfully,
     * filtered the results set by the status of the upload record
     */
    List<String> getClassStudentNameListByUploadStatus(Integer uploadStatus, String className, Date specifiedDate);
}
