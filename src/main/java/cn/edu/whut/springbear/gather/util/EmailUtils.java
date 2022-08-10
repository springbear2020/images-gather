package cn.edu.whut.springbear.gather.util;


import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-09 14:41 Tuesday
 */
public class EmailUtils {
    /**
     * Get a session between this client and the email server
     *
     * @return java.mail.Session
     */
    private static Session getSession(String smtpHost) {
        // Configure the attribute information required
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.host", smtpHost);
        // Authorize needed
        properties.setProperty("mail.smtp.auth", "true");
        // Can check the details though debug mode by the code [session.setDebug(true);]
        return Session.getInstance(properties);
    }

    /**
     * Create the email content that send the to receiver
     *
     * @return MimeMessage
     */
    private static MimeMessage createMailContent(String sender, String receiver, String verifyCode, String smtpHost) throws UnsupportedEncodingException, MessagingException {
        MimeMessage message = new MimeMessage(getSession(smtpHost));
        // Set the email address of the sender
        message.setFrom(new InternetAddress(sender, "两码一查", "UTF-8"));
        // Set the email address of the receiver
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiver));
        // Email subject
        message.setSubject("【两码一查】身份验证", "UTF-8");
        // Email message body
        message.setContent("<h3>\n" + "\t<span style=\"font-size:16px;\">亲爱的用户：</span> \n" + "</h3>\n" +
                        "<p>\n" + "\t<span style=\"font-size:14px;\">&nbsp;&nbsp;&nbsp;&nbsp;</span><span style=\"font-size:14px;\">&nbsp; <span style=\"font-size:16px;\">&nbsp;&nbsp;您好！本次请求的验证码是：<span style=\"font-size:16px;color:#1d44ff;\"> "
                        + verifyCode + "</span>，验证码有效时间 10 分钟，请尽快完成验证。您正在进行身份验证，打死都不要将验证码告诉他人哦。</span></span>\n" + "</p>\n" +
                        "<p style=\"text-align:right;\">\n" + "\t<span style=\"background-color:#FFFFFF;font-size:16px;color:#000000;\"><span style=\"color:#000000;font-size:16px;background-color:#FFFFFF;\"><span class=\"token string\" style=\"font-family:&quot;font-size:16px;color:#000000;line-height:normal !important;background-color:#FFFFFF;\">两码一查</span></span></span> \n" + "</p>\n" +
                        "<p style=\"text-align:right;\">\n" + "\t<span style=\"background-color:#FFFFFF;font-size:14px;\"><span style=\"color:#FF9900;font-size:18px;\"><span class=\"token string\" style=\"font-family:&quot;font-size:16px;color:#000000;line-height:normal !important;\"><span style=\"font-size:16px;color:#000000;background-color:#FFFFFF;\">"
                        + DateUtils.parseDatetime(new Date()) + "</span><span style=\"font-size:18px;color:#000000;background-color:#FFFFFF;\"></span></span></span></span> \n" + "</p>",
                "text/html;charset=UTF-8");
        // Set delivery time
        message.setSentDate(new Date());
        message.saveChanges();
        return message;
    }

    /**
     * Send an email to the receiver email address
     */
    public synchronized static void sendEmail(String sender, String password, String smtpHost, String receiver, String verifyCode) throws Exception {
        // Make a connection with the email server
        Session session = getSession(smtpHost);
        Transport transport = session.getTransport();
        transport.connect(sender, password);
        // Build the email content
        MimeMessage message = createMailContent(sender, receiver, verifyCode, smtpHost);
        // Send the email
        transport.sendMessage(message, message.getAllRecipients());
        // Close the connection with email server
        transport.close();
    }
}
