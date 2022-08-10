package cn.edu.whut.springbear.gather.service;

import cn.edu.whut.springbear.gather.pojo.EmailLog;
import cn.edu.whut.springbear.gather.pojo.LoginLog;
import cn.edu.whut.springbear.gather.pojo.Upload;
import com.github.pagehelper.PageInfo;

import java.util.Date;
import java.util.List;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-08 22:02 Monday
 */
public interface RecordService {
    /**
     * Get the user's login page data
     */
    PageInfo<LoginLog> getUserLoginLogPageData(Integer userId, Integer pageNum);

    /**
     * Save the login log of the user
     */
    boolean saveLoginLog(LoginLog loginLog);

    /**
     * Save the images upload record of the user
     */
    boolean saveUpload(Upload upload);

    /**
     * Update the upload record of user in specified user,
     * including uploadStatus, uploadDateTime, and three images' local and cloud access url
     */
    boolean updateUpload(Upload upload);

    /**
     * Get the all upload record history page data of the user
     */
    PageInfo<Upload> getUserUploadPageData(Integer userId, Integer uploadStatus, Integer pageNum);

    /**
     * Get the upload record of the user by id of user in the specified date,
     * and filter the results by the upload record status
     */
    Upload getUserUploadInSpecifiedDate(Integer userId, Integer uploadStatus, Date specifiedDate);

    /**
     * Get the upload list of the class with student information at the specified date who signed in the system successfully,
     * filtered the results set by the status of the upload record
     *
     * @param uploadStatus  Status of the upload record
     * @param className     Name of class
     * @param specifiedDate Specified date
     * @return Upload list with student or null
     */
    List<Upload> getClassUploadListWithStudent(Integer uploadStatus, Date specifiedDate, String className);

    /**
     * Save the verify code sent record
     */
    @SuppressWarnings("all")
    boolean saveEmailLog(EmailLog emailLog);

    /**
     * Get the user's latest login log data
     */
    LoginLog getLatestLoginLog(Integer userId);

    /**
     * Get the student list about the class upload record,
     * including not login list, not upload list and completed student list
     *
     * @param className     Name of class
     * @param specifiedDate Specified date
     * @return List[0]: not login;  List[1]: not upload;    List[2]: completed
     */
    List<List<String>> getClassUploadStudents(String className, Date specifiedDate);
}
