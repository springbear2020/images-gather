package edu.whut.bear.gather.controller;

import edu.whut.bear.gather.pojo.Login;
import edu.whut.bear.gather.pojo.Record;
import edu.whut.bear.gather.pojo.Response;
import edu.whut.bear.gather.pojo.User;
import edu.whut.bear.gather.service.RecordService;
import edu.whut.bear.gather.service.UserService;
import edu.whut.bear.gather.util.DateUtils;
import edu.whut.bear.gather.util.PropertyUtils;
import edu.whut.bear.gather.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private PropertyUtils propertyUtils;

    @PostMapping("/user")
    public String login(@RequestParam String username, @RequestParam String password, HttpServletRequest request, HttpSession session) {
        // User has login before, go to the home page
        User user = (User) session.getAttribute("user");
        if (user != null) {
            return user.getUserType() == User.COMMON ? "redirect:" + propertyUtils.getContextUrl() + "home" : "redirect:" + propertyUtils.getContextUrl() + "admin";
        }

        // Verify the username and password entered by user
        user = userService.verifyUsernameAndPassword(username, password);
        if (user == null) {
            return "login";
        }

        // TODO Update the method of get user login ip address
        // Save user login log
        String ip = WebUtils.getIpAddress(request);
        String location = WebUtils.parseIp(ip);
        // String location = "湖北省武汉市";
        if (!recordService.saveLogin(new Login(null, user.getId(), ip, location, new Date()))) {
            return "login";
        }

        Record record;
        // Create the user's upload record if the user last login date is not today
        if (!DateUtils.isToday(user.getLastRecordCreateDate())) {
            int defaultNum = -1;
            String defaultImage = propertyUtils.getContextUrl() + "static/img/4.png";
            record = new Record(null, user.getId(), user.getUsername(), user.getRealName(), user.getClassNumber(), user.getClassName(), defaultNum, defaultNum, defaultNum, new Date(), defaultImage, defaultImage, defaultImage);
            if (!recordService.saveRecord(record)) {
                return "login";
            }
            // Update the user login date be today to prevent create two record of the in one day
            if (!userService.updateLastLoginDate(new Date(), user.getId())) {
                return "login";
            }
        }
        session.setAttribute("user", user);
        return user.getUserType() == User.COMMON ? "redirect:" + propertyUtils.getContextUrl() + "home" : "redirect:" + propertyUtils.getContextUrl() + "admin";
    }

    @GetMapping("/user/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:" + propertyUtils.getContextUrl();
    }

    @ResponseBody
    @PutMapping("/user")
    public Response updatePassword(HttpSession session, @RequestParam String oldPassword, @RequestParam String newPassword) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Response.info("登录后方可修改密码");
        }

        // Verify the correctness of the old password entered by user
        User verifyUser = userService.verifyUsernameAndPassword(user.getUsername(), oldPassword);
        if (verifyUser == null) {
            return Response.error("原密码错误，请重新输入");
        }

        // Update the password of user
        if (!userService.updatePasswordById(newPassword, user.getId())) {
            return Response.error("密码更新失败");
        }
        return Response.success("密码修改成功");
    }

    @ResponseBody
    @GetMapping("/record/{date}")
    public Response record(@PathVariable("date") String date, HttpSession session) {
        // User must login before go to the home page
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Response.info("登录后方可查看用户上传记录");
        }

        if (user.getUserType() == User.COMMON) {
            return Response.info("您暂无权限查看用户上传记录");
        }

        // Get the upload record of the admin class
        return recordService.processRecordList(user.getClassNumber(), DateUtils.parseString(date));
    }
}
