package cn.edu.whut.springbear.gather.controller;

import cn.edu.whut.springbear.gather.pojo.EmailLog;
import cn.edu.whut.springbear.gather.pojo.Response;
import cn.edu.whut.springbear.gather.pojo.User;
import cn.edu.whut.springbear.gather.service.EmailService;
import cn.edu.whut.springbear.gather.service.RecordService;
import cn.edu.whut.springbear.gather.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-11 01:18 Thursday
 */
@RestController
public class EmailController {
    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private RecordService recordService;

    @PostMapping("/email.do")
    public Response sendEmailVerifyCode(@RequestParam("username") String username, @RequestParam("email") String email, HttpSession session) {
        // Verify the format of the email address
        if (!Pattern.matches("\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?", email)) {
            return Response.error("邮箱地址格式不正确，请重新输入");
        }
        // Verify the existence of the username and email address
        User user = userService.queryUserByUsernameAndEmail(username, email);
        if (user == null) {
            return Response.error("用户名不存在或邮箱地址不匹配");
        }

        // Send email verify code
        String emailVerifyCode = emailService.sendEmail(email);
        if (emailVerifyCode == null) {
            return Response.error("服务器繁忙，验证码发送失败");
        } else if (emailVerifyCode.isEmpty()) {
            return Response.info("邮箱服务不可用，请联系管理员");
        }

        // Save the email send log
        EmailLog emailLog = new EmailLog(email, emailVerifyCode, new Date(), user.getId());
        if (!recordService.saveEmailLog(emailLog)) {
            return Response.error("验证码发送记录保存失败");
        }

        // TODO Redis to replace with session 10min
        session.setAttribute("emailVerifyCode", emailVerifyCode);
        return Response.success("邮箱验证码发送成功，请查收");
    }
}
