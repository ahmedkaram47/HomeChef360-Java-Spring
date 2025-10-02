package com.homechief.controller;

import com.homechief.dto.TutorialDTO;
import com.homechief.dto.TutorialRequestDTO;
import com.homechief.service.TutorialService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tutorials")
public class TutorialsController {

    private final TutorialService service;

    public TutorialsController(TutorialService service) {
        this.service = service;
    }

    // public list of published tutorials
    @GetMapping
    public ResponseEntity<List<TutorialDTO>> listPublished() {
        return ResponseEntity.ok(service.listPublished());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TutorialDTO> getById(@PathVariable Integer id) {
        TutorialDTO dto = service.getById(id);
        if (dto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    }

    // create (auth required)
    @PostMapping("/add")
    public ResponseEntity<TutorialDTO> create(@AuthenticationPrincipal UserDetails ud,
                                              @RequestBody TutorialRequestDTO req) {
        return ResponseEntity.ok(service.create(req));
    }

    // update (auth required)
    @PutMapping("/{id}")
    public ResponseEntity<TutorialDTO> update(@AuthenticationPrincipal UserDetails ud,
                                              @PathVariable Integer id,
                                              @RequestBody TutorialRequestDTO req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    // delete (auth required)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@AuthenticationPrincipal UserDetails ud,
                                         @PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.ok("Tutorial deleted");
    }

    @GetMapping("/search")
    public ResponseEntity<List<TutorialDTO>> search(@RequestParam String q) {
        return ResponseEntity.ok(service.search(q));
    }
}
