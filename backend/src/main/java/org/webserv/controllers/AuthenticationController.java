package org.webserv.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.webserv.models.User;
import org.webserv.models.UserRole;
import org.webserv.repository.UserRepository;
import org.webserv.services.JwtService;
import org.webserv.DTO.AuthenticationResponse;
import org.webserv.DTO.LoginRequest;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationController(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // ✅ Fix: Properly retrieve User from database
        Optional<User> userOptional = userRepository.findByEmail(userDetails.getUsername());
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body(null); // Handle error properly
        }

        User user = userOptional.get();
        String jwtToken = jwtService.generateToken(userDetails, user.getRole());

        return ResponseEntity.ok(new AuthenticationResponse(jwtToken, user.getRole().name())); // ✅ Now Correct
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already registered");
        }

        if (user.getRole() == null) {
            user.setRole(UserRole.ACCOUNTANT); // Default role
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);
        String token = jwtService.generateToken(savedUser, savedUser.getRole());

        System.out.println("User registered successfully: " + savedUser.getEmail()); // ✅ Fix here

        return ResponseEntity.ok(new AuthenticationResponse(token, savedUser.getRole().name()));
    }
}
