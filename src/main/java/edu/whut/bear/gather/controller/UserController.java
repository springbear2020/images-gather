package edu.whut.bear.gather.controller;

import edu.whut.bear.gather.pojo.User;
import edu.whut.bear.gather.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

/**
 * @author Spring-_-Bear
 * @datetime 6/2/2022 9:55 PM
 */
@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/user")
    public String login(String username, String password, HttpSession session) {
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
        session.setAttribute("user", user);
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home(HttpSession session) {
        // User must login before go to the home page
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "login";
        }
        return "home";
    }
}
