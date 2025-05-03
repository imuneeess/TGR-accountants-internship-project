package org.webserv.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    @Autowired
    private JavaMailSender mailSender;

    @GetMapping("/test-email")
    public String sendTestEmail() {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("younesselouazzani123@gmail.com");
            message.setTo("younesselouazzani12333@gmail.com");
            message.setSubject("Test Email");
            message.setText("This is a test email from the Spring Boot application.");
            mailSender.send(message);
            return "Email sent successfully!";
        } catch (Exception e) {
            return "Failed to send email: " + e.getMessage();
        }
    }
}
