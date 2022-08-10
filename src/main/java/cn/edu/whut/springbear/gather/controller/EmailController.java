package cn.edu.whut.springbear.gather.controller;

import cn.edu.whut.springbear.gather.pojo.Response;
import cn.edu.whut.springbear.gather.service.EmailService;
import cn.edu.whut.springbear.gather.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.regex.Pattern;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-09 14:22 Tuesday
 */
@RestController
public class EmailController {
    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;

    @PostMapping("/email.do")
    public Response sendEmail(@RequestParam("username") String username, @RequestParam("email") String email, HttpSession session) {
        // Verify the format of the email address
        if (!Pattern.matches("\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?", email)) {
            return Response.warn("邮箱地址格式不正确，请重新输入");
        }

        // Verify the existence of the username and email address
        if (userService.getUserByUsernameAndEmail(username, email) == null) {
            return Response.error("用户名不存在或邮箱地址不匹配");
        }

        // Send an email which contains the email verify code to the email address of the receiver
        String emailVerifyCode = emailService.sendEmail(email);
        if (emailVerifyCode == null) {
            return Response.error("服务器繁忙，验证码发送失败");
        } else if (emailVerifyCode.isEmpty()) {
            return Response.error("邮箱服务暂不可用，请联系管理员");
        }

        session.setAttribute("emailVerifyCode", emailVerifyCode);
        return Response.success("邮箱验证码发送成功，请查收");
    }
}