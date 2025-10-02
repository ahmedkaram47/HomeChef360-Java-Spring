package com.homechief.service;

import com.homechief.dto.RecipeVideoDTO;
import com.homechief.dto.RecipeVideoRequestDTO;
import com.homechief.model.RecipeVideos;
import com.homechief.model.Recipes;
import com.homechief.repository.RecipeVideoRepository;
import com.homechief.repository.RecipesRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeVideoService {

    private final RecipeVideoRepository videosRepo;
    private final RecipesRepository recipesRepo;

    public RecipeVideoService(RecipeVideoRepository videosRepo, RecipesRepository recipesRepo) {
        this.videosRepo = videosRepo;
        this.recipesRepo = recipesRepo;
    }

    public List<RecipeVideoDTO> getVideosForRecipe(Integer recipeId) {
        return videosRepo.findByRecipeId(recipeId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public RecipeVideoDTO addVideo(Integer recipeId, RecipeVideoRequestDTO req) {
        Recipes recipe = recipesRepo.findById(recipeId).orElseThrow(() -> new RuntimeException("Recipe not found"));
        RecipeVideos v = new RecipeVideos();
        v.setRecipe(recipe);
        v.setVideoUrl(req.getVideoUrl());
        v.setCaption(req.getCaption());
        RecipeVideos saved = videosRepo.save(v);
        return toDTO(saved);
    }

    public RecipeVideoDTO updateVideo(Integer recipeId, Integer videoId, RecipeVideoRequestDTO req) {
        RecipeVideos v = videosRepo.findById(videoId).orElseThrow(() -> new RuntimeException("Video not found"));
        if (!v.getRecipe().getId().equals(recipeId)) throw new RuntimeException("Video does not belong to recipe");
        v.setVideoUrl(req.getVideoUrl());
        v.setCaption(req.getCaption());
        return toDTO(videosRepo.save(v));
    }

    public void deleteVideo(Integer recipeId, Integer videoId) {
        RecipeVideos v = videosRepo.findById(videoId).orElseThrow(() -> new RuntimeException("Video not found"));
        if (!v.getRecipe().getId().equals(recipeId)) throw new RuntimeException("Video does not belong to recipe");
        videosRepo.delete(v);
    }

    private RecipeVideoDTO toDTO(RecipeVideos v) {
        RecipeVideoDTO d = new RecipeVideoDTO();
        d.setId(v.getId());
        d.setVideoUrl(v.getVideoUrl());
        d.setCaption(v.getCaption());
        return d;
    }
}
