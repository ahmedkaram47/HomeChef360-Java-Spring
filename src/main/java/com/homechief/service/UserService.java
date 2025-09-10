package com.homechief.service;

import com.homechief.dto.UserDTO;
import com.homechief.model.User;
import com.homechief.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(String name, String email, String rawPassword) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }
        User u = new User();
        u.setName(name);
        u.setEmail(email);
        u.setPassword(passwordEncoder.encode(rawPassword));
        u.setRole("user");
        return userRepository.save(u);
    }

    public UserDTO toDTO(User u) {
        UserDTO d = new UserDTO();
        d.setName(u.getName());
        d.setEmail(u.getEmail());
        return d;
    }
}