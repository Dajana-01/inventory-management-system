package solo.project.inventorymanagementsystem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import solo.project.inventorymanagementsystem.exception.ItemAvailabilityException;
import solo.project.inventorymanagementsystem.models.dto.InventoryItemDTO;
import solo.project.inventorymanagementsystem.service.InventoryItemService;

@RestController
@RequestMapping("/api/v1/inventory_item")
@RequiredArgsConstructor
public class InventoryItem {

    private final  InventoryItemService inventoryItemService;

    @PostMapping("/inventory")
    public ResponseEntity<String> createInventoryItem(@RequestBody InventoryItemDTO itemInventoryDTO) {
        try {
            inventoryItemService.save(itemInventoryDTO);
            return ResponseEntity.ok("Inventory item created successfully");
        } catch (ItemAvailabilityException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create inventory item: " + e.getMessage());
        }
    }
}
