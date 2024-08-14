package com.example.demo.domain.item;

import com.example.demo.customexception.ItemSaveException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class ItemControllerTest {

    @Mock
    private ItemService itemService;

    @InjectMocks
    private ItemController itemController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(itemController).build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateItemSuccess() throws Exception {
        Item item = new Item();
        item.setName("Test Item");
        item.setPrice(10.0);
        item.setQuantity(5);

        when(itemService.saveItem(any(Item.class))).thenReturn(item);

        mockMvc.perform(post("/api/v1/items")
                        .contentType("application/json")
                        .content("{\"name\": \"Test Item\", \"price\": 10.0, \"quantity\": 5}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Item"))
                .andExpect(jsonPath("$.price").value(10.0))
                .andExpect(jsonPath("$.quantity").value(5));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateItemThrowsException() throws Exception {
        when(itemService.saveItem(any(Item.class))).thenThrow(new ItemSaveException("Failed to save item"));

        mockMvc.perform(post("/api/v1/items")
                        .contentType("application/json")
                        .content("{\"name\": \"Test Item\", \"price\": 10.0, \"quantity\": 5}"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testGetItemSuccess() throws Exception {
        Item item = new Item();
        item.setId(1L);
        item.setName("Test Item");
        item.setPrice(10.0);
        item.setQuantity(5);

        when(itemService.getItemById(anyLong())).thenReturn(ResponseEntity.ok(item));

        mockMvc.perform(get("/api/v1/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Item"))
                .andExpect(jsonPath("$.price").value(10.0))
                .andExpect(jsonPath("$.quantity").value(5));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testGetItemNotFound() throws Exception {
        when(itemService.getItemById(anyLong())).thenReturn(ResponseEntity.notFound().build());

        mockMvc.perform(get("/api/v1/1"))
                .andExpect(status().isNotFound());
    }
}
