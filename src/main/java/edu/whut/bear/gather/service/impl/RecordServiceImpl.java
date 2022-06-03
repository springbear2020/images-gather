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

import java.util.Date;

/**
 * @author Spring-_-Bear
 * @datetime 6/3/2022 12:59 AM
 */
@Service
public class RecordServiceImpl implements RecordService {
    @Autowired
    private UploadDao uploadDao;
    @Autowired
    private LoginDao loginDao;
    @Autowired
    private RecordDao recordDao;

    @Override
    public boolean saveLogin(Login login) {
        return loginDao.saveLogin(login) == 1;
    }

    @Override
    public boolean saveUpload(Upload upload) {
        return uploadDao.saveUpload(upload) == 1;
    }

    @Override
    public boolean saveRecord(Record record) {
        return recordDao.saveRecord(record) == 1;
    }

    @Override
    public Record getUserRecordToday(Integer userId, Date date) {
        return recordDao.getUserRecordByDate(userId, date);
    }

    @Override
    public boolean updateRecordState(Record record) {
        return recordDao.updateRecordState(record) == 1;
    }
}
