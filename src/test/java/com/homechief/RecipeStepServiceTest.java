package com.homechief;

import com.homechief.dto.RecipeStepDTO;
import com.homechief.dto.RecipeStepRequestDTO;
import com.homechief.model.RecipeSteps;
import com.homechief.model.Recipes;
import com.homechief.repository.RecipeStepRepository;
import com.homechief.repository.RecipesRepository;
import com.homechief.service.RecipeStepService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecipeStepServiceTest {

    private RecipeStepRepository stepsRepo;
    private RecipesRepository recipesRepo;
    private RecipeStepService service;

    @BeforeEach
    void setUp() {
        stepsRepo = mock(RecipeStepRepository.class);
        recipesRepo = mock(RecipesRepository.class);
        service = new RecipeStepService(stepsRepo, recipesRepo);
    }

    @Test
    void testGetStepsForRecipe() {
        RecipeSteps s1 = new RecipeSteps();
        s1.setId(1);
        s1.setStepNumber(1);
        s1.setInstruction("Chop onions");

        RecipeSteps s2 = new RecipeSteps();
        s2.setId(2);
        s2.setStepNumber(2);
        s2.setInstruction("Fry onions");

        when(stepsRepo.findByRecipeId(10)).thenReturn(Arrays.asList(s1, s2));

        List<RecipeStepDTO> result = service.getStepsForRecipe(10);

        assertEquals(2, result.size());
        assertEquals("Chop onions", result.get(0).getInstruction());
        assertEquals(2, result.get(1).getStepNumber());
    }

    @Test
    void testAddStep() {
        Recipes recipe = new Recipes();
        recipe.setId(5);
        when(recipesRepo.findById(5)).thenReturn(Optional.of(recipe));

        RecipeStepRequestDTO req = new RecipeStepRequestDTO();
        req.setStepNumber(1);
        req.setInstruction("Boil water");

        RecipeSteps saved = new RecipeSteps();
        saved.setId(100);
        saved.setRecipe(recipe);
        saved.setStepNumber(1);
        saved.setInstruction("Boil water");

        when(stepsRepo.save(any(RecipeSteps.class))).thenReturn(saved);

        RecipeStepDTO dto = service.addStep(5, req);

        assertNotNull(dto);
        assertEquals(100, dto.getId());
        assertEquals("Boil water", dto.getInstruction());

        verify(stepsRepo).save(any(RecipeSteps.class));
    }

    @Test
    void testAddStep_RecipeNotFound() {
        when(recipesRepo.findById(99)).thenReturn(Optional.empty());

        RecipeStepRequestDTO req = new RecipeStepRequestDTO();
        req.setStepNumber(1);
        req.setInstruction("Mix flour");

        assertThrows(RuntimeException.class, () -> service.addStep(99, req));
    }

    @Test
    void testUpdateStep() {
        Recipes recipe = new Recipes();
        recipe.setId(5);

        RecipeSteps step = new RecipeSteps();
        step.setId(200);
        step.setRecipe(recipe);
        step.setStepNumber(1);
        step.setInstruction("Old instruction");

        when(stepsRepo.findById(200)).thenReturn(Optional.of(step));
        when(stepsRepo.save(any(RecipeSteps.class))).thenReturn(step);

        RecipeStepRequestDTO req = new RecipeStepRequestDTO();
        req.setStepNumber(2);
        req.setInstruction("New instruction");

        RecipeStepDTO dto = service.updateStep(5, 200, req);

        assertEquals(200, dto.getId());
        assertEquals(2, dto.getStepNumber());
        assertEquals("New instruction", dto.getInstruction());
    }

    @Test
    void testUpdateStep_WrongRecipe() {
        Recipes recipe = new Recipes();
        recipe.setId(5);

        RecipeSteps step = new RecipeSteps();
        step.setId(200);
        step.setRecipe(recipe);

        when(stepsRepo.findById(200)).thenReturn(Optional.of(step));

        RecipeStepRequestDTO req = new RecipeStepRequestDTO();
        req.setStepNumber(1);
        req.setInstruction("test");

        assertThrows(RuntimeException.class, () -> service.updateStep(99, 200, req));
    }

    @Test
    void testDeleteStep() {
        Recipes recipe = new Recipes();
        recipe.setId(5);

        RecipeSteps step = new RecipeSteps();
        step.setId(300);
        step.setRecipe(recipe);

        when(stepsRepo.findById(300)).thenReturn(Optional.of(step));

        service.deleteStep(5, 300);

        verify(stepsRepo).delete(step);
    }

    @Test
    void testDeleteStep_WrongRecipe() {
        Recipes recipe = new Recipes();
        recipe.setId(5);

        RecipeSteps step = new RecipeSteps();
        step.setId(300);
        step.setRecipe(recipe);

        when(stepsRepo.findById(300)).thenReturn(Optional.of(step));

        assertThrows(RuntimeException.class, () -> service.deleteStep(99, 300));
    }
}
