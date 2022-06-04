package edu.whut.bear.gather.controller;

import edu.whut.bear.gather.pojo.User;
import edu.whut.bear.gather.util.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

/**
 * @author Spring-_-Bear
 * @datetime 6/3/2022 12:08 AM
 */
@Controller
public class DispatcherController {
    @Autowired
    private PropertyUtils propertyUtils;

    @GetMapping("/home")
    public String home(HttpSession session) {
        // User must login before go to the home page
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:" + propertyUtils.getContextUrl();
        }
        return "home";
    }

    @GetMapping("/admin")
    public String admin(HttpSession session) {
        // User must login before go to the home page
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:" + propertyUtils.getContextUrl();
        }
        return user.getUserType() == User.COMMON ? "home" : "admin";
    }

    @GetMapping("/record")
    public String adminViewRecord(HttpSession session) {
        // User must login before go to the home page
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:" + propertyUtils.getContextUrl();
        }
        return user.getUserType() == User.COMMON ? "home" : "record";
    }
}
