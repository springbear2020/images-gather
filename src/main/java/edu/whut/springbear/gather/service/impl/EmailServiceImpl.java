package edu.whut.springbear.gather.service.impl;

import edu.whut.springbear.gather.service.EmailService;
import edu.whut.springbear.gather.util.EmailUtils;
import edu.whut.springbear.gather.util.NumberUtils;
import edu.whut.springbear.gather.util.PropertyUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

/**
 * @author Spring-_-Bear
 * @datetime 2022-07-07 06:58 Thursday
 */
@Service
public class EmailServiceImpl implements EmailService {
    @Resource
    private EmailUtils emailUtils;
    @Resource
    private PropertyUtils propertyUtils;

    @Override
    public String sendEmail(String receiver) {
        // Generate the verify code in length randomly
        String verifyCode = NumberUtils.generateDigitalCode(propertyUtils.getVerifyCodeLength());

        // try to send the email to the email address of the receiver
        try {
            emailUtils.sendEmail(receiver, verifyCode);
            return verifyCode;
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
