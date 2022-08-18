package cn.edu.whut.springbear.gather.controller;

import cn.edu.whut.springbear.gather.pojo.Class;
import cn.edu.whut.springbear.gather.pojo.Grade;
import cn.edu.whut.springbear.gather.pojo.entity.Response;
import cn.edu.whut.springbear.gather.pojo.School;
import cn.edu.whut.springbear.gather.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-15 11:44 Monday
 */
@RestController
public class SchoolController {
    @Autowired
    private SchoolService schoolService;

    /**
     * Everyone
     */
    @GetMapping("/school.do")
    public Response listAllSchools() {
        List<School> schoolList = schoolService.listAllSchools();
        if (schoolList == null || schoolList.size() == 0) {
            return Response.info("暂无学校信息");
        }
        return Response.success("查询学校信息成功").put("list", schoolList);
    }

    /**
     * Everyone
     */
    @GetMapping("/grade.do")
    public Response getGradeOfSchool(@RequestParam("schoolId") Integer schoolId) {
        List<Grade> gradeList = schoolService.listGradesOfSchool(schoolId);
        if (gradeList == null || gradeList.size() == 0) {
            return Response.info("暂无年级信息");
        }
        return Response.success("查询年级信息成功").put("list", gradeList);
    }

    /**
     * Everyone
     */
    @GetMapping("/class.do")
    public Response getClassesOfSchool(@RequestParam("gradeId") Integer gradeId) {
        List<Class> classList = schoolService.listClassesOfGrade(gradeId);
        if (classList == null || classList.size() == 0) {
            return Response.info("暂无班级信息");
        }
        return Response.success("查询班级信息成功").put("list", classList);
    }
}
