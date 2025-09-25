package com.homechief.controller;

import com.homechief.dto.IngredientDTO;
import com.homechief.dto.UserPantryDTO;
import com.homechief.dto.UserPantryRequestDTO;
import com.homechief.model.Ingredients;
import com.homechief.model.User;
import com.homechief.model.UserPantry;
import com.homechief.repository.IngredientsRepository;
import com.homechief.repository.UserPantryRepository;
import com.homechief.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/pantry")
public class UserPantryController {

    private final UserPantryRepository userPantryRepository;
    private final UserRepository userRepository;
    private final IngredientsRepository ingredientsRepository;

    public UserPantryController(UserPantryRepository userPantryRepository,
                                UserRepository userRepository,
                                IngredientsRepository ingredientsRepository) {
        this.userPantryRepository = userPantryRepository;
        this.userRepository = userRepository;
        this.ingredientsRepository = ingredientsRepository;
    }

    // --- Get Pantry ---
    @GetMapping("/all")
    public ResponseEntity<?> getPantry(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        List<UserPantry> pantryItems = userPantryRepository.findByUserId(user.getId());

        List<UserPantryDTO> dtoList = pantryItems.stream().map(this::toDTO).toList();
        return ResponseEntity.ok(dtoList);
    }

    // --- Add Ingredient to Pantry ---
    @PostMapping("/add")
    public ResponseEntity<?> addToPantry(@AuthenticationPrincipal UserDetails userDetails,
                                         @RequestBody UserPantryRequestDTO request) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        Ingredients ingredient = ingredientsRepository.findById(request.getIngredientId())
                .orElseThrow(() -> new RuntimeException("Ingredient not found"));

        UserPantry pantryItem = new UserPantry();
        pantryItem.setUser(user);
        pantryItem.setIngredient(ingredient);
        pantryItem.setQuantity(request.getQuantity());
        pantryItem.setUnit(request.getUnit());
        pantryItem.setCategory(request.getCategory());
        pantryItem.setNotes(request.getNotes());
        pantryItem.setExpiryDate(request.getExpiryDate());
        pantryItem.setCreatedAt(LocalDateTime.now());
        pantryItem.setUpdatedAt(LocalDateTime.now());

        return ResponseEntity.ok(toDTO(userPantryRepository.save(pantryItem)));
    }

    // --- Update Pantry Item ---
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updatePantryItem(@AuthenticationPrincipal UserDetails userDetails,
                                              @PathVariable Integer id,
                                              @RequestBody UserPantryRequestDTO request) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        UserPantry pantryItem = userPantryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pantry item not found"));

        if (!pantryItem.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("Not allowed to update this item");
        }

        Ingredients ingredient = ingredientsRepository.findById(request.getIngredientId())
                .orElseThrow(() -> new RuntimeException("Ingredient not found"));

        pantryItem.setIngredient(ingredient);
        pantryItem.setQuantity(request.getQuantity());
        pantryItem.setUnit(request.getUnit());
        pantryItem.setCategory(request.getCategory());
        pantryItem.setNotes(request.getNotes());
        pantryItem.setExpiryDate(request.getExpiryDate());
        pantryItem.setUpdatedAt(LocalDateTime.now());

        return ResponseEntity.ok(toDTO(userPantryRepository.save(pantryItem)));
    }

    // --- Delete Pantry Item ---
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePantryItem(@AuthenticationPrincipal UserDetails userDetails,
                                              @PathVariable Integer id) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        UserPantry pantryItem = userPantryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pantry item not found"));

        if (!pantryItem.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("Not allowed to delete this item");
        }

        userPantryRepository.delete(pantryItem);
        return ResponseEntity.ok("Pantry item deleted successfully");
    }

    // --- Helper: Entity -> DTO ---
    private UserPantryDTO toDTO(UserPantry pantry) {
        UserPantryDTO dto = new UserPantryDTO();
        dto.setId(pantry.getId());
        dto.setQuantity(pantry.getQuantity());
        dto.setUnit(pantry.getUnit());
        dto.setCategory(pantry.getCategory());
        dto.setNotes(pantry.getNotes());
        dto.setExpiryDate(pantry.getExpiryDate() != null ? pantry.getExpiryDate().toLocalDate() : null);

        IngredientDTO ingredientDTO = new IngredientDTO();
        ingredientDTO.setId(pantry.getIngredient().getId());
        ingredientDTO.setName(pantry.getIngredient().getName());
        ingredientDTO.setCategory(pantry.getIngredient().getCategory());
        ingredientDTO.setUnit(pantry.getIngredient().getUnit());

        dto.setIngredient(ingredientDTO);
        return dto;
    }
}
