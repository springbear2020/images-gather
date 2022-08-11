package cn.edu.whut.springbear.gather.service;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-11 01:25 Thursday
 */
public interface EmailService {
    /**
     * Send an email to the email address of the receiver
     *
     * @param receiver Email address of the receiver
     * @return If email send successfully then return the email verify code or return null
     */
    String sendEmail(String receiver);
}
