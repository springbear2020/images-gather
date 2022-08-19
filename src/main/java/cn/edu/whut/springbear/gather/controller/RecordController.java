package cn.edu.whut.springbear.gather.controller;

import cn.edu.whut.springbear.gather.pojo.*;
import cn.edu.whut.springbear.gather.pojo.Class;
import cn.edu.whut.springbear.gather.pojo.entity.Response;
import cn.edu.whut.springbear.gather.service.SchoolService;
import cn.edu.whut.springbear.gather.service.RecordService;
import cn.edu.whut.springbear.gather.util.DateUtils;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-11 15:04 Thursday
 */
@RestController
public class RecordController {
    @Autowired
    private RecordService recordService;
    @Autowired
    private SchoolService schoolService;

    /**
     * Everyone
     */
    @GetMapping("/record/login.do")
    public Response getLoginLogPageData(@RequestParam("pageNum") Integer pageNum, HttpSession session) {
        User user = (User) session.getAttribute("user");
        PageInfo<Login> loginPageData = recordService.listUserLoginLogPageData(user.getId(), pageNum);
        if (loginPageData == null || loginPageData.getList() == null || loginPageData.getList().size() == 0) {
            return Response.info("暂无您的登录记录");
        }
        return Response.success("查询个人登录记录成功").put("pageData", loginPageData);
    }

    /**
     * Student, monitor
     */
    @GetMapping("/record/upload.do")
    public Response getUploadPageData(@RequestParam("pageNum") Integer pageNum, HttpSession session) {
        User user = (User) session.getAttribute("user");
        // Only the student and monitor have the upload record
        if (user.getUserType() > User.TYPE_MONITOR) {
            return Response.info("当前用户账号不存在上传记录");
        }
        PageInfo<Upload> uploadPageData = recordService.listUserUploadPageData(user.getId(), Upload.STATUS_COMPLETED, pageNum);
        if (uploadPageData == null || uploadPageData.getList() == null || uploadPageData.getList().size() == 0) {
            return Response.info("暂无您的上传记录");
        }
        return Response.success("查询个人上传记录成功").put("pageData", uploadPageData);
    }

    /**
     * Student, monitor
     */
    @GetMapping("/record/upload/today.do")
    public Response getUserUploadToday(HttpSession session) {
        User user = (User) session.getAttribute("user");
        // Only the student and monitor have the upload record
        if (user.getUserType() > User.TYPE_MONITOR) {
            return Response.info("当前用户账号不存在上传记录");
        }

        Upload upload = recordService.getUserUpload(user.getId(), new Date(), Upload.STATUS_COMPLETED);
        if (upload == null) {
            return Response.info("暂无您的今日已完成记录").put("name", user.getName()).put("userType", user.getUserType());
        }
        return Response.success("查询今日已完成记录成功").put("name", user.getName()).put("userType", user.getUserType()).put("upload", upload);
    }

    /**
     * >= Monitor
     */
    @GetMapping("/record/class.do")
    public Response getClassUpload(@RequestParam("date") String date, Integer classId, HttpSession session) {
        Date specifiedDate = DateUtils.parseString(date);
        if (specifiedDate == null) {
            return Response.error("请求日期参数格式不正确");
        }

        User user = (User) session.getAttribute("user");
        // Student don't have the privilege to view the class upload record
        if (user.getUserType() < User.TYPE_MONITOR) {
            return Response.error("权限不足，禁止查看班级记录");
        }

        if (classId == null) {
            classId = user.getClassId();
        }

        // Get the class student list someone who not sign in the system
        List<String> notLoginNames = schoolService.listNotLoginNamesOfClass(classId, specifiedDate);
        // Get the class student list someone who sign in the system but not upload the images
        List<String> notUploadNames = schoolService.listNotUploadNamesOfClass(classId, specifiedDate);
        // Get the class student list someone who upload the three images successfully
        List<String> completedNames = schoolService.listCompletedNamesOfClass(classId, specifiedDate);
        // Get the class upload list with student name someone upload three images successfully
        List<Upload> uploadedList = recordService.listUploadsOfClass(classId, Upload.STATUS_COMPLETED, specifiedDate);
        return Response.success("查询班级上传记录成功").put("notLoginNames", notLoginNames).put("notUploadNames", notUploadNames)
                .put("completedNames", completedNames).put("uploadedList", uploadedList);
    }

    /**
     * >= Grade teacher
     */
    @GetMapping("/record/grade.do")
    public Response getGradeUpload(@RequestParam("date") String dateStr, HttpSession session) {
        Date specifiedDate = DateUtils.parseString(dateStr);
        if (specifiedDate == null) {
            return Response.error("请求日期参数格式不正确");
        }

        User user = (User) session.getAttribute("user");
        // Student don't have the privilege to view the class upload record
        if (user.getUserType() < User.TYPE_GRADE_TEACHER) {
            return Response.error("权限不足，禁止查看年级记录");
        }

        // Get the classes list of current grade with class not completed total numbers
        List<Class> classList = schoolService.listNotCompletedClasses(user.getGradeId(), specifiedDate);
        if (classList == null || classList.size() == 0) {
            return Response.info("当前年级下不存在班级信息");
        }

        return Response.success("查询年级上传记录成功").put("list", classList);
    }
}
