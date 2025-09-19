package com.homechief.repository;

import com.homechief.model.UserPreferences;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPreferencesRepository extends JpaRepository<UserPreferences, Integer> {
}
