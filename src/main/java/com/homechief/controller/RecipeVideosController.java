package com.homechief.controller;

import com.homechief.dto.RecipeVideoDTO;
import com.homechief.dto.RecipeVideoRequestDTO;
import com.homechief.service.RecipeVideoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recipes/{recipeId}/videos")
public class RecipeVideosController {

    private final RecipeVideoService service;

    public RecipeVideosController(RecipeVideoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<RecipeVideoDTO>> getVideos(@PathVariable Integer recipeId) {
        return ResponseEntity.ok(service.getVideosForRecipe(recipeId));
    }

    @PostMapping
    public ResponseEntity<RecipeVideoDTO> addVideo(@AuthenticationPrincipal UserDetails ud,
                                                   @PathVariable Integer recipeId,
                                                   @RequestBody RecipeVideoRequestDTO req) {
        return ResponseEntity.ok(service.addVideo(recipeId, req));
    }

    @PutMapping("/{videoId}")
    public ResponseEntity<RecipeVideoDTO> updateVideo(@AuthenticationPrincipal UserDetails ud,
                                                      @PathVariable Integer recipeId,
                                                      @PathVariable Integer videoId,
                                                      @RequestBody RecipeVideoRequestDTO req) {
        return ResponseEntity.ok(service.updateVideo(recipeId, videoId, req));
    }

    @DeleteMapping("/{videoId}")
    public ResponseEntity<String> deleteVideo(@AuthenticationPrincipal UserDetails ud,
                                              @PathVariable Integer recipeId,
                                              @PathVariable Integer videoId) {
        service.deleteVideo(recipeId, videoId);
        return ResponseEntity.ok("Video deleted");
    }
}
