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
    @Autowired
    private UserDao userDao;
    @Autowired
    private PropertyUtils propertyUtils;

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

    @Override
    public Response processClassRecordList(Integer classNumber, Date date) {
        // 班级全部记录集合：未登录、登录未上传、已上传人员组成
        List<Record> classRecordList = new ArrayList<>();

        // 获取未登录人员名单
        List<User> unLoginUserList = userDao.getClassUserListNotLogin(classNumber, date);
        String unLoginImageUrl = propertyUtils.getContextPath() + "static/img/unLogin.png";
        // 遍历未登录人员名单，为每人生成一条 Record
        for (User user : unLoginUserList) {
            classRecordList.add(new Record(user.getId(), user.getUsername(), user.getRealName(), user.getClassNumber(), user.getClassName(), Record.NO,
                    null, null, null, null, unLoginImageUrl, unLoginImageUrl, unLoginImageUrl));
        }

        // 获取登录但未图片未上传记录
        List<Record> loginNotUploadList = recordDao.getClassRecordList(classNumber, date, Record.NO);
        // 登录且已上传记录
        List<Record> longinUploadList = recordDao.getClassRecordList(classNumber, date, Record.YES);

        classRecordList.addAll(loginNotUploadList);
        classRecordList.addAll(longinUploadList);
        // 给客户端返回已上传人数、班级所有成员记录、未登录人员、登录未上传记录，由客户端解析以避免降低服务器性能
        return Response.success("成功获取本班上传记录").put("uploadedNumbers", longinUploadList.size())
                .put("classRecordList", classRecordList).put("unLoginUserList", unLoginUserList).put("loginNotUploadList", loginNotUploadList);
    }

    @Override
    public Response getGradeUnUploadUserList(Integer grade, Date date) {
        // 该年级未登录学生名单
        List<User> gradeNotLoginUserList = userDao.getGradeUserListNotLogin(grade, date);
        // 该年级登录未上传记录
        List<Record> loginNotUploadRecordList = recordDao.getGradeRecordList(grade, date, Record.NO);
        return Response.success("成功获取到年级记录").put("gradeSize", User.GRADE_SIZE)
                .put("userList", gradeNotLoginUserList).put("recordList", loginNotUploadRecordList);
    }
}
