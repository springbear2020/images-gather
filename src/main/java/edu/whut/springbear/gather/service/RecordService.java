package edu.whut.springbear.gather.service;

import com.github.pagehelper.PageInfo;
import edu.whut.springbear.gather.pojo.LoginLog;
import edu.whut.springbear.gather.pojo.Upload;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Spring-_-Bear
 * @datetime 2022-06-30 21:55 Thursday
 */
@Service
public interface RecordService {
    /**
     * Save the login log of the user
     *
     * @param loginLog Log of login
     * @return boolean - Save successfully
     */
    boolean saveLoginLog(LoginLog loginLog);

    /**
     * Save the images upload record of the user
     *
     * @param upload Upload record of user
     * @return true - Save successfully
     */
    boolean saveUserUploadRecord(Upload upload);

    /**
     * Update the upload record of user in specified user,
     * including uploadStatus, uploadDateTime, and three images' local and cloud access url
     *
     * @param upload Upload record
     * @return true - Update successfully
     */
    boolean updateImagesAccessUrl(Upload upload);

    /**
     * Get the upload record of the user by id of user in the specified date,
     * and filter the results by the upload record status
     *
     * @param userId Id of user
     * @return Upload or null
     */
    Upload getUserUploadInSpecifiedDate(Integer userId, Integer uploadStatus, Date specifiedDate);


    /**
     * Get the three images' access url in the upload record
     *
     * @param contextPath Context path of the web project
     * @param upload      Upload
     * @return String[0]-healthImageAccessUrl String[0]-scheduleImageAccessUrl String[0]-closedImageAccessUrl
     */
    String[] getThreeImagesAccessUrl(String contextPath, Upload upload);

    /**
     * Get the user's login page data
     *
     * @param userId Id of user
     * @return Login page data or null
     */
    PageInfo<LoginLog> getUserLoginPageData(Integer userId, Integer pageNum);

    /**
     * Get the all upload record history page data of the user
     *
     * @param userId       Id of user
     * @param uploadStatus Status of upload record
     * @param pageNum      Number of the page
     * @return Upload data or null
     */
    PageInfo<Upload> getUserUploadHistory(Integer userId, Integer uploadStatus, Integer pageNum);
}
