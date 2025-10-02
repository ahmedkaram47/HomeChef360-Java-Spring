package com.homechief.controller;

import com.homechief.dto.NutritionTrackingDTO;
import com.homechief.dto.NutritionTrackingRequestDTO;
import com.homechief.model.User;
import com.homechief.repository.UserRepository;
import com.homechief.service.NutritionTrackingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/nutrition")
public class NutritionTrackingController {

    private final NutritionTrackingService service;
    private final UserRepository userRepo;

    public NutritionTrackingController(NutritionTrackingService service, UserRepository userRepo) {
        this.service = service;
        this.userRepo = userRepo;
    }

    @GetMapping
    public ResponseEntity<List<NutritionTrackingDTO>> getUserNutrition(@AuthenticationPrincipal UserDetails ud) {
        User u = userRepo.findByEmail(ud.getUsername()).orElseThrow();
        return ResponseEntity.ok(service.getAllForUser(u.getId()));
    }

    @PostMapping
    public ResponseEntity<NutritionTrackingDTO> addNutrition(@AuthenticationPrincipal UserDetails ud,
                                                             @RequestBody NutritionTrackingRequestDTO req) {
        User u = userRepo.findByEmail(ud.getUsername()).orElseThrow();
        return ResponseEntity.ok(service.add(u.getId(), req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNutrition(@AuthenticationPrincipal UserDetails ud,
                                                  @PathVariable Integer id) {
        User u = userRepo.findByEmail(ud.getUsername()).orElseThrow();
        service.delete(u.getId(), id);
        return ResponseEntity.ok("Deleted");
    }
}
