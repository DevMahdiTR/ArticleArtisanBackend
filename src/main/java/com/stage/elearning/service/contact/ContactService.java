package com.stage.elearning.service.contact;

import com.stage.elearning.utility.CustomResponseEntity;

public interface ContactService {
    public CustomResponseEntity<String> sendEmailFromContact(String toEmail  , String subject , String senderEmail ,String message);

}
