package com.stage.elearning.service.email;

import org.jetbrains.annotations.Contract;

public interface EmailSenderService {
    public void sendEmail(final String toEmail ,final String userName , final String link);
}
