package edu.whut.bear.gather.service.impl;

import edu.whut.bear.gather.dao.LoginDao;
import edu.whut.bear.gather.dao.RecordDao;
import edu.whut.bear.gather.dao.UploadDao;
import edu.whut.bear.gather.pojo.Login;
import edu.whut.bear.gather.pojo.Record;
import edu.whut.bear.gather.pojo.Response;
import edu.whut.bear.gather.pojo.Upload;
import edu.whut.bear.gather.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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

    @Override
    public List<Record> getClassRecord(Integer classNumber, Date date) {
        return recordDao.getAdminClassRecordByDate(classNumber, date);
    }

    @Override
    public Response processRecordList(List<Record> recordList) {
        if (recordList == null || recordList.size() == 0) {
            return Response.info("暂无用户上传记录");
        }

        Response response = Response.success("Get record successfully").put("size", recordList.size());
        // Set the upload state of the record
        for (Record record : recordList) {
            if (record.getHealthUploadId() == null || record.getHealthUploadId() == -1 ||
                    record.getScheduleUploadId() == null || record.getScheduleUploadId() == -1 ||
                    record.getClosedUploadId() == null || record.getClosedUploadId() == -1) {
                record.setUploaded(Record.NO);
            } else {
                record.setUploaded(Record.YES);
            }
        }
        return response.put("recordList", recordList);
    }
}
