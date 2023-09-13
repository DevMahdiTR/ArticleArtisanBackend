package com.stage.elearning.service.email;

import jakarta.mail.internet.MimeMessage;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;



@Service
public class EmailSenderServiceImpl  implements  EmailSenderService{

    private final static Logger LOGGER = LoggerFactory
            .getLogger(EmailSenderServiceImpl.class);
    private final JavaMailSender javaMailSender;

    public EmailSenderServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    @Async
    public void sendEmail(final String toEmail , final String subject, String body)
    {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(body, true);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setFrom("hello@amigoscode.com");
            javaMailSender.send(mimeMessage);
        } catch (jakarta.mail.MessagingException e) {
            throw new RuntimeException(e);
        }
    }
    @Contract(pure = true)
    @Override
    public String emailTemplateConfirmation(String name, String link) {
        return "<div style=\"font-family: Arial, sans-serif; line-height: 1.6; background-color: #f5f5f5; margin: 0; padding: 0;\">\n" +
                "    <div style=\"max-width: 600px; margin: 0 auto; padding: 20px; background-color: #fff; border-radius: 5px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\">\n" +
                "        <div style=\"background-color: #007bff; color: #fff; text-align: center; padding: 10px; border-top-left-radius: 5px; border-top-right-radius: 5px;\">\n" +
                "            <h2>Email Verification</h2>\n" +
                "        </div>\n" +
                "        <div style=\"padding: 20px;\">\n" +
                "            <p>Dear "+ name +",</p>\n" +
                "            <p>Thank you for signing up with our service. To activate your account, please click the button below:</p>\n" +
                "            <a style=\"display: block; width: 150px; padding: 10px; text-align: center; background-color: #007bff; color: #fff; text-decoration: none; border-radius: 5px; margin: 20px auto;\" href=" + link + ">Activate Account</a>\n" +
                "            <p>If the button above doesn't work, you can also copy and paste the following link into your browser:</p>\n" +
                "            <p>Activation link: " +link+"</p>"+
                "            <p>If you did not sign up for this service, please ignore this email.</p>\n" +
                "        </div>\n" +
                "        <div style=\"text-align: center; padding-top: 20px;\">\n" +
                "            <p>Best regards,<br>Ruspina</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</div>";
    }

    @Override
    public String emailTemplateContact(String senderEmail,String message) {
        return "<div style=\"font-family: Arial, sans-serif; line-height: 1.6; background-color: #f5f5f5; margin: 0; padding: 0;\">\n" +
                "    <div style=\"max-width: 600px; margin: 0 auto; padding: 20px; background-color: #fff; border-radius: 5px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\">\n" +
                "        <div style=\"background-color: #007bff; color: #fff; text-align: center; padding: 10px; border-top-left-radius: 5px; border-top-right-radius: 5px;\">\n" +
                "            <h2>Contact Message</h2>\n" +
                "        </div>\n" +
                "        <div style=\"padding: 20px;\">\n" +
                "            <p>Dear Rusbina,</p>\n" +
                "            <p>You have received a new message from [ " + senderEmail +" ]:</p>\n" +
                "            <blockquote style=\"background-color: #f0f0f0; padding: 10px; border-left: 5px solid #007bff;\">\n" +
                "                  "+ message +"  \n" +
                "            </blockquote>\n" +
                "            <p>If you need to respond to this message, you can do so by replying to this email.</p>\n" +
                "        </div>\n" +
                "        <div style=\"text-align: center; padding-top: 20px;\">\n" +
                "            <p>Best regards,<br>Your Contact Page</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</div>";
    }
}
