package com.homechief;

import com.homechief.controller.AuthController;
import com.homechief.dto.UserDTO;
import com.homechief.model.User;
import com.homechief.repository.UserRepository;
import com.homechief.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test signup with new user
    @Test
    void testSignup_NewUser() {
        UserDTO dto = new UserDTO();
        dto.setEmail("test@example.com");
        dto.setPassword("1234");
        dto.setName("Ali");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("1234")).thenReturn("encoded1234");

        ResponseEntity<String> response = authController.signup(dto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User registered successfully!", response.getBody());
        verify(userRepository, times(1)).save(any(User.class));
    }

    // Test signup with existing user
    @Test
    void testSignup_UserAlreadyExists() {
        UserDTO dto = new UserDTO();
        dto.setEmail("test@example.com");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(new User()));

        ResponseEntity<String> response = authController.signup(dto);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("User already exists!", response.getBody());
    }

    // Test login with invalid credentials
    @Test
    void testLogin_InvalidCredentials() {
        UserDTO dto = new UserDTO();
        dto.setEmail("wrong@example.com");
        dto.setPassword("wrongpass");

        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Invalid"));

        ResponseEntity<?> response = (ResponseEntity<?>) authController.login(dto);

        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Invalid email or password", response.getBody());
    }
}
