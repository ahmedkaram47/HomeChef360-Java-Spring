package com.homechief.controller;

import com.homechief.dto.AIGeneratedRecipeDTO;
import com.homechief.dto.AIGeneratedRecipeRequestDTO;
import com.homechief.model.User;
import com.homechief.repository.UserRepository;
import com.homechief.service.AIGeneratedRecipesService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/AIrecipes")
public class AIGeneratedRecipesController {

    private final AIGeneratedRecipesService service;
    private final UserRepository userRepo;

    public AIGeneratedRecipesController(AIGeneratedRecipesService service, UserRepository userRepo) {
        this.service = service;
        this.userRepo = userRepo;
    }

    @GetMapping
    public ResponseEntity<List<AIGeneratedRecipeDTO>> list(@AuthenticationPrincipal UserDetails ud) {
        User u = userRepo.findByEmail(ud.getUsername()).orElseThrow();
        return ResponseEntity.ok(service.listForUser(u.getId()));
    }

    @PostMapping
    public ResponseEntity<AIGeneratedRecipeDTO> generate(@AuthenticationPrincipal UserDetails ud,
                                                         @RequestBody AIGeneratedRecipeRequestDTO req) {
        User u = userRepo.findByEmail(ud.getUsername()).orElseThrow();
        return ResponseEntity.ok(service.generate(u.getId(), req));
    }
}
