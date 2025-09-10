package com.homechief.service;

import com.homechief.model.User;
import com.homechief.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Ensure role is never null
        String role = user.getRole();
        if (role == null || role.isBlank()) {
            role = "user"; // default role if missing
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(), // must already be BCrypt encoded in DB
                Collections.singletonList(new SimpleGrantedAuthority( role))
        );
    }
}
