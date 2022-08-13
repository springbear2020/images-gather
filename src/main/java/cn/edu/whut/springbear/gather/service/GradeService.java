package cn.edu.whut.springbear.gather.service;

import cn.edu.whut.springbear.gather.pojo.Class;

import java.util.Date;
import java.util.List;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-13 09:18 Saturday
 */
public interface GradeService {
    /**
     * Get the three images not completed classes list with total numbers at specified date
     */
    List<Class> getNotCompletedClasses(Integer gradeId, Date specifiedDate);
}
