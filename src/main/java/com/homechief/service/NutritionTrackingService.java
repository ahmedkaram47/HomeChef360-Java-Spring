package com.homechief.service;

import com.homechief.dto.NutritionTrackingDTO;
import com.homechief.dto.NutritionTrackingRequestDTO;
import com.homechief.model.NutritionTracking;
import com.homechief.model.User;
import com.homechief.repository.NutritionTrackingRepository;
import com.homechief.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NutritionTrackingService {

    private final NutritionTrackingRepository repo;
    private final UserRepository userRepo;

    public NutritionTrackingService(NutritionTrackingRepository repo, UserRepository userRepo) {
        this.repo = repo;
        this.userRepo = userRepo;
    }

    public List<NutritionTrackingDTO> getAllForUser(Integer userId) {
        return repo.findByUserId(userId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public NutritionTrackingDTO add(Integer userId, NutritionTrackingRequestDTO req) {
        User u = userRepo.findById(Long.valueOf(userId)).orElseThrow();
        NutritionTracking n = new NutritionTracking();
        n.setUser(u);
        n.setDate(req.getDate());
        n.setCalories(req.getCalories());
        n.setProtein(req.getProtein());
        n.setCarbs(req.getCarbs());
        n.setFat(req.getFat());
        n.setFiber(req.getFiber());
        n.setNotes(req.getNotes());
        return toDTO(repo.save(n));
    }

    public void delete(Integer userId, Integer id) {
        NutritionTracking n = repo.findById(id).orElseThrow();
        if (!n.getUser().getId().equals(userId)) throw new RuntimeException("Not allowed");
        repo.delete(n);
    }

    private NutritionTrackingDTO toDTO(NutritionTracking n) {
        NutritionTrackingDTO d = new NutritionTrackingDTO();
        d.setId(n.getId());
        d.setDate(n.getDate());
        d.setCalories(n.getCalories());
        d.setProtein(n.getProtein());
        d.setCarbs(n.getCarbs());
        d.setFat(n.getFat());
        d.setFiber(n.getFiber());
        d.setNotes(n.getNotes());
        return d;
    }
}
