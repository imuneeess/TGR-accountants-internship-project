package org.webserv.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.webserv.DTO.CreateUserDTO;
import org.webserv.models.User;
import org.webserv.models.AccountingDay;
import org.webserv.services.UserService;
import org.webserv.services.AccountingDayService;

import java.security.Principal;
import java.util.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/admin")
public class AdminDashboardController {

    @Autowired
    private UserService userService;

    @Autowired
    private AccountingDayService accountingDayService;

    @PostMapping("/create-user")
    public ResponseEntity<Map<String, String>> createUser(@RequestBody CreateUserDTO createUserDTO) {
        if (createUserDTO.getEmail() == null || createUserDTO.getRole() == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email and role are required!"));
        }

        try {
            User newUser = userService.createUser(createUserDTO.getEmail(), createUserDTO.getRole());

            Map<String, String> response = new HashMap<>();
            response.put("email", newUser.getEmail());
            response.put("password", newUser.getPassword());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error creating user: " + e.getMessage()));
        }
    }

    @GetMapping("/accountants")
    public ResponseEntity<List<Map<String, String>>> getAccountantsWithValidationStatus() {
        try {
            String currentDate = LocalDate.now().toString();
            List<AccountingDay> accountingDays = accountingDayService.getAccountingDaysByDate(currentDate);
            List<Map<String, String>> accountantsStatus = new ArrayList<>();

            for (AccountingDay accountingDay : accountingDays) {
                Map<String, String> accountantStatus = new HashMap<>();
                accountantStatus.put("email", accountingDay.getEmail());
                accountantStatus.put("role", "ACCOUNTANT");
                accountantStatus.put("status", accountingDay.isValidated() ? "Validated" : "Not Validated");
                accountantsStatus.add(accountantStatus);
            }

            return ResponseEntity.ok(accountantsStatus);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(List.of(Map.of("message", "Error fetching accountants' validation status: " + e.getMessage())));
        }
    }

    // ✅ Update first and last name
    @PutMapping("/update-name")
    public ResponseEntity<String> updateName(@RequestBody Map<String, String> body, Principal principal) {
        String email = principal.getName();
        String firstName = body.get("firstName");
        String lastName = body.get("lastName");

        userService.updateUserNames(email, firstName, lastName);
        return ResponseEntity.ok("Name updated");
    }

    // ✅ Change password
    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody Map<String, String> body, Principal principal) {
        String email = principal.getName();
        String currentPassword = body.get("currentPassword");
        String newPassword = body.get("newPassword");

        boolean changed = userService.changeUserPassword(email, currentPassword, newPassword);
        if (changed) return ResponseEntity.ok("Password updated");
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Current password incorrect");
    }

    // ✅ Get full name for bubble
    @GetMapping("/profile")
    public ResponseEntity<Map<String, String>> getProfileInfo(Principal principal) {
        String email = principal.getName();
        Optional<User> userOpt = userService.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return ResponseEntity.ok(Map.of(
                    "firstName", user.getFirstName() != null ? user.getFirstName() : "",
                    "lastName", user.getLastName() != null ? user.getLastName() : "",
                    "email", user.getEmail()
            ));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "User not found"));
        }
    }
}
