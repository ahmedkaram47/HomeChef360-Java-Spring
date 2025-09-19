package com.homechief.model;

import jakarta.persistence.*;

@Entity
@Table(name = "RecipeVideos")
public class RecipeVideos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RecipeID", nullable = false)
    private Recipes recipe;

    @Column(name = "VideoURL", columnDefinition = "TEXT", nullable = false)
    private String videoUrl;

    @Column(name = "Caption", length = 255)
    private String caption;

    public RecipeVideos() {}

    // Getters and setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Recipes getRecipe() { return recipe; }
    public void setRecipe(Recipes recipe) { this.recipe = recipe; }

    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }

    public String getCaption() { return caption; }
    public void setCaption(String caption) { this.caption = caption; }
}
