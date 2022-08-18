package cn.edu.whut.springbear.gather.service.impl;

import cn.edu.whut.springbear.gather.mapper.*;
import cn.edu.whut.springbear.gather.pojo.*;
import cn.edu.whut.springbear.gather.pojo.Class;
import cn.edu.whut.springbear.gather.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-13 09:20 Saturday
 */
@Service
public class SchoolServiceImpl implements SchoolService {
    @Autowired
    private GradeMapper gradeMapper;
    @Autowired
    private ClassMapper classMapper;
    @Autowired
    private SchoolMapper schoolMapper;

    @Override
    public List<Class> listNotCompletedClasses(Integer gradeId, Date specifiedDate) {
        List<Class> classesList = new ArrayList<>();
        // Get the classes id list of current grade
        List<Integer> classIdList = gradeMapper.listClassIdsOfGrade(gradeId);
        for (Integer classId : classIdList) {
            classesList.add(getClassNotCompletedDetails(classId, specifiedDate));
        }
        return classesList;
    }

    @Override
    public List<String> listNotLoginNamesOfClass(Integer classId, Date specifiedDate) {
        return classMapper.listNotLoginNamesOfClass(classId, specifiedDate, User.TYPE_MONITOR);
    }

    @Override
    public List<String> listNotUploadNamesOfClass(Integer classId, Date specifiedDate) {
        return classMapper.listUploadNamesOfClass(classId, specifiedDate, Upload.STATUS_NOT_UPLOAD);
    }

    @Override
    public List<String> listCompletedNamesOfClass(Integer classId, Date specifiedDate) {
        return classMapper.listUploadNamesOfClass(classId, specifiedDate, Upload.STATUS_COMPLETED);
    }

    @Override
    public Class getClassNotCompletedDetails(Integer classId, Date specifiedDate) {
        List<String> notLoginNames = this.listNotLoginNamesOfClass(classId, specifiedDate);
        List<String> notUploadNames = this.listNotUploadNamesOfClass(classId, specifiedDate);
        Class classInfo = classMapper.getClass(classId);
        classInfo.setNotCompletedNums(notLoginNames.size() + notUploadNames.size());
        return classInfo;
    }

    @Override
    public List<School> listAllSchools() {
        return schoolMapper.listSchools();
    }

    @Override
    public List<Grade> listGradesOfSchool(Integer schoolId) {
        return gradeMapper.listGradesOfSchool(schoolId);
    }

    @Override
    public List<Class> listClassesOfGrade(Integer gradeId) {
        return classMapper.listClassesOfGrade(gradeId);
    }
}
