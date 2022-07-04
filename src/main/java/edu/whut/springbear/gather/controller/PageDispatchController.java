package edu.whut.springbear.gather.controller;

import edu.whut.springbear.gather.exception.InterceptorException;
import edu.whut.springbear.gather.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

/**
 * @author Spring-_-Bear
 * @datetime 2022-07-03 16:56 Sunday
 */
@Controller
public class PageDispatchController {
    @GetMapping("/home.html")
    public String homePageDispatch(HttpSession session) throws InterceptorException {
        User user = (User) session.getAttribute("user");
        User admin = (User) session.getAttribute("admin");
        // Admin and common user login at the same time on the same browser
        if (user != null && admin != null) {
            throw new InterceptorException("请先退出管理员或用户账号");
        }
        // Judge who is trying to upload images
        user = admin != null ? admin : user;
        assert user != null;
        return User.TYPE_ADMIN == user.getUserType() ? "admin/admin_home" : "user/user_home";
    }
}
