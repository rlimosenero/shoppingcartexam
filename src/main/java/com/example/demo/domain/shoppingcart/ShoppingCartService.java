package com.example.demo.domain.shoppingcart;

import com.example.demo.domain.item.Item;
import com.example.demo.domain.item.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class  ShoppingCartService{

    private final ShoppingCartRepository shoppingCartRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, ItemRepository itemRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.itemRepository = itemRepository;
    }
    @Transactional
    public ShoppingCart createShoppingCart() {
        ShoppingCart cart = new ShoppingCart();
        return shoppingCartRepository.save(cart);
    }

    public ShoppingCart addItemToCart(Long cartId, Long itemId, int quantity) {
        // Find or create the ShoppingCart
        ShoppingCart shoppingCart = shoppingCartRepository.findById(cartId)
                .orElseGet(() -> {
                    ShoppingCart newCart = new ShoppingCart();
                    return shoppingCartRepository.save(newCart);
                });

        // Find the Item
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        // Add the item to the cart with the specified quantity
        boolean itemExists = false;
        for (Item cartItem : shoppingCart.getItems()) {
            if (cartItem.getId().equals(item.getId())) {
                // Update the existing item quantity
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
                itemExists = true;
                break;
            }
        }

        if (!itemExists) {
            // If the item does not exist in the cart, add it
            Item newItem = new Item();
            newItem.setId(item.getId());
            newItem.setName(item.getName());
            newItem.setPrice(item.getPrice());
            newItem.setQuantity(quantity);
            newItem.setDescription(item.getDescription());
            newItem.setLocation(item.getLocation());
            shoppingCart.getItems().add(newItem);
        }

        // Update the total price
        updateTotalPrice(shoppingCart);

        // Save the updated cart
        return shoppingCartRepository.save(shoppingCart);
    }


    @Transactional()
    public ShoppingCart getShoppingCart(Long cartId) {
        return shoppingCartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("ShoppingCart not found"));
    }

    @Transactional()
    public List<Item> getItemsInCart(Long cartId) {
        ShoppingCart cart = getShoppingCart(cartId);
        return cart.getItems();
    }

    public void updateTotalPrice(ShoppingCart cart) {
        double totalPrice = cart.getItems().stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();
        cart.setTotalPrice(totalPrice);
    }
}
