package org.webserv.controllers;

import org.webserv.models.User;
import org.webserv.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    // ✅ Get all users with role ACCOUNTANT
    @GetMapping("/accountants")
    public ResponseEntity<List<User>> getAllAccountants() {
        List<User> accountants = userRepository.findByRole("ACCOUNTANT");
        return ResponseEntity.ok(accountants);
    }

    // ✅ Save notification email for reminders
    @PostMapping("/save-notification-email")
    public ResponseEntity<String> saveNotificationEmail(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String notificationEmail = payload.get("notificationEmail");

        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        User user = userOpt.get();
        user.setNotificationEmail(notificationEmail);
        userRepository.save(user);
        return ResponseEntity.ok("Notification email saved successfully");
    }
}