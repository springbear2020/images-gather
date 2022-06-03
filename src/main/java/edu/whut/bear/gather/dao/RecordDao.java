package edu.whut.bear.gather.dao;

import edu.whut.bear.gather.pojo.Record;
import org.springframework.web.bind.annotation.ResponseBody;


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
}
