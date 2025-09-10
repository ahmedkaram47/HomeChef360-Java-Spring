package com.homechief.controller;

import com.homechief.dto.UserDTO;
import com.homechief.model.User;
import com.homechief.repository.UserRepository;
import com.homechief.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/me")
    public UserDTO me(@AuthenticationPrincipal UserDetails userDetails) {
        User u = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        return userService.toDTO(u);
    }
}