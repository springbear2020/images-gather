package edu.whut.bear.gather.service;

import edu.whut.bear.gather.pojo.Login;
import edu.whut.bear.gather.pojo.Record;
import edu.whut.bear.gather.pojo.Response;
import edu.whut.bear.gather.pojo.Upload;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Spring-_-Bear
 * @datetime 6/6/2022 8:11 PM
 */
@Service
public interface RecordService {
    /**
     * 保存用户登录记录
     *
     * @param login 登录记录
     * @return true - 保存成功
     */
    boolean saveLogin(Login login);

    /**
     * 保存用户记录（每日一条）
     *
     * @param record Record
     * @return true - 保存成功
     */
    boolean saveRecord(Record record);

    /**
     * 保存文件上传记录（七牛云）
     *
     * @param upload 文件上传记录
     * @return true - 保存成功
     */
    boolean saveUpload(Upload upload);

    /**
     * 批量插入文件上传记录并返回自增 ID
     *
     * @param uploads 多条文件上传记录
     * @return 对应记录的自增 ID
     */
    int[] saveUploadBatch(Upload... uploads);

    /**
     * 获取用户特定日期的记录
     *
     * @param userId 用户 ID
     * @param date   特定的日期
     * @return Record or null
     */
    Record getUserRecordByDate(Integer userId, Date date);

    /**
     * 更新用户记录状态
     *
     * @param record Record
     * @return true - 更新成功
     */
    boolean updateRecordState(Record record);

    /**
     * 获取并处理指定日期和班级的班级记录
     *
     * @param classNumber 班级 ID
     * @param date        日期
     * @return Response
     */
    Response processClassRecordList(Integer classNumber, Date date);

    /**
     * 获取指定日期和年级的未上传学生名单
     *
     * @param grade 年级 ID
     * @param date  日期
     * @return Response
     */
    Response getGradeUnUploadUserList(Integer grade, Date date);
}
