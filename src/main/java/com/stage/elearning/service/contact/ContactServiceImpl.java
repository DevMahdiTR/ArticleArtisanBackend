package com.stage.elearning.service.contact;

import com.stage.elearning.service.email.EmailSenderService;
import com.stage.elearning.utility.CustomResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ContactServiceImpl  implements  ContactService{

    private final EmailSenderService emailSenderService;


    public ContactServiceImpl(EmailSenderService emailSenderService)
    {
        this.emailSenderService =emailSenderService;
    }


    @Override
    public CustomResponseEntity<String> sendEmailFromContact(String toEmail  , String subject , String senderEmail ,String message)
    {
        emailSenderService.sendEmail(toEmail , subject,emailSenderService.emailTemplateContact(senderEmail,message));
        final String successResponse = "Your email has been sent successfully.";
        final CustomResponseEntity<String> customResponse = new CustomResponseEntity<>(HttpStatus.OK,successResponse);
        return customResponse;
    }
}
