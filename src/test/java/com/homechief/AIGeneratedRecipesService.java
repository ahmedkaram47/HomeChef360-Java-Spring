package com.homechief;

import com.homechief.dto.AIGeneratedRecipeDTO;
import com.homechief.dto.AIGeneratedRecipeRequestDTO;
import com.homechief.model.AIGeneratedRecipes;
import com.homechief.model.User;
import com.homechief.repository.AIGeneratedRecipesRepository;
import com.homechief.repository.UserRepository;
import com.homechief.service.AIGeneratedRecipesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AIGeneratedRecipesServiceTest {

    private AIGeneratedRecipesRepository repo;
    private UserRepository userRepo;
    private AIGeneratedRecipesService service;

    @BeforeEach
    void setUp() {
        repo = mock(AIGeneratedRecipesRepository.class);
        userRepo = mock(UserRepository.class);
        service = new AIGeneratedRecipesService(repo, userRepo);
    }

    @Test
    void testListForUser() {
        AIGeneratedRecipes g1 = new AIGeneratedRecipes();
        g1.setId(1);
        g1.setPrompt("p1");
        g1.setGeneratedRecipe("recipe1");
        g1.setCreatedAt(LocalDateTime.now());

        AIGeneratedRecipes g2 = new AIGeneratedRecipes();
        g2.setId(2);
        g2.setPrompt("p2");
        g2.setGeneratedRecipe("recipe2");
        g2.setCreatedAt(LocalDateTime.now());

        when(repo.findByUserId(5)).thenReturn(Arrays.asList(g1, g2));

        List<AIGeneratedRecipeDTO> result = service.listForUser(5);

        assertEquals(2, result.size());
        assertEquals("p1", result.get(0).getPrompt());
        assertEquals("recipe2", result.get(1).getGeneratedRecipe());
    }

    @Test
    void testGenerate() {
        int userId = 10;
        User user = new User();
        user.setId( userId);
        when(userRepo.findById((long) userId)).thenReturn(Optional.of(user));

        AIGeneratedRecipeRequestDTO req = new AIGeneratedRecipeRequestDTO();
        req.setPrompt("make vegan dish");

        AIGeneratedRecipes saved = new AIGeneratedRecipes();
        saved.setId(100);
        saved.setPrompt("make vegan dish");
        saved.setGeneratedRecipe("AI Recipe suggestion based on prompt: make vegan dish");
        saved.setCreatedAt(LocalDateTime.now());

        when(repo.save(any(AIGeneratedRecipes.class))).thenReturn(saved);

        AIGeneratedRecipeDTO dto = service.generate(userId, req);

        assertNotNull(dto);
        assertEquals(100, dto.getId());
        assertEquals("make vegan dish", dto.getPrompt());
        assertTrue(dto.getGeneratedRecipe().contains("AI Recipe suggestion"));

        // verify save was called with correct data
        ArgumentCaptor<AIGeneratedRecipes> captor = ArgumentCaptor.forClass(AIGeneratedRecipes.class);
        verify(repo).save(captor.capture());
        AIGeneratedRecipes passed = captor.getValue();

        assertEquals(user, passed.getUser());
        assertEquals("make vegan dish", passed.getPrompt());
    }

    @Test
    void testGenerate_UserNotFound() {
        when(userRepo.findById(99L)).thenReturn(Optional.empty());

        AIGeneratedRecipeRequestDTO req = new AIGeneratedRecipeRequestDTO();
        req.setPrompt("pasta");

        assertThrows(Exception.class, () -> service.generate(99, req));
    }
}
