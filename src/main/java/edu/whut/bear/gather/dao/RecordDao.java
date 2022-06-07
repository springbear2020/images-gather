package edu.whut.bear.gather.dao;

import edu.whut.bear.gather.pojo.Record;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

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


    /**
     * 获取指定班级、日期、记录状态的学生名单
     *
     * @param classNumber 班级 ID
     * @param date        日期
     * @param status      记录状态
     * @return Record list
     */
    List<Record> getClassRecordList(@Param("classNumber") Integer classNumber, @Param("date") Date date, @Param("status") Integer status);

    /**
     * 获取指定年级、日期、记录状态的学生名单
     *
     * @param grade  年级 ID
     * @param date   日期
     * @param status 记录状态
     * @return Record list
     */
    List<Record> getGradeRecordList(@Param("grade") Integer grade, @Param("date") Date date, @Param("status") Integer status);
}
