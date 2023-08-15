package com.stage.elearning.service.email;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {EmailSenderServiceImpl.class})
@ExtendWith(SpringExtension.class)
class EmailSenderServiceImplTest {
    @Autowired
    private EmailSenderServiceImpl emailSenderServiceImpl;

    @MockBean
    private JavaMailSender javaMailSender;


    @Test
    void testSendEmail() throws MailException {
        doNothing().when(javaMailSender).send(Mockito.<MimeMessage>any());
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        emailSenderServiceImpl.sendEmail("jane.doe@example.org", "janedoe", "Link");
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(Mockito.<MimeMessage>any());
    }


    @Test
    void testSendEmail2() throws MailException {
        doThrow(new RuntimeException("utf-8")).when(javaMailSender).send(Mockito.<MimeMessage>any());
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        assertThrows(RuntimeException.class,
                () -> emailSenderServiceImpl.sendEmail("jane.doe@example.org", "janedoe", "Link"));
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(Mockito.<MimeMessage>any());
    }
}

