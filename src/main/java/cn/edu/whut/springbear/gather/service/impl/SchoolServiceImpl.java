package cn.edu.whut.springbear.gather.service.impl;

import cn.edu.whut.springbear.gather.mapper.*;
import cn.edu.whut.springbear.gather.pojo.*;
import cn.edu.whut.springbear.gather.pojo.Class;
import cn.edu.whut.springbear.gather.service.SchoolService;
import cn.edu.whut.springbear.gather.util.DateUtils;
import cn.edu.whut.springbear.gather.util.MD5Utils;
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
    private UserMapper userMapper;
    @Autowired
    private SchoolMapper schoolMapper;
    @Autowired
    private PeopleMapper peopleMapper;

    @Override
    public List<Class> getNotCompletedClasses(Integer gradeId, Date specifiedDate) {
        List<Class> res = new ArrayList<>();
        // Get the classes id list of current grade
        List<Integer> classIdList = gradeMapper.getClassesOfGrade(gradeId);
        for (Integer classId : classIdList) {
            res.add(getClassNotCompleted(classId, specifiedDate));
        }
        return res;
    }

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
    public int classDataInsertBatch(List<People> peopleList) {
        People monitor = peopleList.get(1);
        // Verify the existences of the school, grade and class
        School school = schoolMapper.getSchoolByName(monitor.getSchool());
        if (school == null) {
            // Create school, return the generated auto increment key
            school = new School(monitor.getSchool());
            schoolMapper.saveSchool(school);
        }
        Grade grade = gradeMapper.getGradeOfSchool(monitor.getGrade(), school.getId());
        if (grade == null) {
            // Create grade and save the correspond relation between school and grade
            grade = new Grade(monitor.getGrade());
            gradeMapper.saveGrade(grade);
            gradeMapper.saveSchoolGrade(school.getId(), grade.getId());
        }
        Class classObj = classMapper.getClassOfGrade(monitor.getClassName(), grade.getId());
        if (classObj == null) {
            // Create class  and save correspond relation between grade and class
            classObj = new Class(monitor.getClassName());
            classMapper.saveClass(classObj);
            classMapper.saveGradeClass(grade.getId(), classObj.getId());
        }

        int affectedRows = 0;
        // Traverse the all people list
        for (People people : peopleList) {
            // Register user, encrypt the password
            String password = MD5Utils.md5Encrypt(people.getNumber());
            User user = new User(people.getNumber(), password, DateUtils.parseString("1970-01-01"), people.getPhone(), people.getEmail(), User.STATUS_NORMAL, User.TYPE_STUDENT, new Date());
            if (userMapper.saveUser(user) == 1) {
                people.setUserId(user.getId());
                people.setClassId(classObj.getId());
                people.setGradeId(grade.getId());
                people.setSchoolId(school.getId());
                people.setCreateDatetime(new Date());
                // Save people information
                if (peopleMapper.savePeople(people) == 1) {
                    affectedRows++;
                }
            }
        }
        return affectedRows;
    }

    @Override
    public List<School> getAllSchools() {
        return schoolMapper.getAllSchools();
    }

    @Override
    public List<Grade> getGradesOfSchool(Integer schoolId) {
        return gradeMapper.getGradesOfSchool(schoolId);
    }

    @Override
    public List<Class> getClassesOfSchool(Integer gradeId) {
        return classMapper.getClassesOfSchool(gradeId);
    }

    @Override
    public List<People> getClassPeopleList(Integer classId) {
        return peopleMapper.getClassPeopleList(classId);
    }
}
