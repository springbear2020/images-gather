package edu.whut.springbear.gather.controller;

import edu.whut.springbear.gather.exception.InterceptorException;
import edu.whut.springbear.gather.pojo.*;
import edu.whut.springbear.gather.service.*;
import edu.whut.springbear.gather.util.DateUtils;
import edu.whut.springbear.gather.util.PropertyUtils;
import edu.whut.springbear.gather.util.WebUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * @author Spring-_-Bear
 * @datetime 2022-06-30 18:53 Thursday
 */
@Controller
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private RecordService recordService;
    @Resource
    private PropertyUtils propertyUtils;
    @Resource
    private StudentService studentService;
    @Resource
    private EmailService emailService;
    @Resource
    private TransferService transferService;

    @GetMapping("/login.do")
    public String login(String rememberMe, @RequestParam("username") String username, @RequestParam("password") String password,
                        HttpServletResponse response, HttpServletRequest request, Model model) {
        // Verify the correctness of the username and password entered by the user
        User user = userService.queryUserByUsernameAndPassword(username, password);
        if (user == null) {
            model.addAttribute("loginMsg", "用户名不存在或密码错误，请重新输入");
            return "login";
        }

        // Remember me, tell the browser save the cookie info for one week
        if (rememberMe != null) {
            Cookie usernameCookie = new Cookie("username", username);
            usernameCookie.setMaxAge(60 * 60 * 24 * 7);
            response.addCookie(usernameCookie);
            Cookie passwordCookie = new Cookie("password", password);
            passwordCookie.setMaxAge(60 * 60 * 24 * 7);
            response.addCookie(passwordCookie);
        }

        String location = "未知地点";
        String ip = WebUtils.getIpAddress(request);
        // Get the ip location from the baidu api
        if (propertyUtils.getStartIpParse()) {
            location = WebUtils.getIpLocationFromBaiduMap(propertyUtils.getIpParseUrl() + ip);
        }
        // Save the user login log
        if (!recordService.saveLoginLog(new LoginLog(null, ip, location, new Date(), user.getId()))) {
            model.addAttribute("loginMsg", "登录记录保存失败，请稍后重试");
            return "login";
        }
        // Judge the user status
        if (User.STATUS_NORMAL != user.getUserStatus()) {
            model.addAttribute("loginMsg", "用户状态异常，禁止登录");
            return "login";
        }
        // Update user last login date
        if (!userService.updateUserLastLoginDate(user.getId(), new Date())) {
            model.addAttribute("loginMsg", "更新登录时间失败，请稍后重试");
            return "login";
        }

        HttpSession session = request.getSession();
        session.setAttribute("studentName", user.getStudent().getName());
        // Judge whether the user have uploaded the images successfully
        Upload uploadToday = recordService.getUserUploadInSpecifiedDate(user.getId(), Upload.STATUS_UPLOADED, new Date());
        if (uploadToday != null) {
            // Get the three images' access url from the upload record, result format as follow,
            // String[0]-healthImageAccessUrl String[0]-scheduleImageAccessUrl String[0]-closedImageAccessUrl
            String[] accessUrls = recordService.getThreeImagesAccessUrl(uploadToday);
            // Add the images' access url to the request scope
            model.addAttribute("todayHealth", accessUrls[0]);
            model.addAttribute("todaySchedule", accessUrls[1]);
            model.addAttribute("todayClosed", accessUrls[2]);
            if (User.TYPE_USER == user.getUserType()) {
                session.setAttribute("user", user);
            } else {
                session.setAttribute("admin", user);
            }
            return "complete";
        }

        // Create the user's today upload record of today
        if (!DateUtils.isToday(user.getLastLoginDatetime())) {
            //  Create the user's today upload record
            String uploadPath = "static/img/notUpload.png";
            Upload upload = new Upload(null, Upload.STATUS_NOT_UPLOAD, new Date(), uploadPath, uploadPath, uploadPath, uploadPath, uploadPath, uploadPath, user.getId(), null);
            if (!recordService.saveUserUploadRecord(upload)) {
                model.addAttribute("loginMsg", "今日上传记录创建失败，请稍后重试");
                return "login";
            }
        }

        // Redirect to different page by the user's type
        switch (user.getUserType()) {
            case User.TYPE_USER:
                session.setAttribute("user", user);
                break;
            case User.TYPE_ADMIN:
                session.setAttribute("admin", user);
                break;
            default:
                model.addAttribute("loginMsg", "用户类型不存在，禁止登录");
                return "login";
        }
        return "redirect:/home.do";
    }

    @GetMapping("/logout.do")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        session.removeAttribute("admin");
        session.removeAttribute("person");
        session.invalidate();
        return "redirect:/";
    }

    @ResponseBody
    @PutMapping("/reset.do")
    public Response resetPassword(@RequestParam("username") String username, @RequestParam("email") String email,
                                  @RequestParam("verifyCode") String codeByUser, @RequestParam("newPassword") String newPassword,
                                  HttpSession session) {
        // Verify the format of the email address
        if (!Pattern.matches("\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?", email)) {
            return Response.warn("邮箱地址格式不正确，请重新输入");
        }
        // Verify the length of the newPassword
        if (newPassword == null || newPassword.length() < 6) {
            return Response.warn("新密码长度不小于 6 位，请重新输入");
        }
        // Verify the existence of the student number and email address (student number is the username)
        if (studentService.getStudentByStudentNumberAndEmail(username, email) == null) {
            return Response.error("用户名不存在或邮箱地址不匹配");
        }

        String codeBySystem = (String) session.getAttribute("emailVerifyCode");
        // The email is sending but not failed, will be send successfully a few moment later
        if (codeBySystem == null) {
            return Response.info("系统正在发送验证码，请稍等···");
        }
        // Verify the correctness of the email verify code entered by user
        if (!codeBySystem.equals(codeByUser)) {
            return Response.error("邮箱验证码有误，请重新输入");
        }

        // Update the login password of the user
        if (!userService.updateUserPassword(username, newPassword)) {
            return Response.error("密码重置失败，请稍后重试");
        }

        // TODO Set the max alive time of the verify code is 10m
        session.removeAttribute("emailVerifyCode");
        return Response.success("密码重置成功，3 秒后返回登录页面");
    }

    @ResponseBody
    @PutMapping("/password.do")
    public Response updatePassword(@RequestParam String oldPassword, @RequestParam String newPassword, HttpSession session) throws InterceptorException {
        User user = (User) session.getAttribute("user");
        User admin = (User) session.getAttribute("admin");
        // Admin and common user login at the same time on the same browser
        if (user != null && admin != null) {
            session.removeAttribute("user");
            session.removeAttribute("admin");
            throw new InterceptorException("不允许同时登录管理员和用户账号，请重新登录");
        }
        // Judge who is trying to update his/her password
        user = admin != null ? admin : user;

        // Whether the old password id equals to the new password
        if (oldPassword.equals(newPassword)) {
            return Response.info("新旧密码一致，请重新输入新密码");
        }

        // Verify the correctness of the old password
        assert user != null;
        User verifyUser = userService.queryUserByUsernameAndPassword(user.getUsername(), oldPassword);
        if (verifyUser == null) {
            return Response.error("原密码有误，请重新输入");
        }

        // Update the password of the user
        if (!userService.updateUserPassword(newPassword, user.getId())) {
            return Response.error("密码修改失败，请稍后重试");
        }
        return Response.success("个人登录密码修改成功");
    }

    @ResponseBody
    @PutMapping("/personal.do")
    public Response updatePersonalInformation(@RequestParam String newSex, @RequestParam String newPhone, @RequestParam String newEmail, HttpSession session) throws InterceptorException {
        User user = (User) session.getAttribute("user");
        User admin = (User) session.getAttribute("admin");
        // Admin and common user login at the same time on the same browser
        if (user != null && admin != null) {
            session.removeAttribute("user");
            session.removeAttribute("admin");
            throw new InterceptorException("不允许同时登录管理员和用户账号，请重新登录");
        }
        // Judge who is trying to update his/her password
        user = admin != null ? admin : user;

        // Verify the data entered by the user
        if (!("男".equals(newSex) || "女".equals(newSex))) {
            return Response.error("性别类型不正确，请重新输入");
        }
        if (!Pattern.matches("^1[3-9]\\d{9}$", newPhone)) {
            return Response.error("手机号格式不正确，请重新输入");
        }
        if (!Pattern.matches("\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?", newEmail)) {
            return Response.warn("邮箱地址格式不正确，请重新输入");
        }

        // Update the user info by the user number
        assert user != null;
        Student student = user.getStudent();
        if (!studentService.updateStudent(newSex, newPhone, newEmail, student.getNumber())) {
            return Response.error("个人信息更新失败，请稍后重试");
        }
        student.setEmail(newEmail);
        student.setPhone(newPhone);
        student.setSex(newSex);
        user.setStudent(student);
        // Update the student information in the session score, let be the latest
        if (admin != null) {
            session.setAttribute("admin", user);
        } else {
            session.setAttribute("user", user);
        }
        return Response.success("个人信息修改成功");
    }

    @ResponseBody
    @PostMapping("/email.do")
    public Response sendEmail(@RequestParam("username") String studentNumber, @RequestParam("email") String email, HttpSession session) {
        if (!propertyUtils.getStartEmailService()) {
            return Response.info("邮箱服务暂不可用，请稍后重试");
        }

        // Verify the format of the email address
        if (!Pattern.matches("\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?", email)) {
            return Response.warn("邮箱地址格式不正确，请重新输入");
        }

        // Verify the existence of the student number and email address (student number is the username)
        if (studentService.getStudentByStudentNumberAndEmail(studentNumber, email) == null) {
            return Response.error("用户名不存在或邮箱地址不匹配");
        }

        // Send an email which contains the email verify code to the email address of the receiver
        String emailVerifyCode = emailService.sendEmail(email);
        if (emailVerifyCode == null) {
            return Response.error("服务器繁忙，验证码发送失败");
        }

        session.setAttribute("emailVerifyCode", emailVerifyCode);
        return Response.success("邮箱验证码发送成功，请查收");
    }

    @ResponseBody
    @PostMapping("/transfer.do")
    public Response imagesUpload(HttpSession session,
                                 @RequestParam("healthImage") MultipartFile healthImage,
                                 @RequestParam("scheduleImage") MultipartFile scheduleImage,
                                 @RequestParam("closedImage") MultipartFile closedImage) throws InterceptorException {
        // Judge whether the three images are in correct format
        Response response = transferService.judgeThreeImagesFormat(healthImage, scheduleImage, closedImage);
        if (response != null) {
            return response;
        }

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

        // Get the user info with student
        assert user != null;
        User userWithStudent = userService.getUserWithStudentInfo(user.getId());
        String realPath = session.getServletContext().getRealPath("/");

        // Create the user's file save directory of today
        String userTodayPath = transferService.createUserTodayDirectory(userWithStudent, realPath);
        if (userTodayPath == null) {
            return Response.error("今日保存目录创建失败，请稍后重试");
        }

        // Save the image files to the physical disk
        Upload upload = transferService.saveImageFilesToDisk(userWithStudent, realPath, userTodayPath, healthImage, scheduleImage, closedImage);
        if (upload == null) {
            return Response.error("图片文件保存失败，请稍后重试");
        }

        // Upload the image files to the Qiniu cloud
        if (propertyUtils.getStartQiniuService()) {
            upload = transferService.saveImagesToCloud(upload, realPath);
        }

        // Update the upload record of the user
        if (!recordService.updateImagesAccessUrl(upload)) {
            return Response.error("更新今日上传记录失败，请稍后重试");
        }

        return Response.success("今日【两码一查】已完成");
    }
}