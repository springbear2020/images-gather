package cn.edu.whut.springbear.gather.service;


import cn.edu.whut.springbear.gather.pojo.Email;
import cn.edu.whut.springbear.gather.pojo.Login;
import cn.edu.whut.springbear.gather.pojo.Upload;
import com.github.pagehelper.PageInfo;

import java.util.Date;
import java.util.List;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-11 00:10 Thursday
 */
public interface RecordService {
    /**
     * Save login log of the user
     */
    boolean saveLoginLog(String ip, Integer userId);

    /**
     * Create the upload of user on today
     */
    boolean createUserUploadToday(Integer userId);

    /**
     * Save verify code send log
     */
    boolean saveEmailLog(Email email);

    /**
     * Get the upload of the user filter by date and status
     */
    Upload getUserUpload(Integer userId, Date date, Integer uploadStatus);

    /**
     * Update the upload of user, including uploadStatus, uploadDateTime, three images' local and cloud access url
     */
    boolean updateUpload(Upload upload);

    /**
     * Get the login page data of user
     */
    PageInfo<Login> listUserLoginLogPageData(Integer userId, Integer pageNum);

    /**
     * Get the upload page data of user
     */
    PageInfo<Upload> listUserUploadPageData(Integer userId, Integer uploadStatus, Integer pageNum);

    /**
     * Get the upload list of class contains the relevant user real name on specified date
     */
    List<Upload> listUploadsOfClass(Integer classId, Integer uploadStatus, Date date);

    /**
     * Get the latest login log data of the user
     */
    Login getUserLatestLoginLog(Integer userId);
}
