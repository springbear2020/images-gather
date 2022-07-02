package edu.whut.springbear.gather.controller;

import edu.whut.springbear.gather.pojo.LoginLog;
import edu.whut.springbear.gather.pojo.Response;
import edu.whut.springbear.gather.pojo.Upload;
import edu.whut.springbear.gather.pojo.User;
import edu.whut.springbear.gather.service.RecordService;
import edu.whut.springbear.gather.service.UserService;
import edu.whut.springbear.gather.util.DateUtils;
import edu.whut.springbear.gather.util.PropertyUtils;
import edu.whut.springbear.gather.util.WebUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;

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

    @GetMapping("/login")
    public String login(String rememberMe, @RequestParam("username") String username, @RequestParam("password") String password,
                        HttpServletResponse response, HttpServletRequest request, Model model) {
        // Verify the correctness of the username and password entered by the user
        User user = userService.queryUserByUsernameAndPassword(username, password);
        if (user == null) {
            model.addAttribute("loginMsg", "用户名不存在或密码错误");
            return "index";
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

        // Save the user login log
        String ip = WebUtils.getIpAddress(request);
        String location = "未知地点";
        // Get the ip location from the baidu api
        if (propertyUtils.getStartIpParse()) {
            location = "127.0.0.1".equals(ip) ? "localhost" : WebUtils.getIpLocationFromBaiduMap(propertyUtils.getIpParseUrl() + ip);
        }
        if (!recordService.saveLoginLog(new LoginLog(null, ip, location, new Date(), user.getId()))) {
            model.addAttribute("loginMsg", "登录记录保存失败，请稍后重试");
            return "index";
        }

        // Judge the user state
        if (User.STATUS_NORMAL != user.getUserStatus()) {
            model.addAttribute("loginMsg", "用户状态异常，禁止登录");
            return "index";
        }

        // Create the user's today upload record and update last login date
        if (!DateUtils.isToday(user.getLastLoginDate())) {
            //  Create the user's today upload record
            String uploadPath = "static/img/notUploaded.png";
            Upload upload = new Upload(null, Upload.STATUS_NON_UPLOAD, new Date(), uploadPath, uploadPath, uploadPath, uploadPath, uploadPath, uploadPath, user.getId());
            if (!recordService.saveUserUploadRecord(upload)) {
                model.addAttribute("loginMsg", "今日上传记录创建失败，请稍后重试");
                return "index";
            }

            // Update user last login date to today
            if (!userService.updateUserLastLoginDate(user.getId(), new Date())) {
                model.addAttribute("loginMsg", "更新登录时间失败，请稍后重试");
                return "index";
            }
        }

        // Redirect to different page by the user's type
        HttpSession session = request.getSession();
        switch (user.getUserType()) {
            case User.TYPE_STUDENT:
                session.setAttribute("user", user);
                return "redirect:/user/user_home.html";
            case User.TYPE_MONITOR:
                session.setAttribute("admin", user);
                return "redirect:/admin/admin_home.html";
            default:
                model.addAttribute("loginMsg", "用户类型不存在，禁止登录");
                return "index";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        session.removeAttribute("admin");
        session.invalidate();
        return "redirect:/";
    }

    @ResponseBody
    @PutMapping("/update")
    public Response updatePassword(@RequestParam String oldPassword, @RequestParam String newPassword, HttpSession session) {
        User user = (User) session.getAttribute("user");
        User admin = (User) session.getAttribute("admin");

        // Admin and common user login at the same time on the same browser
        if (user != null && admin != null) {
            return Response.error("请先退出管理员或用户账号");
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
}
