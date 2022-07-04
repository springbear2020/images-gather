package edu.whut.springbear.gather.controller;

import com.github.pagehelper.PageInfo;
import edu.whut.springbear.gather.exception.InterceptorException;
import edu.whut.springbear.gather.pojo.LoginLog;
import edu.whut.springbear.gather.pojo.Response;
import edu.whut.springbear.gather.pojo.Upload;
import edu.whut.springbear.gather.pojo.User;
import edu.whut.springbear.gather.service.RecordService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;


/**
 * @author Spring-_-Bear
 * @datetime 2022-07-02 17:00 Saturday
 */
@RestController
public class RecordController {
    @Resource
    private RecordService recordService;

    @GetMapping("/admin/images/personal/today")
    public Response adminTodayImages(HttpSession session) {
        User admin = (User) session.getAttribute("admin");
        Upload upload = recordService.getUserUploadInSpecifiedDate(admin.getId(), Upload.STATUS_UPLOADED, new Date());
        if (upload == null) {
            return Response.info("今日【两码一查】尚未完成");
        }
        String contextPath = session.getServletContext().getContextPath() + "/";
        // String[0]-healthImageAccessUrl String[0]-scheduleImageAccessUrl String[0]-closedImageAccessUrl
        String[] imagesUrl = recordService.getThreeImagesAccessUrl(contextPath, upload);
        return Response.success(admin.getStudent().getName() + "，今日【两码一查】已完成").put("imagesUrl", imagesUrl);
    }

    @GetMapping("/record/login/{pageNum}")
    public Response getLoginLogData(@PathVariable("pageNum") Integer pageNum, HttpSession session) throws InterceptorException {
        User user = (User) session.getAttribute("user");
        User admin = (User) session.getAttribute("admin");
        // Admin and common user login at the same time on the same browser
        if (user != null && admin != null) {
            throw new InterceptorException("请先退出管理员或用户账号");
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

    @GetMapping("/record/upload/{pageNum}")
    public Response getUploadDate(@PathVariable("pageNum") Integer pageNum, HttpSession session) throws InterceptorException {
        User user = (User) session.getAttribute("user");
        User admin = (User) session.getAttribute("admin");
        // Admin and common user login at the same time on the same browser
        if (user != null && admin != null) {
            throw new InterceptorException("请先退出管理员或用户账号");
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
}
