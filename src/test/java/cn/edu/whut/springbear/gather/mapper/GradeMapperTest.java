package cn.edu.whut.springbear.gather.mapper;

import cn.edu.whut.springbear.gather.config.SpringConfiguration;
import cn.edu.whut.springbear.gather.pojo.Grade;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;


/**
 * @author Spring-_-Bear
 * @datetime 2022-08-13 08:54 Saturday
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfiguration.class)
public class GradeMapperTest {
    @Autowired
    private GradeMapper gradeMapper;

    @Test
    public void getClassesOfGrade() {
        System.out.println(gradeMapper.listClassIdsOfGrade(1));
    }

    @Test
    public void getGradesOfSchool() {
        List<Grade> gradeList = gradeMapper.listGradesOfSchool(1);
        gradeList.forEach(System.out::println);
    }

    @Test
    public void getGradeNameById() {
        System.out.println(gradeMapper.getGradeNameById(1));
    }
}