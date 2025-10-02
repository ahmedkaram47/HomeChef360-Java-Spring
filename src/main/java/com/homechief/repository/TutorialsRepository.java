package com.homechief.repository;

import com.homechief.model.Tutorials;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TutorialsRepository extends JpaRepository<Tutorials, Integer> {
    List<Tutorials> findByTitleContainingIgnoreCase(String q);
    List<Tutorials> findByIsPublishedTrue();
}
