package edu.whut.bear.gather.controller;

import edu.whut.bear.gather.pojo.User;
import edu.whut.bear.gather.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

/**
 * @author Spring-_-Bear
 * @datetime 6/6/2022 12:55 PM
 */
@Controller
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 用户登录：理论上本应使用 get 方法，此处采用 post 方法以隐藏地址栏敏感信息
     */
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
}
