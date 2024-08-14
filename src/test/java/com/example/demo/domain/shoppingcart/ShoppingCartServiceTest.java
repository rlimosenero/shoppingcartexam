package com.example.demo.domain.shoppingcart;

import com.example.demo.domain.item.Item;
import com.example.demo.domain.item.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class ShoppingCartServiceTest {

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ShoppingCartService shoppingCartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateShoppingCart() {
        ShoppingCart newCart = new ShoppingCart();
        when(shoppingCartRepository.save(any(ShoppingCart.class))).thenReturn(newCart);

        ShoppingCart cart = shoppingCartService.createShoppingCart();

        assertNotNull(cart);
    }

    @Test
    void testAddItemToCartItemExists() {
        ShoppingCart cart = new ShoppingCart();
        Item item = new Item();
        item.setId(1L);
        item.setPrice(10.0);
        item.setQuantity(2);

        cart.setItems(new ArrayList<>());
        cart.getItems().add(item);

        when(shoppingCartRepository.findById(anyLong())).thenReturn(Optional.of(cart));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(shoppingCartRepository.save(any(ShoppingCart.class))).thenReturn(cart);

        ShoppingCart updatedCart = shoppingCartService.addItemToCart(1L, 1L, 3);

        assertEquals(1, updatedCart.getItems().size());
        assertEquals(5, updatedCart.getItems().get(0).getQuantity());
    }

    @Test
    void testAddItemToCartItemDoesNotExist() {
        ShoppingCart cart = new ShoppingCart();
        cart.setItems(new ArrayList<>());

        Item item = new Item();
        item.setId(1L);
        item.setName("Test Item");
        item.setPrice(10.0);
        item.setQuantity(2);

        when(shoppingCartRepository.findById(anyLong())).thenReturn(Optional.of(cart));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(shoppingCartRepository.save(any(ShoppingCart.class))).thenReturn(cart);

        ShoppingCart updatedCart = shoppingCartService.addItemToCart(1L, 1L, 3);

        assertEquals(1, updatedCart.getItems().size());
        assertEquals(3, updatedCart.getItems().get(0).getQuantity());
    }

    @Test
    void testAddItemToCartItemNotFound() {
        ShoppingCart cart = new ShoppingCart();
        cart.setItems(new ArrayList<>());

        when(shoppingCartRepository.findById(anyLong())).thenReturn(Optional.of(cart));
        when(itemRepository.findById(anyLong())).thenThrow(new RuntimeException("Item not found"));

        assertThrows(RuntimeException.class, () -> shoppingCartService.addItemToCart(1L, 1L, 3));
    }

    @Test
    void testGetShoppingCart() {
        ShoppingCart cart = new ShoppingCart();
        when(shoppingCartRepository.findById(anyLong())).thenReturn(Optional.of(cart));

        ShoppingCart retrievedCart = shoppingCartService.getShoppingCart(1L);

        assertNotNull(retrievedCart);
    }

    @Test
    void testGetShoppingCartNotFound() {
        when(shoppingCartRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> shoppingCartService.getShoppingCart(1L));
    }

    @Test
    void testGetItemsInCart() {
        ShoppingCart cart = new ShoppingCart();
        List<Item> items = new ArrayList<>();
        Item item = new Item();
        item.setName("Test Item");
        items.add(item);
        cart.setItems(items);

        when(shoppingCartRepository.findById(anyLong())).thenReturn(Optional.of(cart));

        List<Item> retrievedItems = shoppingCartService.getItemsInCart(1L);

        assertEquals(1, retrievedItems.size());
        assertEquals("Test Item", retrievedItems.get(0).getName());
    }
}
