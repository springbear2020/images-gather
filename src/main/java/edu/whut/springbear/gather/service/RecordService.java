package edu.whut.springbear.gather.service;

import edu.whut.springbear.gather.pojo.LoginLog;
import edu.whut.springbear.gather.pojo.Upload;
import org.springframework.stereotype.Service;

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
}
