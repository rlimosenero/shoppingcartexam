package com.example.demo.domain.item;

import com.example.demo.customexception.ItemSaveException;
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
@RequestMapping("/api/v1")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @Operation(summary = "Add a new item to the inventory")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Item created successfully",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Item.class))),
            @ApiResponse(responseCode = "403",
                    description = "Forbidden",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/items")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Item> createItem(
            @Parameter(description = "Add an item to inventory", required = true)
            @RequestBody Item item) {
            try {
                Item savedItem = itemService.saveItem(item);
                return new ResponseEntity<>(savedItem, HttpStatus.CREATED);
            } catch (ItemSaveException ex) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

    }


    @Operation(summary = "Get Item by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Item.class))),
            @ApiResponse(responseCode = "403",
                    description = "Forbidden",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Item> getItem(@PathVariable Long id) {
        return itemService.getItemById(id);
    }


}
