package com.homechief;

import com.homechief.model.User;
import com.homechief.repository.UserRepository;
import com.homechief.service.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testLoadUserByUsername_UserExists() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("encodedPass");
        user.setRole("admin");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        UserDetails result = customUserDetailsService.loadUserByUsername("test@example.com");

        assertNotNull(result);
        assertEquals("test@example.com", result.getUsername());
        assertEquals("encodedPass", result.getPassword());
        assertTrue(result.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("admin")));

        verify(userRepository, times(1)).findByEmail("test@example.com");
    }


    @Test
    void testLoadUserByUsername_UserNotFound() {
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername("notfound@example.com"));

        verify(userRepository, times(1)).findByEmail("notfound@example.com");
    }


    @Test
    void testLoadUserByUsername_RoleIsNullOrEmpty() {
        User user = new User();
        user.setEmail("test2@example.com");
        user.setPassword("encodedPass");
        user.setRole(null); // null role

        when(userRepository.findByEmail("test2@example.com")).thenReturn(Optional.of(user));

        UserDetails result = customUserDetailsService.loadUserByUsername("test2@example.com");

        assertNotNull(result);
        assertEquals("test2@example.com", result.getUsername());
        assertTrue(result.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("user"))); // default role

        verify(userRepository, times(1)).findByEmail("test2@example.com");
    }
}

