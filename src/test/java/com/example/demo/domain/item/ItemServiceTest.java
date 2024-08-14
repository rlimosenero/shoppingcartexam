package com.example.demo.domain.item;

import com.example.demo.customexception.ItemSaveException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveItemSuccess() {
        Item item = new Item();
        item.setName("Test Item");

        when(itemRepository.save(any(Item.class))).thenReturn(item);

        Item savedItem = itemService.saveItem(item);

        assertNotNull(savedItem);
        assertEquals("Test Item", savedItem.getName());
        verify(itemRepository, times(1)).save(item);
    }

    @Test
    void testSaveItemThrowsException() {
        Item item = new Item();
        item.setName("Test Item");

        when(itemRepository.save(any(Item.class))).thenThrow(new RuntimeException("Database error"));

        assertThrows(ItemSaveException.class, () -> itemService.saveItem(item));
    }

    @Test
    void testGetItemByIdSuccess() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Test Item");

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        ResponseEntity<Item> response = itemService.getItemById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Item", response.getBody().getName());
    }

    @Test
    void testGetItemByIdNotFound() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<Item> response = itemService.getItemById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testGetItemByIdThrowsException() {
        when(itemRepository.findById(anyLong())).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<Item> response = itemService.getItemById(1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }
}
