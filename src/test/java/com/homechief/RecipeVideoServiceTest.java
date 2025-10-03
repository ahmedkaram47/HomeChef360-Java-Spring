package com.homechief;

import com.homechief.dto.RecipeVideoDTO;
import com.homechief.dto.RecipeVideoRequestDTO;
import com.homechief.model.RecipeVideos;
import com.homechief.model.Recipes;
import com.homechief.repository.RecipeVideoRepository;
import com.homechief.repository.RecipesRepository;
import com.homechief.service.RecipeVideoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecipeVideoServiceTest {

    private RecipeVideoRepository videosRepo;
    private RecipesRepository recipesRepo;
    private RecipeVideoService service;

    @BeforeEach
    void setUp() {
        videosRepo = mock(RecipeVideoRepository.class);
        recipesRepo = mock(RecipesRepository.class);
        service = new RecipeVideoService(videosRepo, recipesRepo);
    }

    @Test
    void testGetVideosForRecipe() {
        RecipeVideos v1 = new RecipeVideos();
        v1.setId(1);
        v1.setVideoUrl("url1");
        v1.setCaption("caption1");

        when(videosRepo.findByRecipeId(10)).thenReturn(Arrays.asList(v1));

        List<RecipeVideoDTO> result = service.getVideosForRecipe(10);

        assertEquals(1, result.size());
        assertEquals("url1", result.get(0).getVideoUrl());
        assertEquals("caption1", result.get(0).getCaption());
    }

    @Test
    void testAddVideoSuccess() {
        Recipes recipe = new Recipes();
        recipe.setId(10);

        RecipeVideoRequestDTO req = new RecipeVideoRequestDTO();
        req.setVideoUrl("test-url");
        req.setCaption("test-caption");

        when(recipesRepo.findById(10)).thenReturn(Optional.of(recipe));

        RecipeVideos saved = new RecipeVideos();
        saved.setId(1);
        saved.setVideoUrl("test-url");
        saved.setCaption("test-caption");
        saved.setRecipe(recipe);

        when(videosRepo.save(any(RecipeVideos.class))).thenReturn(saved);

        RecipeVideoDTO result = service.addVideo(10, req);

        assertEquals(1, result.getId());
        assertEquals("test-url", result.getVideoUrl());
        assertEquals("test-caption", result.getCaption());

        verify(videosRepo, times(1)).save(any(RecipeVideos.class));
    }

    @Test
    void testAddVideoRecipeNotFound() {
        RecipeVideoRequestDTO req = new RecipeVideoRequestDTO();
        when(recipesRepo.findById(10)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.addVideo(10, req));
    }

    @Test
    void testUpdateVideoSuccess() {
        Recipes recipe = new Recipes();
        recipe.setId(10);

        RecipeVideos video = new RecipeVideos();
        video.setId(5);
        video.setRecipe(recipe);
        video.setVideoUrl("old-url");
        video.setCaption("old-caption");

        RecipeVideoRequestDTO req = new RecipeVideoRequestDTO();
        req.setVideoUrl("new-url");
        req.setCaption("new-caption");

        when(videosRepo.findById(5)).thenReturn(Optional.of(video));
        when(videosRepo.save(video)).thenReturn(video);

        RecipeVideoDTO result = service.updateVideo(10, 5, req);

        assertEquals("new-url", result.getVideoUrl());
        assertEquals("new-caption", result.getCaption());
    }

    @Test
    void testUpdateVideoNotFound() {
        RecipeVideoRequestDTO req = new RecipeVideoRequestDTO();
        when(videosRepo.findById(5)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.updateVideo(10, 5, req));
    }

    @Test
    void testUpdateVideoWrongRecipe() {
        Recipes recipe1 = new Recipes();
        recipe1.setId(10);

        Recipes recipe2 = new Recipes();
        recipe2.setId(20);

        RecipeVideos video = new RecipeVideos();
        video.setId(5);
        video.setRecipe(recipe2);

        RecipeVideoRequestDTO req = new RecipeVideoRequestDTO();
        req.setVideoUrl("url");
        req.setCaption("caption");

        when(videosRepo.findById(5)).thenReturn(Optional.of(video));

        assertThrows(RuntimeException.class, () -> service.updateVideo(10, 5, req));
    }

    @Test
    void testDeleteVideoSuccess() {
        Recipes recipe = new Recipes();
        recipe.setId(10);

        RecipeVideos video = new RecipeVideos();
        video.setId(5);
        video.setRecipe(recipe);

        when(videosRepo.findById(5)).thenReturn(Optional.of(video));

        service.deleteVideo(10, 5);

        verify(videosRepo, times(1)).delete(video);
    }

    @Test
    void testDeleteVideoNotFound() {
        when(videosRepo.findById(5)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.deleteVideo(10, 5));
    }

    @Test
    void testDeleteVideoWrongRecipe() {
        Recipes recipe1 = new Recipes();
        recipe1.setId(10);

        Recipes recipe2 = new Recipes();
        recipe2.setId(20);

        RecipeVideos video = new RecipeVideos();
        video.setId(5);
        video.setRecipe(recipe2);

        when(videosRepo.findById(5)).thenReturn(Optional.of(video));

        assertThrows(RuntimeException.class, () -> service.deleteVideo(10, 5));
    }
}
