package cn.edu.whut.springbear.gather.controller;

import cn.edu.whut.springbear.gather.pojo.Class;
import cn.edu.whut.springbear.gather.pojo.Grade;
import cn.edu.whut.springbear.gather.pojo.Response;
import cn.edu.whut.springbear.gather.pojo.School;
import cn.edu.whut.springbear.gather.pojo.User;
import cn.edu.whut.springbear.gather.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-15 11:44 Monday
 */
@RestController
public class SchoolController {
    @Autowired
    private SchoolService schoolService;

    @GetMapping("/school.do")
    public Response getAllSchools(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user.getUserType() != User.TYPE_ADMIN) {
            return Response.error("权限不足，禁止查看学校信息");
        }
        List<School> schoolList = schoolService.getAllSchools();
        if (schoolList == null || schoolList.size() == 0) {
            return Response.info("暂无学校信息");
        }
        return Response.success("查询学校信息成功").put("list", schoolList);
    }

    @GetMapping("/grade.do")
    public Response getGradeOfSchool(@RequestParam("schoolId") Integer schoolId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user.getUserType() != User.TYPE_ADMIN) {
            return Response.error("权限不足，禁止查看年级信息");
        }
        List<Grade> gradeList = schoolService.getGradesOfSchool(schoolId);
        if (gradeList == null || gradeList.size() == 0) {
            return Response.info("暂无年级信息");
        }
        return Response.success("查询年级信息成功").put("list", gradeList);
    }

    @GetMapping("/class.do")
    public Response getClassesOfSchool(@RequestParam("gradeId") Integer gradeId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user.getUserType() != User.TYPE_ADMIN) {
            return Response.error("权限不足，禁止查看班级信息");
        }
        List<Class> classList = schoolService.getClassesOfSchool(gradeId);
        if (classList == null || classList.size() == 0) {
            return Response.info("暂无班级信息");
        }
        return Response.success("查询班级信息成功").put("list", classList);
    }
}
