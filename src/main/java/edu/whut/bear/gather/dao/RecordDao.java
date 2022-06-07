package edu.whut.bear.gather.dao;

import edu.whut.bear.gather.pojo.Record;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

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

    /**
     * 获取用户特定日期的记录
     *
     * @param userId 用户 ID
     * @param date   特定的日期
     * @return Record or null
     */
    Record getUserRecordByDate(@Param("userId") Integer userId, @Param("date") Date date);

    /**
     * 更新用户记录状态
     *
     * @param record Record
     * @return 1 - 更新成功
     */
    int updateRecordState(Record record);
}
