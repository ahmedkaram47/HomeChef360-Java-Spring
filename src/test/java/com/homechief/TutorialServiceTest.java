package com.homechief;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homechief.dto.TutorialDTO;
import com.homechief.dto.TutorialRequestDTO;
import com.homechief.model.Tutorials;
import com.homechief.repository.TutorialsRepository;
import com.homechief.service.TutorialService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TutorialServiceTest {

    private TutorialsRepository tutorialsRepo;
    private TutorialService service;

    @BeforeEach
    void setUp() {
        tutorialsRepo = mock(TutorialsRepository.class);
        service = new TutorialService(tutorialsRepo);
    }

    @Test
    void testListPublished() {
        Tutorials t = new Tutorials();
        t.setId(1);
        t.setTitle("Java Tutorial");
        t.setIsPublished(true);

        when(tutorialsRepo.findByIsPublishedTrue()).thenReturn(List.of(t));

        List<TutorialDTO> result = service.listPublished();

        assertEquals(1, result.size());
        assertEquals("Java Tutorial", result.get(0).getTitle());
        assertTrue(result.get(0).getIsPublished());
    }

    @Test
    void testSearch() {
        Tutorials t = new Tutorials();
        t.setId(1);
        t.setTitle("Spring Boot Guide");

        when(tutorialsRepo.findByTitleContainingIgnoreCase("spring"))
                .thenReturn(List.of(t));

        List<TutorialDTO> result = service.search("spring");

        assertEquals(1, result.size());
        assertEquals("Spring Boot Guide", result.get(0).getTitle());
    }

    @Test
    void testGetByIdFound() {
        Tutorials t = new Tutorials();
        t.setId(2);
        t.setTitle("Docker Basics");

        when(tutorialsRepo.findById(2)).thenReturn(Optional.of(t));

        TutorialDTO dto = service.getById(2);

        assertNotNull(dto);
        assertEquals("Docker Basics", dto.getTitle());
    }

    @Test
    void testGetByIdNotFound() {
        when(tutorialsRepo.findById(99)).thenReturn(Optional.empty());

        TutorialDTO dto = service.getById(99);

        assertNull(dto);
    }

    @Test
    void testCreateTutorial() {
        TutorialRequestDTO req = new TutorialRequestDTO();
        req.setTitle("Kubernetes Intro");
        req.setDescription("Learn k8s");
        req.setThumbnailUrl("thumb.png");
        req.setVideoUrl("video.mp4");
        req.setDuration(5);
        req.setDifficulty("Beginner");
        req.setInstructorName("Alia");
        req.setIsPublished(true);
        req.setSteps(List.of("step1", "step2"));
        req.setTags(List.of("devops", "cloud"));

        Tutorials saved = new Tutorials();
        saved.setId(10);
        saved.setTitle(req.getTitle());
        saved.setDescription(req.getDescription());
        saved.setIsPublished(true);

        when(tutorialsRepo.save(any(Tutorials.class))).thenReturn(saved);

        TutorialDTO result = service.create(req);

        assertEquals(10, result.getId());
        assertEquals("Kubernetes Intro", result.getTitle());
        assertTrue(result.getIsPublished());
    }

    @Test
    void testUpdateTutorialSuccess() {
        Tutorials existing = new Tutorials();
        existing.setId(5);
        existing.setTitle("Old Title");
        existing.setIsPublished(false);

        when(tutorialsRepo.findById(5)).thenReturn(Optional.of(existing));
        when(tutorialsRepo.save(any(Tutorials.class))).thenAnswer(i -> i.getArgument(0));

        TutorialRequestDTO req = new TutorialRequestDTO();
        req.setTitle("New Title");
        req.setDescription("Updated description");
        req.setThumbnailUrl("new.png");
        req.setVideoUrl("new.mp4");
        req.setDuration(30);
        req.setDifficulty("Advanced");
        req.setInstructorName("Alia");
        req.setIsPublished(true);
        req.setSteps(List.of("step1", "step2"));
        req.setTags(List.of("java", "spring"));

        TutorialDTO dto = service.update(5, req);

        assertEquals("New Title", dto.getTitle());
        assertEquals("Updated description", dto.getDescription());
        assertTrue(dto.getIsPublished());
        assertEquals(List.of("step1", "step2"), dto.getSteps());
    }

    @Test
    void testUpdateTutorialNotFound() {
        when(tutorialsRepo.findById(5)).thenReturn(Optional.empty());

        TutorialRequestDTO req = new TutorialRequestDTO();
        req.setTitle("Should Fail");

        assertThrows(RuntimeException.class, () -> service.update(5, req));
    }

    @Test
    void testDeleteTutorialSuccess() {
        Tutorials t = new Tutorials();
        t.setId(7);

        when(tutorialsRepo.findById(7)).thenReturn(Optional.of(t));

        service.delete(7);

        verify(tutorialsRepo, times(1)).delete(t);
    }

    @Test
    void testDeleteTutorialNotFound() {
        when(tutorialsRepo.findById(8)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.delete(8));
    }
}
