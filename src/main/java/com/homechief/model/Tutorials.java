package com.homechief.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Tutorials")
public class Tutorials {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "Title", nullable = false)
    private String title;

    @Column(name = "Description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "ThumbnailUrl")
    private String thumbnailUrl;

    @Column(name = "VideoUrl")
    private String videoUrl;

    @Column(name = "Duration")
    private Integer duration;

    @Column(name = "Difficulty")
    private String difficulty;

    @Column(name = "InstructorName")
    private String instructorName;

    @Column(name = "Rating")
    private Double rating = 0.0;

    @Column(name = "ViewCount")
    private Integer viewCount = 0;

    @Column(name = "Steps", columnDefinition = "json")
    private String steps; // store JSON as String

    @Column(name = "Tags", columnDefinition = "json")
    private String tags; // store JSON as String

    @Column(name = "IsPublished")
    private Boolean isPublished = true;

    @Column(name = "CreatedAt", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "UpdatedAt", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    public Tutorials() {}

    // Getters & setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getThumbnailUrl() { return thumbnailUrl; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }

    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }

    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public String getInstructorName() { return instructorName; }
    public void setInstructorName(String instructorName) { this.instructorName = instructorName; }

    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }

    public Integer getViewCount() { return viewCount; }
    public void setViewCount(Integer viewCount) { this.viewCount = viewCount; }

    public String getSteps() { return steps; }
    public void setSteps(String steps) { this.steps = steps; }

    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }

    public Boolean getIsPublished() { return isPublished; }
    public void setIsPublished(Boolean isPublished) { this.isPublished = isPublished; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
