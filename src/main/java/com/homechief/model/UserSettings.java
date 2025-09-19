package com.homechief.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "usersettings")
public class UserSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "UserID", referencedColumnName = "ID", nullable = false)
    private User user;

    @Column(name = "Language")
    private String language = "en";

    @Column(name = "Theme")
    private String theme = "light";

    @Column(name = "MeasurementUnits")
    private String measurementUnits = "metric";

    @Column(name = "DefaultCuisine")
    private String defaultCuisine;

    @Column(name = "DefaultServings")
    private Integer defaultServings = 4;

    @Column(name = "Notifications", columnDefinition = "json")
    private String notifications; // stored as JSON string

    @Column(name = "CreatedAt", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "UpdatedAt", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    public UserSettings() {}

    // Getters & Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

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

    public String getNotifications() { return notifications; }
    public void setNotifications(String notifications) { this.notifications = notifications; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
