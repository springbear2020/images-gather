package cn.edu.whut.springbear.gather.service.impl;

import cn.edu.whut.springbear.gather.mapper.EmailLogMapper;
import cn.edu.whut.springbear.gather.mapper.LoginLogMapper;
import cn.edu.whut.springbear.gather.mapper.UploadMapper;
import cn.edu.whut.springbear.gather.pojo.EmailLog;
import cn.edu.whut.springbear.gather.pojo.LoginLog;
import cn.edu.whut.springbear.gather.pojo.Upload;
import cn.edu.whut.springbear.gather.service.RecordService;
import cn.edu.whut.springbear.gather.service.StudentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-08 22:06 Monday
 */
@Service
public class RecordServiceImpl implements RecordService {
    private static final int PAGE_DATA_ROWS = 10;
    private static final int PAGE_NUMS = 7;

    @Autowired
    private LoginLogMapper loginLogMapper;
    @Autowired
    private UploadMapper uploadMapper;
    @Autowired
    private EmailLogMapper emailLogMapper;
    @Autowired
    private StudentService studentService;

    @Override
    public PageInfo<LoginLog> getUserLoginLogPageData(Integer userId, Integer pageNum) {
        PageHelper.startPage(pageNum, PAGE_DATA_ROWS);
        List<LoginLog> loginList = loginLogMapper.getUserLoginLog(userId);
        return new PageInfo<>(loginList, PAGE_NUMS);
    }

    @Override
    public boolean saveLoginLog(LoginLog loginLog) {
        return loginLogMapper.saveLoginLog(loginLog) == 1;
    }

    @Override
    public boolean saveUpload(Upload upload) {
        return uploadMapper.saveUpload(upload) == 1;
    }

    @Override
    public boolean updateUpload(Upload upload) {
        return uploadMapper.updateUserUploadImagesUrl(upload) == 1;
    }

    @Override
    public PageInfo<Upload> getUserUploadPageData(Integer userId, Integer uploadStatus, Integer pageNum) {
        PageHelper.startPage(pageNum, PAGE_DATA_ROWS);
        List<Upload> uploadList = uploadMapper.getUserAllUploads(userId, uploadStatus);
        return new PageInfo<>(uploadList, PAGE_NUMS);
    }

    @Override
    public Upload getUserUploadInSpecifiedDate(Integer userId, Integer uploadStatus, Date specifiedDate) {
        return uploadMapper.getUserUploadAtSpecifiedDate(userId, uploadStatus, specifiedDate);
    }

    @Override
    public List<Upload> getClassUploadListWithStudent(Integer uploadStatus, Date specifiedDate, String className) {
        return uploadMapper.getClassUploadListWithStudent(uploadStatus, specifiedDate, className);
    }

    @Override
    public boolean saveEmailLog(EmailLog emailLog) {
        return emailLogMapper.saveEmailLog(emailLog) == 1;
    }

    @Override
    public LoginLog getLatestLoginLog(Integer userId) {
        return loginLogMapper.getLatestLoginLog(userId);
    }

    @Override
    public List<List<String>> getClassUploadStudents(String className, Date specifiedDate) {
        // Get the class student list someone who not sign in the system
        List<String> notLoginStudentNames = studentService.getClassStudentNameListNotLogin(className, specifiedDate);
        // Get the class student list someone who sign in the system but not upload the images
        List<String> notUploadStudentNames = studentService.getClassStudentNameListByUploadStatus(Upload.STATUS_NOT_UPLOAD, className, specifiedDate);
        // Get the class student list someone who upload the images successfully
        List<String> completedStudentNames = studentService.getClassStudentNameListByUploadStatus(Upload.STATUS_UPLOADED, className, specifiedDate);
        List<List<String>> res = new ArrayList<>();
        res.add(notLoginStudentNames);
        res.add(notUploadStudentNames);
        res.add(completedStudentNames);
        return res;
    }
}
