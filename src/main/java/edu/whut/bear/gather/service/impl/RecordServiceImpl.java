package edu.whut.bear.gather.service.impl;

import edu.whut.bear.gather.dao.LoginDao;
import edu.whut.bear.gather.dao.RecordDao;
import edu.whut.bear.gather.dao.UploadDao;
import edu.whut.bear.gather.pojo.Login;
import edu.whut.bear.gather.pojo.Record;
import edu.whut.bear.gather.pojo.Upload;
import edu.whut.bear.gather.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author Spring-_-Bear
 * @datetime 6/6/2022 8:11 PM
 */
@Service
public class RecordServiceImpl implements RecordService {
    @Autowired
    private LoginDao loginDao;
    @Autowired
    private RecordDao recordDao;
    @Autowired
    private UploadDao uploadDao;

    @Override
    public boolean saveLogin(Login login) {
        return loginDao.saveLogin(login) == 1;
    }

    @Override
    public boolean saveRecord(Record record) {
        return recordDao.saveRecord(record) == 1;
    }

    @Override
    public boolean saveUpload(Upload upload) {
        return uploadDao.saveUpload(upload) == 1;
    }

    @Override
    public int[] saveUploadBatch(Upload... uploads) {
        // 将多个 Upload 添加到 List 集合中
        List<Upload> uploadList = new ArrayList<>(Arrays.asList(uploads));
        // 保存到数据库
        int affectedRows = uploadDao.saveUploadBatch(uploadList);
        int[] res = new int[affectedRows];
        // 遍历经 MyBatis 保存后的 Record 对象，从中获取自增 ID
        for (int i = 0; i < affectedRows; i++) {
            res[i] = uploadList.get(i).getId();
        }
        return res;
    }

    @Override
    public Record getUserRecordByDate(Integer userId, Date date) {
        return recordDao.getUserRecordByDate(userId, date);
    }

    @Override
    public boolean updateRecordState(Record record) {
        return recordDao.updateRecordState(record) == 1;
    }
}
