package solo.project.inventorymanagementsystem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import solo.project.inventorymanagementsystem.models.dto.ItemDTO;
import solo.project.inventorymanagementsystem.models.entitites.Item;
import solo.project.inventorymanagementsystem.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/all_items")
    public List<Item> getAllItems() {
        return itemService.getAllItems();
    }

    @GetMapping
    public ResponseEntity<Item> getItemById(@PathVariable Long itemId) {
        Item item = itemService.getItem(itemId);
        if (item != null) {
            return ResponseEntity.ok(item);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/create")
    public String createItem(@RequestBody ItemDTO itemDTO) {
        return itemService.createItem(itemDTO);
    }

    @PutMapping("/update/{id}")
    public String updateItem(@PathVariable Long id, @RequestBody ItemDTO itemDTO) {
        return itemService.updateItem(id, itemDTO);
    }

    @DeleteMapping("/{id}")
    public String deleteItem(@PathVariable Long itemId) {
        return itemService.deleteItem(itemId);
    }
}