package com.homechief.dto;

import java.time.LocalDateTime;

public class UserSettingsDTO {
    private Integer id;
    private String language;
    private String theme;
    private String measurementUnits;
    private String defaultCuisine;
    private Integer defaultServings;

    // Change this:
    // private String notifications;
    // To this:
    private Object notifications;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Getters and setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }

    public String getMeasurementUnits() { return measurementUnits; }
    public void setMeasurementUnits(String measurementUnits) { this.measurementUnits = measurementUnits; }

    public String getDefaultCuisine() { return defaultCuisine; }
    public void setDefaultCuisine(String defaultCuisine) { this.defaultCuisine = defaultCuisine; }

    public Integer getDefaultServings() { return defaultServings; }
    public void setDefaultServings(Integer defaultServings) { this.defaultServings = defaultServings; }

    public Object getNotifications() { return notifications; }
    public void setNotifications(Object notifications) { this.notifications = notifications; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
