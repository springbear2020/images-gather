package cn.edu.whut.springbear.gather.service.impl;

import cn.edu.whut.springbear.gather.pojo.EmailLog;
import cn.edu.whut.springbear.gather.service.EmailService;
import cn.edu.whut.springbear.gather.service.RecordService;
import cn.edu.whut.springbear.gather.util.EmailUtils;
import cn.edu.whut.springbear.gather.util.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.Date;


/**
 * @author Spring-_-Bear
 * @datetime 2022-08-09 14:31 Tuesday
 */
@Service
@PropertySource("classpath:email.properties")
public class EmailServiceImpl implements EmailService {
    @Value("${email.emailService}")
    private Boolean emailService;
    @Value("${email.sender}")
    private String sender;
    @Value("${email.password}")
    private String password;
    @Value("${email.smtpHost}")
    private String smtpHost;
    @Autowired
    private RecordService recordService;

    private static final int CODE_LEN = 6;

    @Override
    public String sendEmail(String receiver) {
        if (!emailService) {
            return "";
        }
        // Generate the verify code in length randomly
        String verifyCode = NumberUtils.generateDigitalCode(CODE_LEN);

        recordService.saveEmailLog(new EmailLog(null, receiver, verifyCode, new Date()));

        // try to send the email to the email address of the receiver
        try {
            EmailUtils.sendEmail(sender, password, smtpHost, receiver, verifyCode);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return verifyCode;
    }
}
