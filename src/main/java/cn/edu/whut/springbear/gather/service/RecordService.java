package cn.edu.whut.springbear.gather.service;


import cn.edu.whut.springbear.gather.pojo.EmailLog;
import cn.edu.whut.springbear.gather.pojo.Upload;

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
}
