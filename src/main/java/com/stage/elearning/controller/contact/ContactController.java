package com.stage.elearning.controller.contact;

import com.stage.elearning.service.contact.ContactService;
import com.stage.elearning.utility.CustomResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/contact")
public class ContactController {


    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }


    @GetMapping("/client/mail/sender_email/{senderEmail}/message/{message}")
    public CustomResponseEntity<String> sendEmailFromContact(@PathVariable("senderEmail") String senderEmail, @PathVariable("message")String message){
        return contactService.sendEmailFromContact("learninge452@gmail.com" , "Contact Message",senderEmail,message);
    }
}
