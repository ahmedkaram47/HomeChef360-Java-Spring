package com.homechief.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.homechief.dto.TutorialDTO;
import com.homechief.dto.TutorialRequestDTO;
import com.homechief.model.Tutorials;
import com.homechief.repository.TutorialsRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TutorialService {

    private final TutorialsRepository tutorialsRepo;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public TutorialService(TutorialsRepository tutorialsRepo) {
        this.tutorialsRepo = tutorialsRepo;
    }

    public List<TutorialDTO> listPublished() {
        return tutorialsRepo.findByIsPublishedTrue().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<TutorialDTO> search(String q) {
        return tutorialsRepo.findByTitleContainingIgnoreCase(q).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public TutorialDTO getById(Integer id) {
        return tutorialsRepo.findById(id).map(this::toDTO).orElse(null);
    }

    public TutorialDTO create(TutorialRequestDTO req) {
        Tutorials t = new Tutorials();
        t.setTitle(req.getTitle());
        t.setDescription(req.getDescription());
        t.setThumbnailUrl(req.getThumbnailUrl());
        t.setVideoUrl(req.getVideoUrl());
        t.setDuration(req.getDuration());
        t.setDifficulty(req.getDifficulty());
        t.setInstructorName(req.getInstructorName());
        t.setIsPublished(req.getIsPublished() != null ? req.getIsPublished() : false);
        try {
            t.setStepsJson(req.getSteps() != null ? objectMapper.writeValueAsString(req.getSteps()) : objectMapper.writeValueAsString(new ArrayList<>()));
            t.setTagsJson(req.getTags() != null ? objectMapper.writeValueAsString(req.getTags()) : objectMapper.writeValueAsString(new ArrayList<>()));
        } catch (Exception e) {
            throw new RuntimeException("Error serializing tutorial JSON fields", e);
        }
        Tutorials saved = tutorialsRepo.save(t);
        return toDTO(saved);
    }

    public TutorialDTO update(Integer id, TutorialRequestDTO req) {
        Tutorials t = tutorialsRepo.findById(id).orElseThrow(() -> new RuntimeException("Tutorial not found"));
        t.setTitle(req.getTitle());
        t.setDescription(req.getDescription());
        t.setThumbnailUrl(req.getThumbnailUrl());
        t.setVideoUrl(req.getVideoUrl());
        t.setDuration(req.getDuration());
        t.setDifficulty(req.getDifficulty());
        t.setInstructorName(req.getInstructorName());
        t.setIsPublished(req.getIsPublished() != null ? req.getIsPublished() : t.getIsPublished());
        try {
            if (req.getSteps() != null) t.setStepsJson(objectMapper.writeValueAsString(req.getSteps()));
            if (req.getTags() != null) t.setTagsJson(objectMapper.writeValueAsString(req.getTags()));
        } catch (Exception e) {
            throw new RuntimeException("Error serializing tutorial JSON fields", e);
        }
        Tutorials saved = tutorialsRepo.save(t);
        return toDTO(saved);
    }

    public void delete(Integer id) {
        Tutorials t = tutorialsRepo.findById(id).orElseThrow(() -> new RuntimeException("Tutorial not found"));
        tutorialsRepo.delete(t);
    }

    private TutorialDTO toDTO(Tutorials t) {
        TutorialDTO d = new TutorialDTO();
        d.setId(t.getId());
        d.setTitle(t.getTitle());
        d.setDescription(t.getDescription());
        d.setThumbnailUrl(t.getThumbnailUrl());
        d.setVideoUrl(t.getVideoUrl());
        d.setDuration(t.getDuration());
        d.setDifficulty(t.getDifficulty());
        d.setInstructorName(t.getInstructorName());
        d.setRating(t.getRating());
        d.setViewCount(t.getViewCount());
        d.setIsPublished(t.getIsPublished());
        try {
            d.setSteps(t.getStepsJson() != null ? objectMapper.readValue(t.getStepsJson(), new TypeReference<List<String>>() {}) : new ArrayList<>());
            d.setTags(t.getTagsJson() != null ? objectMapper.readValue(t.getTagsJson(), new TypeReference<List<String>>() {}) : new ArrayList<>());
        } catch (Exception e) {
            d.setSteps(new ArrayList<>());
            d.setTags(new ArrayList<>());
        }
        d.setCreatedAt(t.getCreatedAt());
        d.setUpdatedAt(t.getUpdatedAt());
        return d;
    }
}
