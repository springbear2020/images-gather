package edu.whut.springbear.gather.service;

import org.springframework.stereotype.Service;

/**
 * @author Spring-_-Bear
 * @datetime 2022-07-07 06:57 Thursday
 */
@Service
public interface EmailService {
    /**
     * Send an email to the email address of the receiver
     *
     * @param receiver Email address of the receiver
     * @return If email send successfully then return the email verify code or return null
     */
    String sendEmail(String receiver);
}
