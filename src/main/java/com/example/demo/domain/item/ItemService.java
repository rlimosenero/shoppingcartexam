package com.example.demo.domain.item;

import com.example.demo.customexception.ItemSaveException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Slf4j
public class ItemService {

    private final ItemRepository itemRepository;
    @Autowired
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }


    @Transactional
    public Item saveItem(Item item) {
        validateItem(item);

        try {
            Item savedItem = itemRepository.save(item);
            log.info("Item saved successfully: " + savedItem);
            return savedItem;
        } catch (Exception e) {
            log.error("Error occurred while saving item", e);
            throw new ItemSaveException("Failed to save item", e);
        }
    }

    private void validateItem(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }

        if (item.getName() == null || item.getName().isEmpty()) {
            throw new IllegalArgumentException("Item name is required");
        }

    }

    public ResponseEntity<Item> getItemById(Long id) {
        log.info("Fetching item with id: {}", id);

        try {
            Optional<Item> item = itemRepository.findById(id);
            if (item.isPresent()) {
                return ResponseEntity.ok(item.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error fetching item: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
