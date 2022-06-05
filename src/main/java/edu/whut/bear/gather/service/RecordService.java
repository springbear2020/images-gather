package edu.whut.bear.gather.service;

import edu.whut.bear.gather.pojo.Login;
import edu.whut.bear.gather.pojo.Record;
import edu.whut.bear.gather.pojo.Response;
import edu.whut.bear.gather.pojo.Upload;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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

    /**
     * Get the upload record of user, limit the time is today
     *
     * @param userId Id of user
     * @param date   Date
     * @return Record or null
     */
    Record getUserRecordToday(Integer userId, Date date);

    /**
     * Update the upload record, update the info below,
     * healthUploadId,healthImageUrl,
     * scheduleUploadId,scheduleImageUrl,
     * closedUploadId,closedImageUrl
     *
     * @param record Record
     * @return true - Update successfully
     */
    boolean updateRecordState(Record record);

    /**
     * Get the record of the class
     *
     * @param classNumber Number of class
     * @param date        Date
     * @return Record list or null
     */
    List<Record> getClassRecord(Integer classNumber, Date date);

    /**
     * Process with the upload record list of the class
     *
     * @param classNumber Number of class
     * @param date        Date
     * @return Response
     */
    Response processRecordList(Integer classNumber, Date date);
}
