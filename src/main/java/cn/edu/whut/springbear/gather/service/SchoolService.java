package cn.edu.whut.springbear.gather.service;

import cn.edu.whut.springbear.gather.pojo.Class;
import cn.edu.whut.springbear.gather.pojo.Grade;
import cn.edu.whut.springbear.gather.pojo.School;

import java.util.Date;
import java.util.List;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-13 09:18 Saturday
 */
public interface SchoolService {
    /**
     * Get the three images not completed classes list with total people numbers
     */
    List<Class> listNotCompletedClasses(Integer gradeId, Date specifiedDate);

    /**
     * Get the user real names who not sign in the system at specified date
     */
    List<String> listNotLoginNamesOfClass(Integer classId, Date specifiedDate);

    /**
     * Get the user real name list of the class who sign the system successfully,
     * but not upload the three images at specified date
     */
    List<String> listNotUploadNamesOfClass(Integer classId, Date specifiedDate);

    /**
     * Get the user real name list of the class who sign the system successfully
     * and upload the three images successfully at specified date
     */
    List<String> listCompletedNamesOfClass(Integer classId, Date specifiedDate);

    /**
     * Get the class with total numbers who not sign in the system and not upload three images
     */
    Class getClassNotCompletedDetails(Integer classId, Date specifiedDate);

    /**
     * Get all schools
     */
    List<School> listAllSchools();

    /**
     * Get all grades of the school
     */
    List<Grade> listGradesOfSchool(Integer schoolId);

    /**
     * Get all classes of the grade
     */
    List<Class> listClassesOfGrade(Integer gradeId);
}
