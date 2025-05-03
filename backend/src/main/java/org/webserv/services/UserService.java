package org.webserv.services;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.webserv.models.User;
import org.webserv.models.UserRole;
import org.webserv.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Inject PasswordEncoder

    public User createAccountant(String email) {
        String generatedPassword = RandomStringUtils.randomAlphanumeric(12); // 12-character password

        String hashedPassword = passwordEncoder.encode(generatedPassword);

        User user = new User();
        user.setEmail(email);
        user.setPassword(hashedPassword);  // Store the hashed password
        user.setRole(UserRole.ACCOUNTANT);

        userRepository.save(user);

        user.setPassword(generatedPassword);  // Set the plain-text password back
        return user;
    }
}
