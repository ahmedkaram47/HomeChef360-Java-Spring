package com.homechief.dto;

public class RecipeVideoRequestDTO {
    private String videoUrl;
    private String caption;

    public RecipeVideoRequestDTO() {}

    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }

    public String getCaption() { return caption; }
    public void setCaption(String caption) { this.caption = caption; }
}
