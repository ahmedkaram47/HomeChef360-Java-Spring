package com.homechief.service;

import com.homechief.dto.AIGeneratedRecipeDTO;
import com.homechief.dto.AIGeneratedRecipeRequestDTO;
import com.homechief.model.AIGeneratedRecipes;
import com.homechief.model.User;
import com.homechief.repository.AIGeneratedRecipesRepository;
import com.homechief.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AIGeneratedRecipesService {

    private final AIGeneratedRecipesRepository repo;
    private final UserRepository userRepo;

    public AIGeneratedRecipesService(AIGeneratedRecipesRepository repo, UserRepository userRepo) {
        this.repo = repo;
        this.userRepo = userRepo;
    }

    public List<AIGeneratedRecipeDTO> listForUser(Integer userId) {
        return repo.findByUserId(userId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public AIGeneratedRecipeDTO generate(Integer userId, AIGeneratedRecipeRequestDTO req) {
        User u = userRepo.findById(Long.valueOf(userId)).orElseThrow();

        // For now: stub generation logic
        String generatedText = "AI Recipe suggestion based on prompt: " + req.getPrompt();

        AIGeneratedRecipes g = new AIGeneratedRecipes();
        g.setUser(u);
        g.setPrompt(req.getPrompt());
        g.setGeneratedRecipe(generatedText);
        g.setCreatedAt(LocalDateTime.now());

        return toDTO(repo.save(g));
    }

    private AIGeneratedRecipeDTO toDTO(AIGeneratedRecipes g) {
        AIGeneratedRecipeDTO d = new AIGeneratedRecipeDTO();
        d.setId(g.getId());
        d.setPrompt(g.getPrompt());
        d.setGeneratedRecipe(g.getGeneratedRecipe());
        d.setCreatedAt(g.getCreatedAt());
        return d;
    }
}
