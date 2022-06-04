package edu.whut.bear.gather.dao;

import edu.whut.bear.gather.pojo.Record;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;


/**
 * @author Spring-_-Bear
 * @datetime 6/3/2022 12:33 PM
 */
@ResponseBody
public interface RecordDao {
    /**
     * Save the user's upload record
     *
     * @param record Record
     * @return 1 - Save successfully
     */
    int saveRecord(Record record);

    /**
     * Get the user's upload by limit time
     *
     * @param userId Id of user
     * @param date   Date
     * @return Record or null
     */
    Record getUserRecordByDate(@Param("userId") Integer userId, @Param("date") Date date);

    /**
     * Update the upload record, update the info below,
     * healthUploadId,healthImageUrl,
     * scheduleUploadId,scheduleImageUrl,
     * closedUploadId,closedImageUrl
     *
     * @param record Record
     * @return 1 - Update successfully
     */
    int updateRecordState(Record record);

    /**
     * Get the record of the admin class by the specified day
     *
     * @param classNumber Number of the admin class
     * @param date        Date
     * @return Record list or null
     */
    List<Record> getAdminClassRecordByDate(@Param("classNumber") Integer classNumber, @Param("date") Date date);
}
