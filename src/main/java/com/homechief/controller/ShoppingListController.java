package com.homechief.controller;

import com.homechief.dto.*;
import com.homechief.model.User;
import com.homechief.repository.UserRepository;
import com.homechief.service.ShoppingListService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shoppinglists")
public class ShoppingListController {

    private final ShoppingListService service;
    private final UserRepository userRepo;

    public ShoppingListController(ShoppingListService service, UserRepository userRepo) {
        this.service = service;
        this.userRepo = userRepo;
    }

    @GetMapping("/my")
    public ResponseEntity<List<ShoppingListDTO>> getMyLists(@AuthenticationPrincipal UserDetails ud) {
        User u = userRepo.findByEmail(ud.getUsername()).orElseThrow();
        return ResponseEntity.ok(service.getUserLists(u.getId()));
    }

    @PostMapping("/add")
    public ResponseEntity<ShoppingListDTO> createList(@AuthenticationPrincipal UserDetails ud,
                                                      @RequestBody ShoppingListRequestDTO req) {
        User u = userRepo.findByEmail(ud.getUsername()).orElseThrow();
        return ResponseEntity.ok(service.createList(u.getId(), req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShoppingListDTO> updateList(@AuthenticationPrincipal UserDetails ud,
                                                      @PathVariable Integer id,
                                                      @RequestBody ShoppingListRequestDTO req) {
        User u = userRepo.findByEmail(ud.getUsername()).orElseThrow();
        return ResponseEntity.ok(service.updateList(id, u.getId(), req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteList(@AuthenticationPrincipal UserDetails ud,
                                             @PathVariable Integer id) {
        User u = userRepo.findByEmail(ud.getUsername()).orElseThrow();
        service.deleteList(id, u.getId());
        return ResponseEntity.ok("Shopping list deleted");
    }
}
