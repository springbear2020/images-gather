package edu.whut.bear.gather.controller;

import edu.whut.bear.gather.pojo.Login;
import edu.whut.bear.gather.pojo.User;
import edu.whut.bear.gather.service.RecordService;
import edu.whut.bear.gather.service.UserService;
import edu.whut.bear.gather.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * @author Spring-_-Bear
 * @datetime 6/2/2022 9:55 PM
 */
@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RecordService recordService;

    @GetMapping("/user")
    public String login(String username, String password, HttpServletRequest request, HttpSession session) {
        // User has login before, go to the home page
        User user = (User) session.getAttribute("user");
        if (user != null) {
            return "home";
        }
        // Verify the username and password entered by user
        user = userService.verifyUsernameAndPassword(username, password);
        if (user == null) {
            return "login";
        }
        // Save user login log
        String ip = WebUtils.getIpAddress(request);
        // TODO
        // String location = WebUtils.parseIp(ip);
        String location = "湖北省武汉市";
        if (!recordService.saveLogin(new Login(user.getId(), ip, location, new Date()))) {
            return "login";
        }
        session.setAttribute("user", user);
        return "redirect:/home";
    }
}
