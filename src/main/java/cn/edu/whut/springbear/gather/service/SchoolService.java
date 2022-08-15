package cn.edu.whut.springbear.gather.service;

import cn.edu.whut.springbear.gather.pojo.Class;
import cn.edu.whut.springbear.gather.pojo.Grade;
import cn.edu.whut.springbear.gather.pojo.People;
import cn.edu.whut.springbear.gather.pojo.School;

import java.util.Date;
import java.util.List;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-13 09:18 Saturday
 */
public interface SchoolService {
    /**
     * Get the three images not completed classes list with total numbers at specified date
     */
    List<Class> getNotCompletedClasses(Integer gradeId, Date specifiedDate);

    /**
     * Get the people name list who not sign in the system at specified date
     */
    List<String> queryNotLoginNamesOfClass(Integer classId, Date specifiedDate);

    /**
     * Get the people name list of the class who sign the system successfully
     * but not upload the three images at specified date
     */
    List<String> queryNotUploadNamesOfClass(Integer classId, Date specifiedDate);

    /**
     * Get the people name list of the class who sign the system successfully
     * and upload the three images successfully at specified date
     */
    List<String> queryCompletedNamesOfClass(Integer classId, Date specifiedDate);

    /**
     * Get the numbers of the class who don't upload the three images with class name
     */
    Class getClassNotCompleted(Integer classId, Date specifiedDate);

    /**
     * Insert the class data in batch, and auto register user
     */
    int classDataInsertBatch(List<People> peopleList);

    /**
     * Get all schools
     */
    List<School> getAllSchools();

    /**
     * Get all grades of the school
     */
    List<Grade> getGradesOfSchool(Integer schoolId);

    /**
     * Get all classes of the school in specified grade
     */
    List<Class> getClassesOfSchool(Integer gradeId);

    /**
     * Get all people list in the specified class
     */
    List<People> getClassPeopleList(Integer classId);
}
