package com.homechief.controller;

import com.homechief.dto.RecipeStepDTO;
import com.homechief.dto.RecipeStepRequestDTO;
import com.homechief.service.RecipeStepService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recipes/{recipeId}/steps")
public class RecipeStepsController {

    private final RecipeStepService service;

    public RecipeStepsController(RecipeStepService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<RecipeStepDTO>> getSteps(@PathVariable Integer recipeId) {
        return ResponseEntity.ok(service.getStepsForRecipe(recipeId));
    }

    @PostMapping
    public ResponseEntity<RecipeStepDTO> addStep(@AuthenticationPrincipal UserDetails ud,
                                                 @PathVariable Integer recipeId,
                                                 @RequestBody RecipeStepRequestDTO req) {
        // require authentication for write
        return ResponseEntity.ok(service.addStep(recipeId, req));
    }

    @PutMapping("/{stepId}")
    public ResponseEntity<RecipeStepDTO> updateStep(@AuthenticationPrincipal UserDetails ud,
                                                    @PathVariable Integer recipeId,
                                                    @PathVariable Integer stepId,
                                                    @RequestBody RecipeStepRequestDTO req) {
        return ResponseEntity.ok(service.updateStep(recipeId, stepId, req));
    }

    @DeleteMapping("/{stepId}")
    public ResponseEntity<String> deleteStep(@AuthenticationPrincipal UserDetails ud,
                                             @PathVariable Integer recipeId,
                                             @PathVariable Integer stepId) {
        service.deleteStep(recipeId, stepId);
        return ResponseEntity.ok("Step deleted");
    }
}
