package com.ucm.lib.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import java.util.Locale;

@SpringBootTest
class EmailServiceTest {
    @Autowired
    private EmailService emailService;

    /**
     * Test email sending. Succeeds if no exceptions are thrown.
     * I personally use FakeSMTP for this, but use what you like.
     * @author Joshua Podhola
     * @throws MessagingException Email server failed to connect.
     */
    /*
    @Test
    void htmlEmailTest() throws MessagingException {
        final Context ctx = new Context(new Locale("en"));
        //HTML Test
        emailService.sendEmail("you@gmail.com", ctx, "html/test", "HTML Email Unit Test");
        //Plaintext test
        emailService.sendEmail("you@gmail.com", ctx, "text/test", "Plaintext Email Unit Test");
        //String HTML test
        emailService.sendEmail("you@gmail.com", ctx, "<h2>hi</h2>", "String HTML Email Unit Test");
    }
    */
}