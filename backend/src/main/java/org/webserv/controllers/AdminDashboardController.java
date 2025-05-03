package org.webserv.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.webserv.DTO.CreateAccountantDTO;
import org.webserv.services.UserService;
import org.webserv.services.AccountingDayService; // Add this import for the AccountingDay service
import org.webserv.models.User;
import org.webserv.models.AccountingDay; // Add this import for the AccountingDay model

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List; // Add this import for List
import java.time.LocalDate;

@RestController
@RequestMapping("/admin")
public class AdminDashboardController {

    @Autowired
    private UserService userService;

    @Autowired
    private AccountingDayService accountingDayService; // Inject AccountingDayService

    @PostMapping("/create-accountant")
    public ResponseEntity<Map<String, String>> createAccountant(@RequestBody CreateAccountantDTO createAccountantDTO) {
        // Validation for email
        if (createAccountantDTO.getEmail() == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email is required!"));
        }

        try {
            // Create a new accountant
            User newAccountant = userService.createAccountant(createAccountantDTO.getEmail());

            // Prepare the response with email and generated password
            Map<String, String> response = new HashMap<>();
            response.put("email", newAccountant.getEmail());
            response.put("password", newAccountant.getPassword());

            // Return the response with status OK
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Return an error response if account creation fails
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error creating accountant: " + e.getMessage()));
        }
    }

    // Endpoint to get accountants with today's validation status
    @GetMapping("/accountants")
    public ResponseEntity<List<Map<String, String>>> getAccountantsWithValidationStatus() {
        try {
            String currentDate = LocalDate.now().toString(); // Get today's date (YYYY-MM-DD)

            // Fetch accountants who have validated their day for today's date
            List<AccountingDay> accountingDays = accountingDayService.getAccountingDaysByDate(currentDate);

            // Prepare the response with validation status
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
}
