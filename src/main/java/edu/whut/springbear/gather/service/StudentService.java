package edu.whut.springbear.gather.service;

import edu.whut.springbear.gather.pojo.Student;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author Spring-_-Bear
 * @datetime 2022-07-05 21:04 Tuesday
 */
@Repository
public interface StudentService {
    /**
     * Save the student info then save the number as the username and password to the t_user table
     *
     * @param student Student
     * @return 1 - Save successfully
     */
    int saveStudent(Student student);

    /**
     * Get the student list of the class at the specified date who not sign in the system,
     * someone not sign in the system meaning his/her upload record was not created
     *
     * @param className     Name of the class
     * @param specifiedDate Specified date
     * @return Student list or null
     */
    List<Student> getClassStudentsNotSignInOnDate(String className, Date specifiedDate);

    /**
     * Get the student list of the class at the specified date who signed in the system successfully,
     * filtered the results set by the status of the upload record
     *
     * @param uploadStatus  Status of the upload record
     * @param className     Name of class
     * @param specifiedDate SpecifiedDate
     * @return Student list or null
     */
    List<Student> getClassStudentsNotUploadOnDate(Integer uploadStatus, String className, Date specifiedDate);
}