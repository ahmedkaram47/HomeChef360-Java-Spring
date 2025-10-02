package com.homechief.controller;

import com.homechief.dto.*;
import com.homechief.model.User;
import com.homechief.repository.UserRepository;
import com.homechief.service.MealPlanService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mealplans")
public class MealPlansController {

    private final MealPlanService service;
    private final UserRepository userRepository;

    public MealPlansController(MealPlanService service, UserRepository userRepository) {
        this.service = service;
        this.userRepository = userRepository;
    }

    // Get all plans for current user
    @GetMapping("/all")
    public ResponseEntity<List<MealPlanDTO>> getMyPlans(@AuthenticationPrincipal UserDetails userDetails) {
        User u = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(service.getMealPlansForUser(u.getId()));
    }

    // Create
    @PostMapping("/add")
    public ResponseEntity<MealPlanDTO> createPlan(@AuthenticationPrincipal UserDetails userDetails,
                                                  @RequestBody MealPlanRequestDTO req) {
        User u = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(service.createMealPlan(u.getId(), req));
    }

    // Get by id
    @GetMapping("/{id}")
    public ResponseEntity<MealPlanDTO> getPlan(@AuthenticationPrincipal UserDetails userDetails,
                                               @PathVariable Integer id) {
        // optional: enforce ownership
        MealPlanDTO dto = service.getMealPlan(id);
        if (dto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<MealPlanDTO> updatePlan(@AuthenticationPrincipal UserDetails userDetails,
                                                  @PathVariable Integer id,
                                                  @RequestBody MealPlanRequestDTO req) {
        User u = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));
        try {
            return ResponseEntity.ok(service.updateMealPlan(id, u.getId(), req));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(403).body(null);
        }
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlan(@AuthenticationPrincipal UserDetails userDetails,
                                        @PathVariable Integer id) {
        User u = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));
        try {
            service.deleteMealPlan(id, u.getId());
            return ResponseEntity.ok("Meal plan deleted");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(403).body(ex.getMessage());
        }
    }

    // ========== ITEMS ==========
    @GetMapping("/{id}/items")
    public ResponseEntity<List<MealPlanItemDTO>> getItems(@AuthenticationPrincipal UserDetails userDetails,
                                                          @PathVariable Integer id) {
        // no ownership check for read (optional to enforce)
        return ResponseEntity.ok(service.getItemsForMealPlan(id));
    }

    @PostMapping("/{id}/items")
    public ResponseEntity<?> addItem(@AuthenticationPrincipal UserDetails userDetails,
                                     @PathVariable Integer id,
                                     @RequestBody MealPlanItemRequestDTO req) {
        User u = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));
        try {
            MealPlanItemDTO created = service.addItem(id, u.getId(), req);
            return ResponseEntity.ok(created);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(403).body(ex.getMessage());
        }
    }

    @PutMapping("/{id}/items/{itemId}")
    public ResponseEntity<?> updateItem(@AuthenticationPrincipal UserDetails userDetails,
                                        @PathVariable Integer id,
                                        @PathVariable Integer itemId,
                                        @RequestBody MealPlanItemRequestDTO req) {
        User u = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));
        try {
            MealPlanItemDTO updated = service.updateItem(id, itemId, u.getId(), req);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(403).body(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}/items/{itemId}")
    public ResponseEntity<?> deleteItem(@AuthenticationPrincipal UserDetails userDetails,
                                        @PathVariable Integer id,
                                        @PathVariable Integer itemId) {
        User u = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));
        try {
            service.deleteItem(id, itemId, u.getId());
            return ResponseEntity.ok("Item deleted");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(403).body(ex.getMessage());
        }
    }
}
