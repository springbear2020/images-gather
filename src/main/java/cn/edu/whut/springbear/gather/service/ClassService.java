package cn.edu.whut.springbear.gather.service;

import cn.edu.whut.springbear.gather.pojo.Class;
import cn.edu.whut.springbear.gather.pojo.People;

import java.util.Date;
import java.util.List;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-13 08:59 Saturday
 */
public interface ClassService {
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
}
