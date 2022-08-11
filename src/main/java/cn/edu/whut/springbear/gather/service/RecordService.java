package cn.edu.whut.springbear.gather.service;


import cn.edu.whut.springbear.gather.pojo.EmailLog;
import cn.edu.whut.springbear.gather.pojo.LoginLog;
import cn.edu.whut.springbear.gather.pojo.Upload;
import com.github.pagehelper.PageInfo;

import java.util.Date;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-11 00:10 Thursday
 */
public interface RecordService {
    /**
     * Save the login log of the user
     */
    boolean saveLoginLog(String ip, Integer userId);

    /**
     * Create the upload record of student on today
     */
    boolean createStudentUploadToday(Integer userId);

    /**
     * Save the verify code sent record
     */
    boolean saveEmailLog(EmailLog emailLog);

    /**
     * Get the upload record of the student filter by date and status of upload
     */
    Upload getStudentUpload(Integer userId, Date date, Integer uploadStatus);

    /**
     * Update the upload record of user, including uploadStatus, uploadDateTime, three images' local and cloud access url
     */
    boolean updateUploadImagesUrl(Upload upload);

    /**
     * Get the login page data of user
     */
    PageInfo<LoginLog> getUserLoginLogPageData(Integer userId, Integer pageNum);

    /**
     * Get the upload record history page data of the user
     */
    PageInfo<Upload> getUserUploadPageData(Integer userId, Integer uploadStatus, Integer pageNum);
}
