package org.webserv.controllers; // ✅ Make sure this matches your actual package!

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.webserv.repository.UserRepository;
import org.webserv.services.EmailService;
import org.webserv.models.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth") // ✅ This must match frontend route
public class PasswordResetController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/forgot-password")
    public ResponseEntity<String> resetPassword(@RequestBody String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(404).body("User not found");
        }

        User user = userOptional.get();
        String newPassword = UUID.randomUUID().toString().substring(0, 8);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        String targetEmail = user.getNotificationEmail() != null
                ? user.getNotificationEmail()
                : email;

        emailService.sendEmail(targetEmail, "Your New Password", "Your new password is: " + newPassword);
        return ResponseEntity.ok("New password sent to your notification email");
    }
}
