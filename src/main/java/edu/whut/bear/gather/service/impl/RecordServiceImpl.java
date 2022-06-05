package edu.whut.bear.gather.service.impl;

import edu.whut.bear.gather.dao.LoginDao;
import edu.whut.bear.gather.dao.RecordDao;
import edu.whut.bear.gather.dao.UploadDao;
import edu.whut.bear.gather.dao.UserDao;
import edu.whut.bear.gather.pojo.*;
import edu.whut.bear.gather.service.RecordService;
import edu.whut.bear.gather.util.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    @Autowired
    private UserDao userDao;
    @Autowired
    private PropertyUtils propertyUtils;

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
    public Response processRecordList(Integer classNumber, Date date) {
        List<Record> recordList = new ArrayList<>();
        // Get user not login list
        List<User> recordNotCreatedUserList = userDao.getUserRecordNotCreated(classNumber, date);
        List<String> notLoginUserList = new ArrayList<>();
        for (User user : recordNotCreatedUserList) {
            // public Record(Integer id, Integer userId, String realName, Integer classNumber, String className, Integer healthUploadId, Integer scheduleUploadId, Integer closedUploadId, Date uploadDate, String healthImageUrl, String scheduleImageUrl, String closedImageUrl) {
            int defaultNum = -1;
            String defaultImage = propertyUtils.getContextUrl() + "static/img/5.png";
            Record record = new Record(null, user.getId(), user.getUsername(), user.getRealName(), user.getClassNumber(), user.getClassName(), defaultNum, defaultNum, defaultNum, null, defaultImage, defaultImage, defaultImage);
            record.setUploaded(Record.NO);
            recordList.add(record);
            notLoginUserList.add(user.getRealName());
        }

        // Get user login but don't upload the images
        List<Record> notUploadedList = recordDao.getLoginByNotUploaded(classNumber, date);
        List<String> notUploadedUserList = new ArrayList<>();
        for (Record record : notUploadedList) {
            record.setUploaded(Record.NO);
            recordList.add(record);
            notUploadedUserList.add(record.getRealName());
        }

        // Get record uploaded successfully
        List<Record> uploadedList = recordDao.getRecordUploaded(classNumber, date);
        for (Record record : uploadedList) {
            record.setUploaded(Record.YES);
            recordList.add(record);
        }
        return Response.success("Get record list successfully").put("notLoginUserList", notLoginUserList).put("notUploadedUserList", notUploadedUserList).put("recordList", recordList);
    }
}
