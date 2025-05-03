package org.webserv.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.webserv.models.User;

import java.util.Optional;
import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    List<User> findByRole(String role); // ✅ THIS is what was missing!
}
