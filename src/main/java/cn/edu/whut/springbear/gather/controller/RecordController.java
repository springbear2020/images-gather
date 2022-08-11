package cn.edu.whut.springbear.gather.controller;

import cn.edu.whut.springbear.gather.pojo.*;
import cn.edu.whut.springbear.gather.service.PeopleService;
import cn.edu.whut.springbear.gather.service.RecordService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-11 15:04 Thursday
 */
@RestController
public class RecordController {
    @Autowired
    private PeopleService peopleService;
    @Autowired
    private RecordService recordService;

    @GetMapping("/record/upload/today.do")
    public Response getUserUploadToday(HttpSession session) {
        User user = (User) session.getAttribute("user");
        // Only the student and monitor have the upload record
        if (user.getUserType() > User.TYPE_MONITOR) {
            return Response.info("当前用户账号不存在上传记录");
        }

        // Keep the user info in the session scope with people information
        People people = peopleService.queryPeople(user.getId());
        user.setPeople(people);
        session.setAttribute("user", user);

        Upload upload = recordService.getStudentUpload(user.getId(), new Date(), Upload.STATUS_COMPLETED);
        if (upload == null) {
            return Response.info("暂无您的今日已完成记录").put("name", people.getName()).put("userType", user.getUserType());
        }
        return Response.success("今日已完成记录查询成功").put("name", people.getName()).put("userType", user.getUserType()).put("upload", upload);
    }

    @GetMapping("/record/login.do")
    public Response getLoginLogPageData(@RequestParam("pageNum") Integer pageNum, HttpSession session) {
        User user = (User) session.getAttribute("user");
        PageInfo<LoginLog> loginPageData = recordService.getUserLoginLogPageData(user.getId(), pageNum);
        if (loginPageData == null || loginPageData.getList() == null || loginPageData.getList().size() == 0) {
            return Response.info("暂无您的个人登录记录");
        }
        return Response.success("个人登录记录查询成功").put("pageData", loginPageData);
    }

    @GetMapping("/record/upload.do")
    public Response getUploadPageData(@RequestParam("pageNum") Integer pageNum, HttpSession session) {
        User user = (User) session.getAttribute("user");
        PageInfo<Upload> uploadPageData = recordService.getUserUploadPageData(user.getId(), Upload.STATUS_COMPLETED, pageNum);
        if (uploadPageData == null || uploadPageData.getList() == null || uploadPageData.getList().size() == 0) {
            return Response.info("暂无您的个人上传记录");
        }
        return Response.success("个人上传记录查询成功").put("pageData", uploadPageData);
    }
}
