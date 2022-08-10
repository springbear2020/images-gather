package cn.edu.whut.springbear.gather.controller;

import cn.edu.whut.springbear.gather.pojo.*;
import cn.edu.whut.springbear.gather.service.RecordService;
import cn.edu.whut.springbear.gather.service.StudentService;
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
 * @datetime 2022-08-08 21:59 Monday
 */
@RestController
public class RecordController {
    @Autowired
    private RecordService recordService;
    @Autowired
    private StudentService studentService;

    @GetMapping("/record/login.do")
    public Response getLoginLogPageData(@RequestParam("pageNum") Integer pageNum, HttpSession session) {
        User user = (User) session.getAttribute("user");
        PageInfo<LoginLog> loginPageData = recordService.getUserLoginLogPageData(user.getId(), pageNum);
        if (loginPageData == null || loginPageData.getList() == null || loginPageData.getList().size() == 0) {
            return Response.info("暂无您的个人登录记录");
        }
        return Response.success("查询个人登录记录成功").put("pageData", loginPageData);
    }

    @GetMapping("/record/upload.do")
    public Response getUploadPageData(@RequestParam("pageNum") Integer pageNum, HttpSession session) {
        User user = (User) session.getAttribute("user");
        PageInfo<Upload> uploadPageData = recordService.getUserUploadPageData(user.getId(), Upload.STATUS_UPLOADED, pageNum);
        if (uploadPageData == null || uploadPageData.getList() == null || uploadPageData.getList().size() == 0) {
            return Response.info("暂无您的个人上传记录");
        }
        return Response.success("查询个人上传记录成功").put("pageData", uploadPageData);
    }

    @GetMapping("/record/upload/today.do")
    public Response getUserUploadToday(HttpSession session) {
        User user = (User) session.getAttribute("user");
        Student student = studentService.getStudentByUserId(user.getId());
        user.setStudent(student);
        session.setAttribute("user", user);

        Upload upload = recordService.getUserUploadInSpecifiedDate(user.getId(), Upload.STATUS_UPLOADED, new Date());
        if (upload == null) {
            return Response.info("暂无您的今日上传记录").put("user", user);
        }
        return Response.success("查询今日上传记录成功").put("item", upload).put("user", user);
    }

    @GetMapping("/record/class.do")
    public Response getClassUpload(@RequestParam("date") String date, HttpSession session) {
        Date specifiedDate = DateUtils.parseString(date);
        if (specifiedDate == null) {
            return Response.error("请求日期参数格式不正确");
        }

        User user = (User) session.getAttribute("user");
        // Student don't have the privilege to view the class upload record
        if (user.getUserType() < 1) {
            return Response.error("权限不足，无权查看班级上传记录");
        }

        // Get the class student list someone who not sign in the system
        String className;
        if (user.getUserType() == User.TYPE_TEACHER) {
            className = user.getTeacher().getClassName();
        } else {
            className = user.getStudent().getClassName();
        }
        List<Student> notLoginList = studentService.getClassNotLoginList(className, specifiedDate);
        // Get the class student list someone who sign in the system but not upload the images
        List<Student> notUploadList = studentService.getClassUploadList(Upload.STATUS_NOT_UPLOAD, className, specifiedDate);
        // Get the class student list someone who upload the images successfully
        List<Upload> uploadedList = recordService.getClassUploadListWithStudent(Upload.STATUS_UPLOADED, specifiedDate, className);
        return Response.success("查询班级上传记录成功").put("notLoginList", notLoginList).put("notUploadList", notUploadList).put("uploadedList", uploadedList);
    }
}
