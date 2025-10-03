package com.homechief;

import com.homechief.dto.NutritionTrackingDTO;
import com.homechief.dto.NutritionTrackingRequestDTO;
import com.homechief.model.NutritionTracking;
import com.homechief.model.User;
import com.homechief.repository.NutritionTrackingRepository;
import com.homechief.repository.UserRepository;
import com.homechief.service.NutritionTrackingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NutritionTrackingServiceTest {

    @Mock
    private NutritionTrackingRepository repo;

    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private NutritionTrackingService service;

    private User user;
    private NutritionTracking tracking;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1);

        tracking = new NutritionTracking();
        tracking.setId(10);
        tracking.setUser(user);
        tracking.setDate(LocalDate.now());
        tracking.setCalories(2000);
        tracking.setProtein(100);
        tracking.setCarbs(250);
        tracking.setFat(70);
        tracking.setFiber(30);
        tracking.setNotes("Healthy day");
    }

    @Test
    void testGetAllForUser() {
        when(repo.findByUserId(1)).thenReturn(Arrays.asList(tracking));

        List<NutritionTrackingDTO> list = service.getAllForUser(1);

        assertEquals(1, list.size());
        assertEquals(2000, list.get(0).getCalories());
        verify(repo, times(1)).findByUserId(1);
    }

    @Test
    void testAdd() {
        NutritionTrackingRequestDTO req = new NutritionTrackingRequestDTO();
        req.setDate(LocalDate.now());
        req.setCalories(1800);
        req.setProtein(90);
        req.setCarbs(220);
        req.setFat(60);
        req.setFiber(25);
        req.setNotes("Test day");

        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(repo.save(any(NutritionTracking.class))).thenAnswer(i -> {
            NutritionTracking saved = i.getArgument(0);
            saved.setId(11);
            return saved;
        });

        NutritionTrackingDTO dto = service.add(1, req);

        assertNotNull(dto.getId());
        assertEquals(1800, dto.getCalories());
        assertEquals("Test day", dto.getNotes());
        verify(repo, times(1)).save(any(NutritionTracking.class));
    }

    @Test
    void testDelete_Success() {
        when(repo.findById(10)).thenReturn(Optional.of(tracking));

        service.delete(1, 10);

        verify(repo, times(1)).delete(tracking);
    }

    @Test
    void testDelete_NotAllowed() {
        User anotherUser = new User();
        anotherUser.setId(2);
        tracking.setUser(anotherUser);

        when(repo.findById(10)).thenReturn(Optional.of(tracking));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.delete(1, 10));

        assertEquals("Not allowed", ex.getMessage());
        verify(repo, never()).delete(any());
    }
}
