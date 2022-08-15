package cn.edu.whut.springbear.gather.controller;

import cn.edu.whut.springbear.gather.pojo.Response;
import cn.edu.whut.springbear.gather.pojo.User;
import cn.edu.whut.springbear.gather.service.RecordService;
import cn.edu.whut.springbear.gather.service.UserService;
import cn.edu.whut.springbear.gather.util.DateUtils;
import cn.edu.whut.springbear.gather.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-10 23:23 Wednesday
 */
@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RecordService recordService;

    @GetMapping("/login.do")
    public Response login(@RequestParam("username") String username, @RequestParam("password") String password, String rememberMe,
                          HttpServletResponse response, HttpServletRequest request, HttpSession session) {
        // Verify the correctness of the username and password entered by user when login
        User user = userService.queryUser(username, password);
        if (user == null) {
            return Response.error("用户名不存在或密码错误");
        }

        // Remember me, tell the browser save the cookie info for one week
        if ("true".equals(rememberMe)) {
            Cookie usernameCookie = new Cookie("username", username);
            usernameCookie.setMaxAge(60 * 60 * 24 * 7);
            usernameCookie.setPath("/static/html/login.html");
            response.addCookie(usernameCookie);
            Cookie passwordCookie = new Cookie("password", password);
            passwordCookie.setMaxAge(60 * 60 * 24 * 7);
            passwordCookie.setPath("/static/html/login.html");
            response.addCookie(passwordCookie);
        }

        // Save user login log, get ip address from the request header and save login log
        String ip = WebUtils.getIpAddress(request);
        if (!recordService.saveLoginLog(ip, user.getId())) {
            return Response.error("登录记录保存失败");
        }
        // Judge the user status
        if (User.STATUS_NORMAL != user.getUserStatus()) {
            return Response.error("禁止登录，用户状态异常");
        }
        // Update user last login datetime
        if (!userService.updateLoginDatetime(user.getId(), new Date())) {
            return Response.error("更新登录时间失败");
        }
        // Create student upload record of today
        if (user.getUserType() <= User.TYPE_MONITOR && !DateUtils.isToday(user.getLastLoginDatetime())) {
            if (!recordService.createStudentUploadToday(user.getId())) {
                return Response.error("今日上传记录新建失败");
            }
        }
        session.setAttribute("user", user);
        return Response.success("登录成功").put("item", user.getUserType());
    }

    @PutMapping("/reset.do")
    public Response resetPassword(@RequestParam("username") String username, @RequestParam("email") String email,
                                  @RequestParam("verifyCode") String codeByUser, @RequestParam("newPassword") String newPassword,
                                  HttpSession session) {
        // Verify the format of the email address
        if (!Pattern.matches("\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?", email)) {
            return Response.error("邮箱地址格式不正确，请重新输入");
        }
        // Verify the length of the newPassword
        if (newPassword == null || newPassword.length() < 6) {
            return Response.error("新密码长度不小于 6 位，请重新输入");
        }
        // Verify the existence of the username and email address
        User user = userService.queryUserByUsernameAndEmail(username, email);
        if (user == null) {
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
        if (!userService.updateUserPassword(user.getId(), newPassword)) {
            return Response.error("密码重置失败");
        }

        session.removeAttribute("emailVerifyCode");
        return Response.success("密码重置成功，3 秒后返回登录页面");
    }

    @PutMapping("/update.do")
    public Response updatePassword(@RequestParam String oldPassword, @RequestParam String newPassword, HttpSession session) {
        User user = (User) session.getAttribute("user");
        // Whether the old password id equals to the new password
        if (oldPassword.equals(newPassword)) {
            return Response.error("新旧密码一致，请重新输入新密码");
        }
        if (newPassword.length() < 6) {
            return Response.error("新密码长度不小于 6 位，请重新输入");
        }

        // Verify the correctness of the old password
        User verifyUser = userService.queryUser(user.getUsername(), oldPassword);
        if (verifyUser == null) {
            return Response.error("原密码有误，请重新输入");
        }

        // Update the password of the user
        if (!userService.updateUserPassword(user.getId(), newPassword)) {
            return Response.error("密码修改失败");
        }
        return Response.success("密码修改成功");
    }

    @GetMapping("/logout.do")
    public Response logout(HttpSession session) {
        session.removeAttribute("user");
        session.invalidate();
        return Response.success("注销登录成功");
    }
}
