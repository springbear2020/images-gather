package cn.edu.whut.springbear.gather.controller;

import cn.edu.whut.springbear.gather.pojo.Email;
import cn.edu.whut.springbear.gather.pojo.Login;
import cn.edu.whut.springbear.gather.pojo.entity.Response;
import cn.edu.whut.springbear.gather.pojo.User;
import cn.edu.whut.springbear.gather.service.EmailService;
import cn.edu.whut.springbear.gather.service.RecordService;
import cn.edu.whut.springbear.gather.service.UserService;
import cn.edu.whut.springbear.gather.util.DateUtils;
import cn.edu.whut.springbear.gather.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

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
    @Autowired
    private EmailService emailService;

    /**
     * Everyone
     */
    @GetMapping("/login.do")
    public Response login(@RequestParam("username") String username, @RequestParam("password") String password, String rememberMe, HttpServletResponse response, HttpServletRequest request, HttpSession session) {
        // Verify the correctness of the username and password entered by user when login
        User user = userService.getUser(username, password);
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
            return Response.error("用户状态异常，禁止登录");
        }
        // Update user last login datetime
        User tmp = new User();
        tmp.setId(user.getId());
        tmp.setLastLoginDatetime(new Date());
        if (!userService.updateUser(tmp)) {
            return Response.error("更新登录时间失败");
        }
        // Create student upload record of today
        if (user.getUserType() <= User.TYPE_MONITOR && !DateUtils.isToday(user.getLastLoginDatetime())) {
            if (!recordService.createUserUploadToday(user.getId())) {
                return Response.error("今日上传记录新建失败");
            }
        }
        session.setAttribute("user", user);
        return Response.success("登录成功").put("item", user.getUserType());
    }

    /**
     * Everyone
     */
    @PutMapping("/reset.do")
    public Response resetPassword(@RequestParam("username") String username, @RequestParam("email") String email, @RequestParam("verifyCode") String codeByUser, @RequestParam("newPassword") String newPassword, HttpSession session) {
        if (newPassword.length() < 6) {
            return Response.error("新密码长度不小于 6 位，请重新输入");
        }

        // Verify the existence of the username and email address
        User user = userService.getUserByUsernameAndEmail(username, email);
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
        User tmp = new User();
        tmp.setId(user.getId());
        tmp.setPassword(newPassword);
        if (!userService.updateUser(tmp)) {
            return Response.error("密码重置失败");
        }

        session.removeAttribute("emailVerifyCode");
        return Response.success("密码重置成功，3 秒后返回登录页面");
    }

    /**
     * Everyone
     */
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
        User verifyUser = userService.getUser(user.getUsername(), oldPassword);
        if (verifyUser == null) {
            return Response.error("原密码有误，请重新输入");
        }

        // Update the password of the user
        User tmp = new User();
        tmp.setId(user.getId());
        tmp.setPassword(newPassword);
        if (!userService.updateUser(tmp)) {
            return Response.error("密码修改失败");
        }
        return Response.success("密码修改成功");
    }

    /**
     * Everyone
     */
    @GetMapping("/logout.do")
    public Response logout(HttpSession session) {
        session.removeAttribute("user");
        session.invalidate();
        return Response.success("注销登录成功");
    }

    /**
     * Everyone
     */
    @PostMapping("/email.do")
    public Response sendEmail(@RequestParam("username") String username, @RequestParam("email") String email, HttpSession session) {
        // Verify the existence of the username and email address
        User user = userService.getUserByUsernameAndEmail(username, email);
        if (user == null) {
            return Response.error("用户名不存在或邮箱地址不匹配");
        }

        // Send email verify code
        String code = emailService.sendEmail(email);
        if (code == null) {
            return Response.info("邮箱服务不可用，请联系管理员");
        } else if (code.isEmpty()) {
            return Response.error("服务器繁忙，验证码发送失败");
        }

        // Save the email send log
        recordService.saveEmailLog(new Email(email, code, new Date(), user.getId()));

        // TODO Redis to replace with session 10min
        session.setAttribute("emailVerifyCode", code);
        return Response.success("邮箱验证码发送成功，请查收");
    }

    /**
     * Everyone
     */
    @GetMapping("/user.do")
    public Response getUser(HttpSession session) {
        User user = (User) session.getAttribute("user");
        // Get the latest login log of the user
        Login login = recordService.getUserLatestLoginLog(user.getId());
        return Response.success("查询个人信息成功").put("item", user).put("loginLog", login);
    }

    /**
     * Admin
     */
    @GetMapping("/user/{userId}.do")
    public Response getUserById(@PathVariable("userId")Integer userId, HttpSession session) {
        User admin = (User) session.getAttribute("user");
        if (admin.getUserType() != User.TYPE_ADMIN) {
            return Response.error("权限不足，禁止查看用户信息");
        }
        User user = userService.getUser(userId);
        if (user == null) {
            return Response.info("用户信息不存在");
        }
        return Response.success("查询用户信息成功").put("item", user);
    }

    /**
     * Everyone
     */
    @PutMapping("/user.do")
    public Response updateUserInfo(@RequestParam String newSex, @RequestParam String newPhone, @RequestParam String newEmail, HttpSession session) {
        User user = (User) session.getAttribute("user");
        User tmp = new User();
        tmp.setId(user.getId());
        tmp.setSex(newSex);
        tmp.setEmail(newEmail);
        tmp.setPhone(newPhone);
        if (!userService.updateUser(tmp)) {
            return Response.error("个人信息更新失败");
        }

        // Keep the user info in the session scope be latest
        user.setSex(newSex);
        user.setPhone(newPhone);
        user.setEmail(newEmail);
        session.setAttribute("user", user);
        return Response.success("个人信息更新成功").put("item", user.getUserType());
    }

    /**
     * Admin
     */
    @PutMapping("/user/{userId}.do")
    public Response updateUserType(@PathVariable("userId") Integer userId, @RequestParam Integer newUserType, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user.getUserType() != User.TYPE_ADMIN) {
            return Response.error("权限不足，禁止修改用户类型");
        }
        User tmp = new User();
        tmp.setId(userId);
        tmp.setUserType(newUserType);
        if (!userService.updateUser(tmp)) {
            return Response.error("修改用户类型失败");
        }
        return Response.success("更新用户类型成功");
    }

    /**
     * Admin
     */
    @GetMapping("/user/class.do")
    public Response usersOfClass(@RequestParam("classId") Integer classId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user.getUserType() != User.TYPE_ADMIN) {
            return Response.error("权限不足，禁止查看班级人员信息");
        }
        List<User> userList = userService.listUsersOfClass(classId);
        if (userList == null || userList.size() == 0) {
            return Response.info("当前班级暂无人员信息");
        }
        return Response.success("查询班级人员信息成功").put("list", userList);
    }

    /**
     * Admin
     */
    @PostMapping("/user.do")
    public Response saveHeadTeacher(User user, HttpSession session) {
        User admin = (User) session.getAttribute("user");
        if (admin.getUserType() != User.TYPE_ADMIN) {
            return Response.error("权限不足，禁止新增年级主任");
        }
        if (!userService.saveUser(user)) {
            return Response.error("保存人员信息失败");
        }
        return Response.success("新增年级主任成功");
    }
}
