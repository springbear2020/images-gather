package edu.whut.bear.gather.service;

import edu.whut.bear.gather.pojo.Login;
import edu.whut.bear.gather.pojo.Record;
import edu.whut.bear.gather.pojo.Upload;
import org.springframework.stereotype.Service;

/**
 * @author Spring-_-Bear
 * @datetime 6/3/2022 12:58 AM
 */
@Service
public interface RecordService {
    /**
     * Save the log record of user login
     *
     * @param login Record of user login
     * @return true - Save successfully
     */
    boolean saveLogin(Login login);

    /**
     * Save the file upload record of the user
     *
     * @param upload File upload record
     * @return true - Save successfully
     */
    boolean saveUpload(Upload upload);

    /**
     * Save the user's upload record
     *
     * @param record Record
     * @return true - Save successfully
     */
    boolean saveRecord(Record record);
}
