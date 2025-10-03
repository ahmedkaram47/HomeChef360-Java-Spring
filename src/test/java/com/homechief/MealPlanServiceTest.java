package com.homechief;

import com.homechief.dto.*;
import com.homechief.model.*;
import com.homechief.repository.*;
import com.homechief.service.MealPlanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MealPlanServiceTest {

    @Mock
    private MealPlansRepository mealPlansRepository;

    @Mock
    private MealPlanItemsRepository mealPlanItemsRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RecipesRepository recipesRepository;

    @InjectMocks
    private MealPlanService service;

    private User user;
    private MealPlans mealPlan;
    private MealPlanItems item;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1);

        mealPlan = new MealPlans();
        mealPlan.setId(100);
        mealPlan.setUser(user);
        mealPlan.setName("Weekly Plan");
        mealPlan.setDescription("Healthy meals");
        mealPlan.setStartDate(LocalDate.now());
        mealPlan.setEndDate(LocalDate.now().plusDays(7));
        mealPlan.setTotalCalories(2000);
        mealPlan.setDifficulty("Easy");
        mealPlan.setCuisine("Italian");

        item = new MealPlanItems();
        item.setId(500);
        item.setMealPlan(mealPlan);
        item.setDayOfWeek("Monday");
        item.setMealType("Lunch");
        item.setCalories(600);
    }

    // ---------------- MealPlans ----------------

    @Test
    void testGetMealPlansForUser() {
        when(mealPlansRepository.findByUserId(1)).thenReturn(List.of(mealPlan));

        var list = service.getMealPlansForUser(1);

        assertEquals(1, list.size());
        assertEquals("Weekly Plan", list.get(0).getName());
        verify(mealPlansRepository, times(1)).findByUserId(1);
    }

    @Test
    void testCreateMealPlan() {
        MealPlanRequestDTO req = new MealPlanRequestDTO();
        req.setName("New Plan");
        req.setDescription("Test Plan");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(mealPlansRepository.save(any(MealPlans.class))).thenAnswer(i -> {
            MealPlans saved = i.getArgument(0);
            saved.setId(200);
            return saved;
        });

        MealPlanDTO dto = service.createMealPlan(1, req);

        assertNotNull(dto.getId());
        assertEquals("New Plan", dto.getName());
        verify(mealPlansRepository, times(1)).save(any(MealPlans.class));
    }

    @Test
    void testUpdateMealPlan_Success() {
        MealPlanRequestDTO req = new MealPlanRequestDTO();
        req.setName("Updated Plan");

        when(mealPlansRepository.findById(100)).thenReturn(Optional.of(mealPlan));
        when(mealPlansRepository.save(mealPlan)).thenReturn(mealPlan);

        MealPlanDTO dto = service.updateMealPlan(100, 1, req);

        assertEquals("Updated Plan", dto.getName());
        verify(mealPlansRepository, times(1)).save(mealPlan);
    }

    @Test
    void testUpdateMealPlan_Unauthorized() {
        mealPlan.setUser(new User() {{ setId(2); }});
        when(mealPlansRepository.findById(100)).thenReturn(Optional.of(mealPlan));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.updateMealPlan(100, 1, new MealPlanRequestDTO()));

        assertEquals("Not authorized to update this meal plan", ex.getMessage());
    }

    @Test
    void testDeleteMealPlan_Success() {
        when(mealPlansRepository.findById(100)).thenReturn(Optional.of(mealPlan));

        service.deleteMealPlan(100, 1);

        verify(mealPlansRepository, times(1)).delete(mealPlan);
    }

    @Test
    void testDeleteMealPlan_NotAuthorized() {
        mealPlan.setUser(new User() {{ setId(2); }});
        when(mealPlansRepository.findById(100)).thenReturn(Optional.of(mealPlan));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.deleteMealPlan(100, 1));

        assertEquals("Not authorized to delete this meal plan", ex.getMessage());
    }

    // ---------------- Items ----------------

    @Test
    void testGetItemsForMealPlan() {
        when(mealPlanItemsRepository.findByMealPlanId(100)).thenReturn(List.of(item));

        var items = service.getItemsForMealPlan(100);

        assertEquals(1, items.size());
        assertEquals("Monday", items.get(0).getDayOfWeek());
    }

    @Test
    void testAddItem_Success() {
        MealPlanItemRequestDTO req = new MealPlanItemRequestDTO();
        req.setDayOfWeek("Tuesday");
        req.setMealType("Dinner");

        when(mealPlansRepository.findById(100)).thenReturn(Optional.of(mealPlan));
        when(mealPlanItemsRepository.save(any(MealPlanItems.class))).thenAnswer(i -> {
            MealPlanItems saved = i.getArgument(0);
            saved.setId(600);
            return saved;
        });

        MealPlanItemDTO dto = service.addItem(100, 1, req);

        assertEquals("Tuesday", dto.getDayOfWeek());
        assertNotNull(dto.getId());
    }

    @Test
    void testUpdateItem_Success() {
        MealPlanItemRequestDTO req = new MealPlanItemRequestDTO();
        req.setDayOfWeek("Friday");
        req.setMealType("Breakfast");

        when(mealPlanItemsRepository.findById(500)).thenReturn(Optional.of(item));
        when(mealPlanItemsRepository.save(item)).thenReturn(item);

        MealPlanItemDTO dto = service.updateItem(100, 500, 1, req);

        assertEquals("Friday", dto.getDayOfWeek());
        assertEquals("Breakfast", dto.getMealType());
    }

    @Test
    void testDeleteItem_Success() {
        when(mealPlanItemsRepository.findById(500)).thenReturn(Optional.of(item));

        service.deleteItem(100, 500, 1);

        verify(mealPlanItemsRepository, times(1)).delete(item);
    }

    @Test
    void testDeleteItem_NotAuthorized() {
        mealPlan.setUser(new User() {{ setId(2); }});
        item.setMealPlan(mealPlan);

        when(mealPlanItemsRepository.findById(500)).thenReturn(Optional.of(item));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.deleteItem(100, 500, 1));

        assertEquals("Not authorized to delete this item", ex.getMessage());
    }
}
