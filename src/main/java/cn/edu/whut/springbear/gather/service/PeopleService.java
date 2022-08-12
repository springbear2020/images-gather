package cn.edu.whut.springbear.gather.service;

import cn.edu.whut.springbear.gather.pojo.People;

import java.util.Date;
import java.util.List;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-11 15:16 Thursday
 */
public interface PeopleService {
    /**
     * Get people info by user id
     */
    People queryPeople(Integer userId);

    /**
     * Update the info of the people, including sex, email and phone number
     */
    boolean updatePeopleInfo(String newSex, String newEmail, String newPhone, Integer id);

    /**
     * Get the people name list who not sign in the system at specified date
     */
    List<String> queryNotLoginNamesOfClass(String className, Date specifiedDate);

    /**
     * Get the people name list of the class who sign the system successfully
     * but not upload the three images at specified date
     */
    List<String> queryNotUploadNamesOfClass(String className, Date specifiedDate);

    /**
     * Get the people name list of the class who sign the system successfully
     * and upload the three images successfully at specified date
     */
    List<String> queryCompletedNamesOfClass(String className, Date specifiedDate);
}
