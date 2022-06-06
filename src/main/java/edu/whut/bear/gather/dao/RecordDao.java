package edu.whut.bear.gather.dao;

import edu.whut.bear.gather.pojo.Record;
import org.springframework.stereotype.Repository;

/**
 * @author Spring-_-Bear
 * @datetime 6/6/2022 8:46 PM
 */
@Repository
public interface RecordDao {
    /**
     * 保存用户记录（每日一份）
     *
     * @param record Record
     * @return 1 - Save successfully
     */
    int saveRecord(Record record);
}
