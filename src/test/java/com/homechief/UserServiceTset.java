package com.homechief;

import com.homechief.dto.UserPreferencesDTO;
import com.homechief.dto.UserSettingsDTO;
import com.homechief.model.*;
import com.homechief.repository.*;
import com.homechief.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserPreferencesRepository userPreferencesRepository;

    @Mock
    private UserSettingsRepository userSettingsRepository;

    @Mock
    private UserFavoritesRepository userFavoritesRepository;

    @Mock
    private RecipesRepository recipesRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //  Test register new user
    @Test
    void testRegister_NewUser_Success() {
        String email = "test@example.com";
        String rawPassword = "12345";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(rawPassword)).thenReturn("encoded123");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User user = userService.register("Ali", email, rawPassword);

        assertNotNull(user);
        assertEquals("Ali", user.getName());
        assertEquals(email, user.getEmail());
        assertEquals("encoded123", user.getPassword());
        assertEquals("user", user.getRole());

        verify(userRepository, times(1)).save(any(User.class));
    }

    //  Test register with existing email
    @Test
    void testRegister_EmailAlreadyExists() {
        String email = "test@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(new User()));

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                userService.register("Ali", email, "12345")
        );

        assertEquals("Email already in use", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testGetPreferences_Found() {
        UserPreferences  prefs = new UserPreferences();
        prefs.setId(1);
        prefs.setDietaryType("Vegan");
        prefs.setAllergies("[\"nuts\"]");
        prefs.setDislikes("[\"onions\"]");
        prefs.setServings(2);
        prefs.setCreatedAt(LocalDateTime.now());
        prefs.setUpdatedAt(LocalDateTime.now());

        when(userPreferencesRepository.findByUserId(1)).thenReturn(Optional.of(prefs));

        UserPreferencesDTO dto = userService.getPreferences(1);

        assertNotNull(dto);
        assertEquals("Vegan", dto.getDietaryType());
        assertTrue(dto.getAllergies().contains("nuts"));
        assertTrue(dto.getDislikes().contains("onions"));
    }

    //  Test getPreferences - not found
    @Test
    void testGetPreferences_NotFound() {
        when(userPreferencesRepository.findByUserId(1)).thenReturn(Optional.empty());

        UserPreferencesDTO dto = userService.getPreferences(1);

        assertNull(dto);
    }

    //  Test savePreferences
    @Test
    void testSavePreferences_Success() {
        User user = new User();
        user.setId(1);
        user.setName("Ali");

        UserPreferencesDTO dto = new UserPreferencesDTO();
        dto.setDietaryType("Vegetarian");
        dto.setAllergies(List.of("milk"));
        dto.setDislikes(List.of("fish"));
        dto.setServings(3);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userPreferencesRepository.save(any(UserPreferences.class))).thenAnswer(invocation -> {
            UserPreferences entity = invocation.getArgument(0);
            entity.setId(10);
            return entity;
        });

        UserPreferencesDTO savedDto = userService.savePreferences(1, dto);

        assertNotNull(savedDto);
        assertEquals("Vegetarian", savedDto.getDietaryType());
        assertTrue(savedDto.getAllergies().contains("milk"));
        assertEquals(3, savedDto.getServings());

        verify(userPreferencesRepository, times(1)).save(any(UserPreferences.class));
    }

    //  Test updatePreferences
    @Test
    void testUpdatePreferences_Success() {
        UserPreferences existing = new UserPreferences();
        existing.setId(5);
        existing.setDietaryType("Keto");
        existing.setServings(1);
        existing.setAllergies("[\"eggs\"]");

        UserPreferencesDTO dto = new UserPreferencesDTO();
        dto.setDietaryType("Low Carb");
        dto.setAllergies(List.of("gluten"));
        dto.setDislikes(List.of("sugar"));
        dto.setServings(4);

        when(userPreferencesRepository.findByUserId(1)).thenReturn(Optional.of(existing));
        when(userPreferencesRepository.save(any(UserPreferences.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserPreferencesDTO updatedDto = userService.updatePreferences(1, dto);

        assertNotNull(updatedDto);
        assertEquals("Low Carb", updatedDto.getDietaryType());
        assertTrue(updatedDto.getAllergies().contains("gluten"));
        assertEquals(4, updatedDto.getServings());

        verify(userPreferencesRepository, times(1)).save(existing);
    }

    //  Test updatePreferences - not found
    @Test
    void testUpdatePreferences_NotFound() {
        UserPreferencesDTO dto = new UserPreferencesDTO();
        dto.setDietaryType("Low Carb");

        when(userPreferencesRepository.findByUserId(1)).thenReturn(Optional.empty());

        Exception ex = assertThrows(RuntimeException.class, () ->
                userService.updatePreferences(1, dto));

        assertEquals("Preferences not found", ex.getMessage());
    }

    @Test
    void testGetSettings_Found() {
        UserSettings settings = new UserSettings();
        settings.setId(1);
        settings.setLanguage("en");
        settings.setTheme("dark");
        settings.setMeasurementUnits("metric");
        settings.setDefaultCuisine("Italian");
        settings.setDefaultServings(2);
        settings.setNotifications("{\"email\":true}");
        settings.setCreatedAt(LocalDateTime.now());
        settings.setUpdatedAt(LocalDateTime.now());

        when(userSettingsRepository.findByUserId(1)).thenReturn(Optional.of(settings));

        UserSettingsDTO dto = userService.getSettings(1);

        assertNotNull(dto);
        assertEquals("en", dto.getLanguage());
        assertEquals("dark", dto.getTheme());
        assertEquals("metric", dto.getMeasurementUnits());
        assertEquals("Italian", dto.getDefaultCuisine());
        assertEquals(2, dto.getDefaultServings());
    }

    // Test getSettings - not found
    @Test
    void testGetSettings_NotFound() {
        when(userSettingsRepository.findByUserId(1)).thenReturn(Optional.empty());

        UserSettingsDTO dto = userService.getSettings(1);

        assertNull(dto);
    }

    // Test updateSettings - new settings created
    @Test
    void testUpdateSettings_New() {
        User user = new User();
        user.setId(1);

        UserSettingsDTO dto = new UserSettingsDTO();
        dto.setLanguage("ar");
        dto.setTheme("light");
        dto.setMeasurementUnits("imperial");
        dto.setDefaultCuisine("Egyptian");
        dto.setDefaultServings(5);
        dto.setNotifications("{\"sms\":true}");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userSettingsRepository.findByUserId(1)).thenReturn(Optional.empty());
        when(userSettingsRepository.save(any(UserSettings.class))).thenAnswer(invocation -> {
            UserSettings entity = invocation.getArgument(0);
            entity.setId(100);
            return entity;
        });

        UserSettingsDTO updated = userService.updateSettings(1, dto);

        assertNotNull(updated);
        assertEquals("ar", updated.getLanguage());
        assertEquals("light", updated.getTheme());
        assertEquals("imperial", updated.getMeasurementUnits());
        assertEquals("Egyptian", updated.getDefaultCuisine());
        assertEquals(5, updated.getDefaultServings());

        verify(userSettingsRepository, times(1)).save(any(UserSettings.class));
    }

    //  Test updateSettings - update existing settings
    @Test
    void testUpdateSettings_Existing() {
        User user = new User();
        user.setId(1);

        UserSettings existing = new UserSettings();
        existing.setId(10);
        existing.setLanguage("en");
        existing.setTheme("dark");

        UserSettingsDTO dto = new UserSettingsDTO();
        dto.setLanguage("fr");
        dto.setTheme("light");
        dto.setMeasurementUnits("metric");
        dto.setDefaultCuisine("French");
        dto.setDefaultServings(3);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userSettingsRepository.findByUserId(1)).thenReturn(Optional.of(existing));
        when(userSettingsRepository.save(any(UserSettings.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserSettingsDTO updated = userService.updateSettings(1, dto);

        assertNotNull(updated);
        assertEquals("fr", updated.getLanguage());
        assertEquals("light", updated.getTheme());
        assertEquals("metric", updated.getMeasurementUnits());
        assertEquals("French", updated.getDefaultCuisine());
        assertEquals(3, updated.getDefaultServings());

        verify(userSettingsRepository, times(1)).save(existing);
    }


    @Test
    void testGetFavorites_Found() {
        UserFavorites fav1 = new UserFavorites();
        fav1.setId(1);
        UserFavorites fav2 = new UserFavorites();
        fav2.setId(2);

        when(userFavoritesRepository.findByUserId(1)).thenReturn(List.of(fav1, fav2));

        List<UserFavorites> favorites = userService.getFavorites(1);

        assertNotNull(favorites);
        assertEquals(2, favorites.size());
        verify(userFavoritesRepository, times(1)).findByUserId(1);
    }

    @Test
    void testGetFavorites_Empty() {
        when(userFavoritesRepository.findByUserId(1)).thenReturn(List.of());

        List<UserFavorites> favorites = userService.getFavorites(1);

        assertNotNull(favorites);
        assertTrue(favorites.isEmpty());
    }

    @Test
    void testAddFavorite_Success() {
        User user = new User();
        user.setId(1);
        Recipes recipe = new Recipes();
        recipe.setId(100);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(recipesRepository.findById(100)).thenReturn(Optional.of(recipe));
        when(userFavoritesRepository.findByUserIdAndRecipeId(1, 100)).thenReturn(Optional.empty());
        when(userFavoritesRepository.save(any(UserFavorites.class))).thenAnswer(invocation -> {
            UserFavorites fav = invocation.getArgument(0);
            fav.setId(10);
            return fav;
        });

        UserFavorites saved = userService.addFavorite(1, 100);

        assertNotNull(saved);
        assertEquals(user, saved.getUser());
        assertEquals(recipe, saved.getRecipe());
        verify(userFavoritesRepository, times(1)).save(any(UserFavorites.class));
    }

    @Test
    void testAddFavorite_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                userService.addFavorite(1, 100));

        assertEquals("User not found", ex.getMessage());
        verify(userFavoritesRepository, never()).save(any());
    }

    @Test
    void testAddFavorite_RecipeNotFound() {
        User user = new User();
        user.setId(1);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(recipesRepository.findById(100)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                userService.addFavorite(1, 100));

        assertEquals("Recipe not found", ex.getMessage());
        verify(userFavoritesRepository, never()).save(any());
    }

    @Test
    void testAddFavorite_AlreadyExists() {
        User user = new User();
        user.setId(1);
        Recipes recipe = new Recipes();
        recipe.setId(100);
        UserFavorites existing = new UserFavorites();
        existing.setId(50);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(recipesRepository.findById(100)).thenReturn(Optional.of(recipe));
        when(userFavoritesRepository.findByUserIdAndRecipeId(1, 100)).thenReturn(Optional.of(existing));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                userService.addFavorite(1, 100));

        assertEquals("Recipe already in favorites", ex.getMessage());
        verify(userFavoritesRepository, never()).save(any());
    }

    @Test
    void testRemoveFavorite_Success() {
        UserFavorites fav = new UserFavorites();
        fav.setId(10);

        when(userFavoritesRepository.findByUserIdAndRecipeId(1, 100))
                .thenReturn(Optional.of(fav));

        userService.removeFavorite(1, 100);

        verify(userFavoritesRepository, times(1)).delete(fav);
    }

    @Test
    void testRemoveFavorite_NotFound() {
        when(userFavoritesRepository.findByUserIdAndRecipeId(1, 100))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                userService.removeFavorite(1, 100));

        assertEquals("Favorite not found", ex.getMessage());
        verify(userFavoritesRepository, never()).delete(any());
    }



}
