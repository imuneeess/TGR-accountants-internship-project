package org.webserv.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.webserv.services.EmailService; // ✅ Ensure this import exists

@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public String sendTestEmail(@RequestParam String to) {
        emailService.sendEmail(to, "Test Email", "This is a test email from the Accounting System.");
        return "Email sent successfully to " + to;
    }
}
