package edu.whut.springbear.gather.controller;

import edu.whut.springbear.gather.pojo.Response;
import edu.whut.springbear.gather.service.EmailService;
import edu.whut.springbear.gather.service.StudentService;
import edu.whut.springbear.gather.util.PropertyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.regex.Pattern;

/**
 * @author Spring-_-Bear
 * @datetime 2022-07-06 23:04 Wednesday
 */
@RestController
public class EmailController {
    @Resource
    private EmailService emailService;
    @Resource
    private PropertyUtils propertyUtils;
    @Resource
    private StudentService studentService;

    @PostMapping("/email")
    public Response sendEmail(@RequestParam("username") String studentNumber, @RequestParam("email") String email, HttpSession session) {
        if (!propertyUtils.getStartEmailService()) {
            return Response.info("邮箱服务暂不可用，请稍后重试");
        }

        // Verify the format of the email address
        if (!Pattern.matches("\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?", email)) {
            return Response.warn("邮箱地址格式不正确，请重新输入");
        }

        // Verify the existence of the student number and email address (student number is the username)
        if (studentService.getStudentByStudentNumberAndEmail(studentNumber, email) == null) {
            return Response.error("用户名不存在或邮箱地址不匹配");
        }

        // Send an email which contains the email verify code to the email address of the receiver
        String emailVerifyCode = emailService.sendEmail(email);
        if (emailVerifyCode == null) {
            return Response.error("服务器繁忙，验证码发送失败");
        }

        session.setAttribute("emailVerifyCode", emailVerifyCode);
        return Response.success("邮箱验证码发送成功，请查收");
    }
}
