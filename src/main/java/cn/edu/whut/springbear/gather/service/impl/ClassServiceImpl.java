package cn.edu.whut.springbear.gather.service.impl;

import cn.edu.whut.springbear.gather.mapper.*;
import cn.edu.whut.springbear.gather.pojo.*;
import cn.edu.whut.springbear.gather.pojo.Class;
import cn.edu.whut.springbear.gather.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private GradeMapper gradeMapper;
    @Autowired
    private SchoolMapper schoolMapper;
    @Autowired
    private PeopleMapper peopleMapper;

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

    @Override
    @Transactional
    public int classDataInsertBatch(List<People> peopleList) {
        People monitor = peopleList.get(1);
        // Check the status of the class, grade and school, if it not exists then insert
        String schoolName = monitor.getSchool();
        School school = schoolMapper.getSchoolByName(schoolName);
        if (school == null) {
            school = new School(schoolName);
            schoolMapper.saveSchool(school);
        }
        String gradeName = monitor.getGrade();
        Grade grade = gradeMapper.getGradeByName(gradeName);
        if (grade == null) {
            grade = new Grade(gradeName);
            gradeMapper.saveGrade(grade);
            // Save the correspondence between schools and grades
            gradeMapper.saveSchoolGrade(school.getId(), grade.getId());
        }
        String className = monitor.getClassName();
        Class aClass = classMapper.getClassByName(className);
        if (aClass == null) {
            aClass = new Class(className);
            classMapper.saveClass(aClass);
            // Save the correspondence between grades and classes
            classMapper.saveGradeClass(grade.getId(), aClass.getId());
        }

        int affectedRows = 0;
        // Traverse the all people list
        for (People people : peopleList) {
            // Register user
            User user = new User(people.getNumber(), people.getNumber(), new Date(), people.getPhone(), people.getEmail(), User.STATUS_NORMAL, User.TYPE_STUDENT);
            if (userMapper.saveUser(user) == 1) {
                people.setUserId(user.getId());
                people.setClassId(aClass.getId());
                people.setGradeId(grade.getId());
                people.setSchoolId(school.getId());
                // Save people
                if (peopleMapper.savePeople(people) == 1) {
                    affectedRows++;
                }
            }
        }
        return affectedRows;
    }
}
