package cn.edu.whut.springbear.gather.service.impl;

import cn.edu.whut.springbear.gather.mapper.GradeMapper;
import cn.edu.whut.springbear.gather.pojo.Class;
import cn.edu.whut.springbear.gather.service.ClassService;
import cn.edu.whut.springbear.gather.service.GradeService;
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
public class GradeServiceImpl implements GradeService {
    @Autowired
    private GradeMapper gradeMapper;
    @Autowired
    private ClassService classService;

    @Override
    public List<Class> getNotCompletedClasses(Integer gradeId, Date specifiedDate) {
        List<Class> res = new ArrayList<>();
        // Get the classes id list of current grade
        List<Integer> classIdList = gradeMapper.getClassesOfGrade(gradeId);
        for (Integer classId : classIdList) {
            res.add(classService.getClassNotCompleted(classId, specifiedDate));
        }
        return res;
    }
}
