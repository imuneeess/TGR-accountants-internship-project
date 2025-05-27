package org.webserv.services;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.webserv.models.User;
import org.webserv.models.UserRole;
import org.webserv.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createAccountant(String email) {
        String generatedPassword = RandomStringUtils.randomAlphanumeric(12);
        String hashedPassword = passwordEncoder.encode(generatedPassword);

        User user = new User();
        user.setEmail(email);
        user.setPassword(hashedPassword);
        user.setRole(UserRole.ACCOUNTANT);

        userRepository.save(user);
        user.setPassword(generatedPassword);
        return user;
    }

    public User createUser(String email, String roleString) {
        String generatedPassword = RandomStringUtils.randomAlphanumeric(12);
        String hashedPassword = passwordEncoder.encode(generatedPassword);

        UserRole role = UserRole.valueOf(roleString.toUpperCase());

        User user = new User();
        user.setEmail(email);
        user.setPassword(hashedPassword);
        user.setRole(role);
        user.setEnabled(true);

        userRepository.save(user);
        user.setPassword(generatedPassword);
        return user;
    }

    // ✅ Find user
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // ✅ Update name
    public void updateUserNames(String email, String firstName, String lastName) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        userOpt.ifPresent(user -> {
            user.setFirstName(firstName);
            user.setLastName(lastName);
            userRepository.save(user);
        });
    }

    // ✅ Change password
    public boolean changeUserPassword(String email, String currentPassword, String newPassword) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(currentPassword, user.getPassword())) {
                user.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }
}
