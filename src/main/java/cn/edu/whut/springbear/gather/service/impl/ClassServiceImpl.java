package cn.edu.whut.springbear.gather.service.impl;

import cn.edu.whut.springbear.gather.mapper.ClassMapper;
import cn.edu.whut.springbear.gather.pojo.Class;
import cn.edu.whut.springbear.gather.pojo.Upload;
import cn.edu.whut.springbear.gather.pojo.User;
import cn.edu.whut.springbear.gather.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-13 09:00 Saturday
 */
@Service
public class ClassServiceImpl implements ClassService {
    @Autowired
    private ClassMapper classMapper;

    @Override
    public List<String> queryNotLoginNamesOfClass(Integer classId, Date specifiedDate) {
        return classMapper.getNotLoginNamesOfClass(classId, specifiedDate, User.TYPE_MONITOR);
    }

    @Override
    public List<String> queryNotUploadNamesOfClass(Integer classId, Date specifiedDate) {
        return classMapper.getNamesOfClassByUploadStatus(classId, specifiedDate, Upload.STATUS_NOT_UPLOAD);
    }

    @Override
    public List<String> queryCompletedNamesOfClass(Integer classId, Date specifiedDate) {
        return classMapper.getNamesOfClassByUploadStatus(classId, specifiedDate, Upload.STATUS_COMPLETED);
    }

    @Override
    public Class getClassNotCompleted(Integer classId, Date specifiedDate) {
        List<String> notLoginNames = this.queryNotLoginNamesOfClass(classId, specifiedDate);
        List<String> notUploadNames = this.queryNotUploadNamesOfClass(classId, specifiedDate);
        Class classInfo = classMapper.getClassById(classId);
        classInfo.setNotCompletedNums(notLoginNames.size() + notUploadNames.size());
        return classInfo;
    }
}
