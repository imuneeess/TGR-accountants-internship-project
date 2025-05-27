package org.webserv.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.webserv.models.User;
import org.webserv.repository.UserRepository;
import org.webserv.services.JwtService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/accountants")
    public ResponseEntity<List<User>> getAllAccountants() {
        List<User> accountants = userRepository.findByRole("ACCOUNTANT");
        return ResponseEntity.ok(accountants);
    }

    @GetMapping("/me")
    public ResponseEntity<User> getUserByToken(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String email = jwtService.extractUsername(token);
        Optional<User> userOpt = userRepository.findByEmail(email);
        return userOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

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

    @PutMapping("/update-name")
    public ResponseEntity<String> updateName(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String firstName = payload.get("firstName");
        String lastName = payload.get("lastName");

        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        User user = userOpt.get();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        userRepository.save(user);

        return ResponseEntity.ok("Name updated successfully");
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String currentPassword = payload.get("currentPassword");
        String newPassword = payload.get("newPassword");

        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        User user = userOpt.get();
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            return ResponseEntity.badRequest().body("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return ResponseEntity.ok("Password changed successfully");
    }

    @PutMapping("/toggle-status")
    public ResponseEntity<String> toggleAccountStatus(@RequestParam String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        User user = userOpt.get();
        user.setEnabled(!user.isEnabled());
        userRepository.save(user);

        return ResponseEntity.ok("User " + (user.isEnabled() ? "enabled" : "disabled") + " successfully");
    }
}