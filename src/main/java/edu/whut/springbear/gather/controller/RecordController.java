package edu.whut.springbear.gather.controller;

import com.github.pagehelper.PageInfo;
import edu.whut.springbear.gather.exception.InterceptorException;
import edu.whut.springbear.gather.pojo.*;
import edu.whut.springbear.gather.service.RecordService;
import edu.whut.springbear.gather.service.StudentService;
import edu.whut.springbear.gather.util.DateUtils;
import edu.whut.springbear.gather.util.PropertyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;


/**
 * @author Spring-_-Bear
 * @datetime 2022-07-02 17:00 Saturday
 */
@RestController
public class RecordController {
    @Resource
    private RecordService recordService;
    @Resource
    private StudentService studentService;
    @Resource
    private PropertyUtils propertyUtils;

    @GetMapping("/record/login.json")
    public Response getLoginLogPageData(@RequestParam Integer pageNum, HttpSession session) throws InterceptorException {
        User user = (User) session.getAttribute("user");
        User admin = (User) session.getAttribute("admin");
        // Admin and common user login at the same time on the same browser
        if (user != null && admin != null) {
            session.removeAttribute("user");
            session.removeAttribute("admin");
            throw new InterceptorException("不允许同时登录管理员和用户账号，请重新登录");
        }
        // Judge who is trying to upload images
        user = admin != null ? admin : user;
        assert user != null;

        PageInfo<LoginLog> loginPageData = recordService.getUserLoginPageData(user.getId(), pageNum);
        if (loginPageData == null || loginPageData.getList() == null || loginPageData.getList().size() == 0) {
            return Response.info("暂无您的个人登录记录");
        }
        return Response.success("成功查询您的登录记录").put("loginPageData", loginPageData);
    }

    @GetMapping("/record/upload.json")
    public Response getUploadPageData(@RequestParam Integer pageNum, HttpSession session) throws InterceptorException {
        User user = (User) session.getAttribute("user");
        User admin = (User) session.getAttribute("admin");
        // Admin and common user login at the same time on the same browser
        if (user != null && admin != null) {
            session.removeAttribute("user");
            session.removeAttribute("admin");
            throw new InterceptorException("不允许同时登录管理员和用户账号，请重新登录");
        }
        // Judge who is trying to upload images
        user = admin != null ? admin : user;
        assert user != null;

        PageInfo<Upload> uploadPageData = recordService.getUserUploadHistory(user.getId(), Upload.STATUS_UPLOADED, pageNum);
        if (uploadPageData == null || uploadPageData.getList() == null || uploadPageData.getList().size() == 0) {
            return Response.info("暂无您的个人上传记录");
        }
        return Response.success("成功查询您的上传记录").put("uploadPageData", uploadPageData);
    }

    @GetMapping("/personal.json")
    public Response getPersonalInformation(HttpSession session) throws InterceptorException {
        User user = (User) session.getAttribute("user");
        User admin = (User) session.getAttribute("admin");
        // Admin and common user login at the same time on the same browser
        if (user != null && admin != null) {
            session.removeAttribute("user");
            session.removeAttribute("admin");
            throw new InterceptorException("不允许同时登录管理员和用户账号，请重新登录");
        }
        user = admin != null ? admin : user;
        return Response.success("成功查询个人信息").put("personal", user);
    }

    @GetMapping("/admin/record/class.json")
    public Response getClassUploadPageData(@RequestParam String date, HttpSession session) {
        User admin = (User) session.getAttribute("admin");

        // Get the class student list someone who not sign in the system
        String className = admin.getStudent().getClassName();
        Date specifiedDate = DateUtils.parseStringWithHyphen(date, new Date());
        List<Student> notLoginList = studentService.getClassStudentsNotSignInOnDate(className, specifiedDate);
        // Get the class student list someone who sign in the system but not upload the images
        List<Student> notUploadList = studentService.getClassStudentsNotUploadOnDate(Upload.STATUS_NOT_UPLOAD, className, specifiedDate);
        // Get the class student list someone who upload the images successfully
        List<Upload> uploadedList = recordService.getClassUploadListWithStudentOnDayByStatus(Upload.STATUS_UPLOADED, specifiedDate, className);
        return Response.success("成功获取班级上传记录").put("notLoginList", notLoginList).put("notUploadList", notUploadList).put("uploadedList", uploadedList).
                put("notUploadUrl", propertyUtils.getNotUploadUrl()).put("notLoginUrl", propertyUtils.getNotLoginUrl());
    }
}