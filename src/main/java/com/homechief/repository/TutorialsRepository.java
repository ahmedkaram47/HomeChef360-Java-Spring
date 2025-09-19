package com.homechief.repository;

import com.homechief.model.Tutorials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TutorialsRepository extends JpaRepository<Tutorials, Integer> {
}
