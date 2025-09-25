package com.homechief.controller;

import com.homechief.dto.ChangePasswordRequest;
import com.homechief.dto.UserDTO;
import com.homechief.dto.UserPreferencesDTO;
import com.homechief.dto.UserSettingsDTO;
import com.homechief.model.User;
import com.homechief.repository.UserRepository;
import com.homechief.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, UserService userService , PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/me")
    public UserDTO me(@AuthenticationPrincipal UserDetails userDetails) {
        User u = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        return userService.toDTO(u);
    }

    // --- LOGOUT ---
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // With stateless JWT, logout is client-side (delete token from storage)
        return ResponseEntity.ok("Signed out successfully. Please remove token from client storage.");
    }

    // --- CHANGE PASSWORD ---
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
        var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(401).body("Unauthorized - invalid or missing token");
        }

        String email = auth.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return ResponseEntity.ok("Password updated successfully");
    }

    // --- USER PREFERENCES ---
    @GetMapping("/preferences")
    public ResponseEntity<?> getPreferences(@AuthenticationPrincipal UserDetails userDetails) {
        User u = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        return ResponseEntity.ok(userService.getPreferences(u.getId()));
    }

    // Note: POST /users/{id}/preferences accepts UserPreferencesDTO now
    @PostMapping("/{id}/preferences")
    public ResponseEntity<?> savePreferences(@PathVariable Integer id,
                                             @RequestBody UserPreferencesDTO prefs) {
        return ResponseEntity.ok(userService.savePreferences(id, prefs));
    }

    // PUT /users/preferences (update current user's preferences)
    @PutMapping("/preferences")
    public ResponseEntity<?> updatePreferences(@AuthenticationPrincipal UserDetails userDetails,
                                               @RequestBody UserPreferencesDTO prefs) {
        User u = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        return ResponseEntity.ok(userService.updatePreferences(u.getId(), prefs));
    }

    // --- USER SETTINGS ---
    @GetMapping("/settings")
    public ResponseEntity<?> getSettings(@AuthenticationPrincipal UserDetails userDetails) {
        User u = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        return ResponseEntity.ok(userService.getSettings(u.getId()));
    }

    @PutMapping("/settings")
    public ResponseEntity<?> updateSettings(@AuthenticationPrincipal UserDetails userDetails,
                                            @RequestBody UserSettingsDTO settings) {
        User u = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        return ResponseEntity.ok(userService.updateSettings(u.getId(), settings));
    }

    // --- USER FAVORITES ---
    @GetMapping("/favorites")
    public ResponseEntity<?> getFavorites(@AuthenticationPrincipal UserDetails userDetails) {
        User u = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        return ResponseEntity.ok(userService.getFavorites(u.getId()));
    }

    @PostMapping("/favorites/{recipeId}")
    public ResponseEntity<?> addFavorite(@AuthenticationPrincipal UserDetails userDetails,
                                         @PathVariable Integer recipeId) {
        User u = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        return ResponseEntity.ok(userService.addFavorite(u.getId(), recipeId));
    }

    @DeleteMapping("/favorites/{recipeId}")
    public ResponseEntity<?> removeFavorite(@AuthenticationPrincipal UserDetails userDetails,
                                            @PathVariable Integer recipeId) {
        User u = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        userService.removeFavorite(u.getId(), recipeId);
        return ResponseEntity.ok("Favorite removed successfully");
    }
}
