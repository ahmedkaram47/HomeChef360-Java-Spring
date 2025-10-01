package com.homechief.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.homechief.dto.UserDTO;
import com.homechief.dto.UserPreferencesDTO;
import com.homechief.dto.UserSettingsDTO;
import com.homechief.model.*;
import com.homechief.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserPreferencesRepository userPreferencesRepository;
    private final UserSettingsRepository userSettingsRepository;
    private final UserFavoritesRepository userFavoritesRepository;
    private final RecipesRepository recipesRepository;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;

    public UserService(UserRepository userRepository,
                       UserPreferencesRepository userPreferencesRepository,
                       UserSettingsRepository userSettingsRepository,
                       UserFavoritesRepository userFavoritesRepository,
                       RecipesRepository recipesRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userPreferencesRepository = userPreferencesRepository;
        this.userSettingsRepository = userSettingsRepository;
        this.userFavoritesRepository = userFavoritesRepository;
        this.recipesRepository = recipesRepository;
        this.passwordEncoder = passwordEncoder;
        this.objectMapper = new ObjectMapper();
    }

    // basic register / DTO conversion
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

    // ------------------- PREFERENCES (DTO-based) -------------------
    public UserPreferencesDTO getPreferences(Integer userId) {
        return userPreferencesRepository.findByUserId(userId)
                .map(this::toPreferencesDTO)
                .orElse(null);
    }

    public UserPreferencesDTO savePreferences(Integer userId, UserPreferencesDTO dto) {
        User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new RuntimeException("User not found"));
        UserPreferences entity = toPreferencesEntity(dto, user);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        UserPreferences saved = userPreferencesRepository.save(entity);
        return toPreferencesDTO(saved);
    }

    public UserPreferencesDTO updatePreferences(Integer userId, UserPreferencesDTO dto) {
        UserPreferences existing = userPreferencesRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Preferences not found"));

        existing.setDietaryType(dto.getDietaryType());
        try {
            existing.setAllergies(dto.getAllergies() != null ? objectMapper.writeValueAsString(dto.getAllergies()) : null);
            existing.setDislikes(dto.getDislikes() != null ? objectMapper.writeValueAsString(dto.getDislikes()) : null);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing JSON fields", e);
        }
        existing.setServings(dto.getServings());
        existing.setUpdatedAt(LocalDateTime.now());

        UserPreferences saved = userPreferencesRepository.save(existing);
        return toPreferencesDTO(saved);
    }

    private UserPreferencesDTO toPreferencesDTO(UserPreferences entity) {
        UserPreferencesDTO dto = new UserPreferencesDTO();
        dto.setId(entity.getId());
        dto.setDietaryType(entity.getDietaryType());

        try {
            if (entity.getAllergies() != null) {
                List<String> allergies = objectMapper.readValue(entity.getAllergies(), List.class);
                dto.setAllergies(allergies);
            } else {
                dto.setAllergies(Collections.emptyList());
            }

            if (entity.getDislikes() != null) {
                List<String> dislikes = objectMapper.readValue(entity.getDislikes(), List.class);
                dto.setDislikes(dislikes);
            } else {
                dto.setDislikes(Collections.emptyList());
            }
        } catch (Exception e) {
            dto.setAllergies(Collections.emptyList());
            dto.setDislikes(Collections.emptyList());
        }

        dto.setServings(entity.getServings());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    private UserPreferences toPreferencesEntity(UserPreferencesDTO dto, User user) {
        UserPreferences entity = new UserPreferences();
        entity.setUser(user);
        entity.setDietaryType(dto.getDietaryType());
        try {
            entity.setAllergies(dto.getAllergies() != null ? objectMapper.writeValueAsString(dto.getAllergies()) : null);
            entity.setDislikes(dto.getDislikes() != null ? objectMapper.writeValueAsString(dto.getDislikes()) : null);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing JSON fields", e);
        }
        entity.setServings(dto.getServings());
        return entity;
    }

    // ------------------- SETTINGS (DTO-based) -------------------
    public UserSettingsDTO getSettings(Integer userId) {
        return userSettingsRepository.findByUserId(userId)
                .map(this::toSettingsDTO)
                .orElse(null);
    }

    public UserSettingsDTO updateSettings(Integer userId, UserSettingsDTO dto) {
        UserSettings existing = userSettingsRepository.findByUserId(userId)
                .orElse(new UserSettings());

        existing.setUser(userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new RuntimeException("User not found")));
        existing.setLanguage(dto.getLanguage());
        existing.setTheme(dto.getTheme());
        existing.setMeasurementUnits(dto.getMeasurementUnits());
        existing.setDefaultCuisine(dto.getDefaultCuisine());
        existing.setDefaultServings(dto.getDefaultServings());

        // notifications: DTO holds Object (Map/List) or String - serialize safely
        try {
            if (dto.getNotifications() != null) {
                existing.setNotifications(objectMapper.writeValueAsString(dto.getNotifications()));
            } else {
                existing.setNotifications(null);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing notifications JSON", e);
        }

        existing.setUpdatedAt(LocalDateTime.now());

        UserSettings saved = userSettingsRepository.save(existing);
        return toSettingsDTO(saved);
    }

    private UserSettingsDTO toSettingsDTO(UserSettings entity) {
        UserSettingsDTO dto = new UserSettingsDTO();
        dto.setId(entity.getId());
        dto.setLanguage(entity.getLanguage());
        dto.setTheme(entity.getTheme());
        dto.setMeasurementUnits(entity.getMeasurementUnits());
        dto.setDefaultCuisine(entity.getDefaultCuisine());
        dto.setDefaultServings(entity.getDefaultServings());

        // parse notifications JSON back to a generic Object (Map/List) if present
        try {
            if (entity.getNotifications() != null) {
                Object notificationsObj = objectMapper.readValue(entity.getNotifications(), Object.class);
                dto.setNotifications((String) notificationsObj);
            } else {
                dto.setNotifications(null);
            }
        } catch (Exception e) {
            dto.setNotifications(null);
        }

        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    // ------------------- FAVORITES -------------------
    public List<UserFavorites> getFavorites(Integer userId) {
        return userFavoritesRepository.findByUserId(userId);
    }

    public UserFavorites addFavorite(Integer userId, Integer recipeId) {
        User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new RuntimeException("User not found"));
        Recipes recipe = recipesRepository.findById(recipeId).orElseThrow(() -> new RuntimeException("Recipe not found"));

        if (userFavoritesRepository.findByUserIdAndRecipeId(userId, recipeId).isPresent()) {
            throw new RuntimeException("Recipe already in favorites");
        }

        UserFavorites fav = new UserFavorites();
        fav.setUser(user);
        fav.setRecipe(recipe);
        fav.setCreatedAt(LocalDateTime.now());

        return userFavoritesRepository.save(fav);
    }

    public void removeFavorite(Integer userId, Integer recipeId) {
        UserFavorites fav = userFavoritesRepository.findByUserIdAndRecipeId(userId, recipeId)
                .orElseThrow(() -> new RuntimeException("Favorite not found"));
        userFavoritesRepository.delete(fav);
    }


}
