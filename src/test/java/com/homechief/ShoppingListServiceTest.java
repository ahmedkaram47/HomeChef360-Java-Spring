package com.homechief;

import com.homechief.dto.ShoppingListDTO;
import com.homechief.dto.ShoppingListRequestDTO;
import com.homechief.model.*;
import com.homechief.repository.*;
import com.homechief.service.ShoppingListService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShoppingListServiceTest {

    @Mock
    private ShoppingListRepository listRepo;

    @Mock
    private UserRepository userRepo;

    @Mock
    private IngredientsRepository ingredientsRepo;

    @Mock
    private RecipesRepository recipesRepo;

    @InjectMocks
    private ShoppingListService shoppingListService;

    private User user;
    private ShoppingList shoppingList;
    private ShoppingListRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1);
        user.setEmail("test@test.com");

        shoppingList = new ShoppingList();
        shoppingList.setId(1);
        shoppingList.setName("Milk");
        shoppingList.setQuantity("2");
        shoppingList.setStatus("pending");
        shoppingList.setUnit("liters");
        shoppingList.setUser(user);

        requestDTO = new ShoppingListRequestDTO();
        requestDTO.setName("Bread");
        requestDTO.setQuantity("3");
        requestDTO.setStatus("done");
        requestDTO.setUnit("pieces");
    }

    @Test
    void testGetUserLists() {
        when(listRepo.findByUserId(1)).thenReturn(Collections.singletonList(shoppingList));

        List<ShoppingListDTO> result = shoppingListService.getUserLists(1);

        assertEquals(1, result.size());
        assertEquals("Milk", result.get(0).getName());
        verify(listRepo, times(1)).findByUserId(1);
    }

    @Test
    void testCreateList() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(listRepo.save(any(ShoppingList.class))).thenAnswer(i -> {
            ShoppingList saved = i.getArgument(0);
            saved.setId(10);
            return saved;
        });

        ShoppingListDTO dto = shoppingListService.createList(1, requestDTO);

        assertNotNull(dto.getId());
        assertEquals("Bread", dto.getName());
        assertEquals("3", dto.getQuantity());

        assertEquals("done", dto.getStatus());
        assertEquals("pieces", dto.getUnit());
        verify(listRepo, times(1)).save(any(ShoppingList.class));
    }

    @Test
    void testUpdateList_Authorized() {
        when(listRepo.findById(1)).thenReturn(Optional.of(shoppingList));
        when(listRepo.save(any(ShoppingList.class))).thenReturn(shoppingList);

        ShoppingListDTO dto = shoppingListService.updateList(1, 1, requestDTO);

        assertEquals("Bread", dto.getName());
        assertEquals("done", dto.getStatus());
        verify(listRepo, times(1)).save(shoppingList);
    }

    @Test
    void testUpdateList_Unauthorized() {
        when(listRepo.findById(1)).thenReturn(Optional.of(shoppingList));

        Exception ex = assertThrows(RuntimeException.class, () ->
                shoppingListService.updateList(1, 99, requestDTO));

        assertEquals("Unauthorized", ex.getMessage());
    }

    @Test
    void testDeleteList_Authorized() {
        when(listRepo.findById(1)).thenReturn(Optional.of(shoppingList));

        shoppingListService.deleteList(1, 1);

        verify(listRepo, times(1)).delete(shoppingList);
    }

    @Test
    void testDeleteList_Unauthorized() {
        when(listRepo.findById(1)).thenReturn(Optional.of(shoppingList));

        Exception ex = assertThrows(RuntimeException.class, () ->
                shoppingListService.deleteList(1, 99));

        assertEquals("Unauthorized", ex.getMessage());
        verify(listRepo, never()).delete(any());
    }
}
