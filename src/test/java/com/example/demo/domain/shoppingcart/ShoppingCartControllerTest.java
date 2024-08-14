package com.example.demo.domain.shoppingcart;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ShoppingCartControllerTest {

    @Mock
    private ShoppingCartService shoppingCartService;

    @InjectMocks
    private ShoppingCartController shoppingCartController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(shoppingCartController).build();
    }

    @Test
    @WithMockUser(roles = "USER")
    void testCreateItemSuccess() throws Exception {
        ShoppingCart cart = new ShoppingCart();
        cart.setId(1L);
        cart.setTotalPrice(10.0);

        when(shoppingCartService.addItemToCart(anyLong(), anyLong(), anyInt())).thenReturn(cart);

        mockMvc.perform(post("/api/v1/carts/items/1")
                        .param("itemId", "2")
                        .param("quantity", "3"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.totalPrice").value(10.0));
    }

    @Test
    @WithMockUser(roles = "ROLE_USER")
    void testCreateItemFails() throws Exception {
        when(shoppingCartService.addItemToCart(anyLong(), anyLong(), anyInt())).thenThrow(new RuntimeException("Error adding item"));

        mockMvc.perform(post("/api/v1/carts/items/1")
                        .param("itemId", "2")
                        .param("quantity", "3"))
                .andExpect(status().isInternalServerError());
    }
}
