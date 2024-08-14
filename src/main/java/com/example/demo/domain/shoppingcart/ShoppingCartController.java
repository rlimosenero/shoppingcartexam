package com.example.demo.domain.shoppingcart;

import com.example.demo.domain.item.Item;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/carts")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @Operation(summary = "Add an item to cart")

    @ApiResponses(value = {

            @ApiResponse(responseCode = "201",
                    description = "Item created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Item.class))),

            @ApiResponse(responseCode = "403",
                    description = "Forbidden",
                    content = @Content(mediaType = "application/json"))

    })
    @PostMapping("/items/{cartId}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<ShoppingCart> createItem(
            @Parameter(description = "Add an item to cart", required = true)
            @RequestParam Long itemId,
            @PathVariable Long cartId,
            @RequestParam int quantity) {
        try{
            ShoppingCart savedItem = shoppingCartService.addItemToCart(cartId,itemId,quantity);
            return  ResponseEntity.status(HttpStatus.CREATED).body(savedItem);
        }catch (RuntimeException ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

}
