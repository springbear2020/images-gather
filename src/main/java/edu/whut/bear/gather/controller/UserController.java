package edu.whut.bear.gather.controller;

import edu.whut.bear.gather.pojo.Response;
import edu.whut.bear.gather.pojo.User;
import edu.whut.bear.gather.service.UserService;
import edu.whut.bear.gather.util.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * @author Spring-_-Bear
 * @datetime 6/6/2022 12:55 PM
 */
@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private PropertyUtils propertyUtils;

    @PostMapping("/user")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password,
                        ModelMap modelMap, HttpSession session) {
        // 去除用户名、密码首尾两端空格
        username = username.trim();
        password = password.trim();

        // 验证用户名密码存在性
        User user = userService.verifyUsernameAndPassword(username, password);

        // 用户名不存在或密码错误
        if (user == null) {
            modelMap.addAttribute("loginErrorMsg", "用户名不存在或密码错误");
            return "index";
        }

        // 根据用户类型跳转到不同的页面
        switch (user.getUserType()) {
            case User.COMMON:
                session.setAttribute("user", user);
                return "user/home";
            case User.ADMIN:
                session.setAttribute("admin", user);
                return "admin/home";
            default:
                modelMap.addAttribute("loginErrorMsg", "用户类型不存在");
                return "index";
        }
    }

    @GetMapping("/user/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        session.removeAttribute("admin");
        session.invalidate();
        return "redirect:" + propertyUtils.getContextPath();
    }

    @ResponseBody
    @PutMapping("/user")
    public Response updatePassword(@RequestParam String oldPassword, @RequestParam String newPassword, HttpSession session) {
        // 去除原密码和新密码中的首尾空格
        oldPassword = oldPassword.trim();
        newPassword = newPassword.trim();

        User user = (User) session.getAttribute("user");
        User admin = (User) session.getAttribute("admin");

        // 普通用户和管理员同时在一个浏览器中登录，提示需退出一个账户
        if (user != null && admin != null) {
            return Response.info("请先退出管理员或用户账号");
        }

        // 判断是普通用户还是管理员修改密码
        user = admin == null ? user : admin;
        // user 依旧为空则用户未登录
        if (user == null) {
            return Response.info("请先登录您的账号");
        }

        // 验证原密码的正确性
        User verifyUser = userService.verifyUsernameAndPassword(user.getUsername(), oldPassword);
        if (verifyUser == null) {
            return Response.error("原密码有误，请重新输入");
        }

        // 更新用户密码
        if (!userService.updateUserPassword(newPassword, user.getId())) {
            return Response.error("密码修改失败，请稍后重试");
        }
        return Response.success("密码修改成功");
    }
}