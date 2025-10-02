package com.homechief.service;

import com.homechief.dto.RecipeStepDTO;
import com.homechief.dto.RecipeStepRequestDTO;
import com.homechief.model.RecipeSteps;
import com.homechief.model.Recipes;
import com.homechief.repository.RecipeStepRepository;
import com.homechief.repository.RecipesRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeStepService {

    private final RecipeStepRepository stepsRepo;
    private final RecipesRepository recipesRepo;

    public RecipeStepService(RecipeStepRepository stepsRepo, RecipesRepository recipesRepo) {
        this.stepsRepo = stepsRepo;
        this.recipesRepo = recipesRepo;
    }

    public List<RecipeStepDTO> getStepsForRecipe(Integer recipeId) {
        return stepsRepo.findByRecipeId(recipeId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public RecipeStepDTO addStep(Integer recipeId, RecipeStepRequestDTO req) {
        Recipes recipe = recipesRepo.findById(recipeId).orElseThrow(() -> new RuntimeException("Recipe not found"));
        RecipeSteps step = new RecipeSteps();
        step.setRecipe(recipe);
        step.setStepNumber(req.getStepNumber());
        step.setInstruction(req.getInstruction());
        RecipeSteps saved = stepsRepo.save(step);
        return toDTO(saved);
    }

    public RecipeStepDTO updateStep(Integer recipeId, Integer stepId, RecipeStepRequestDTO req) {
        RecipeSteps step = stepsRepo.findById(stepId).orElseThrow(() -> new RuntimeException("Step not found"));
        if (!step.getRecipe().getId().equals(recipeId)) throw new RuntimeException("Step does not belong to recipe");
        step.setStepNumber(req.getStepNumber());
        step.setInstruction(req.getInstruction());
        return toDTO(stepsRepo.save(step));
    }

    public void deleteStep(Integer recipeId, Integer stepId) {
        RecipeSteps step = stepsRepo.findById(stepId).orElseThrow(() -> new RuntimeException("Step not found"));
        if (!step.getRecipe().getId().equals(recipeId)) throw new RuntimeException("Step does not belong to recipe");
        stepsRepo.delete(step);
    }

    private RecipeStepDTO toDTO(RecipeSteps s) {
        RecipeStepDTO d = new RecipeStepDTO();
        d.setId(s.getId());
        d.setStepNumber(s.getStepNumber());
        d.setInstruction(s.getInstruction());
        return d;
    }
}
