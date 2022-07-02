package edu.whut.springbear.gather.controller;

import edu.whut.springbear.gather.pojo.Response;
import edu.whut.springbear.gather.pojo.Upload;
import edu.whut.springbear.gather.pojo.User;
import edu.whut.springbear.gather.service.RecordService;
import edu.whut.springbear.gather.service.TransferService;
import edu.whut.springbear.gather.service.UserService;
import edu.whut.springbear.gather.util.PropertyUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * @author Spring-_-Bear
 * @datetime 2022-07-01 10:23 Friday
 */
@Controller
public class TransferController {
    @Resource
    private TransferService transferService;
    @Resource
    private UserService userService;
    @Resource
    private PropertyUtils propertyUtils;
    @Resource
    private RecordService recordService;

    @ResponseBody
    @PostMapping("/transfer")
    public Response upload(HttpSession session,
                           @RequestParam("healthImage") MultipartFile healthImage,
                           @RequestParam("scheduleImage") MultipartFile scheduleImage,
                           @RequestParam("closedImage") MultipartFile closedImage) {
        // Judge whether the three images are in correct format
        Response response = transferService.judgeThreeImagesFormat(healthImage, scheduleImage, closedImage);
        if (response != null) {
            return response;
        }

        User user = (User) session.getAttribute("user");
        User admin = (User) session.getAttribute("admin");
        // Admin and common user login at the same time on the same browser
        if (user != null && admin != null) {
            return Response.error("请先退出管理员或用户账号");
        }
        // Judge who is trying to upload images
        user = admin != null ? admin : user;

        // Get the user info with student
        assert user != null;
        User userWithStudent = userService.getUserWithStudentInfo(user.getId());
        String realPath = session.getServletContext().getRealPath("/");

        // Create the user's file save directory of today
        String userTodayPath = transferService.createUserTodayDirectory(userWithStudent, realPath);
        if (userTodayPath == null) {
            return Response.error("今日文件保存目录创建失败，请稍后重试");
        }

        // Save the image files to the physical disk
        Upload upload = transferService.saveImageFilesToDisk(userWithStudent, realPath, userTodayPath, healthImage, scheduleImage, closedImage);
        if (upload == null) {
            return Response.error("图片文件保存失败，请稍后重试");
        }

        // Upload the image files to the Qiniu cloud
        if (propertyUtils.getStartService()) {
            upload = transferService.saveImagesToCloud(upload, realPath);
        }

        // Update the upload record of the user
        if (!recordService.updateImagesAccessUrl(upload)) {
            return Response.error("更新今日上传记录失败，请稍后重试");
        }

        return Response.success("今日【两码一查】已完成");
    }
}
