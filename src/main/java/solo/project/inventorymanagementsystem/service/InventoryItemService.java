package solo.project.inventorymanagementsystem.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import solo.project.inventorymanagementsystem.exception.ItemAvailabilityException;
import solo.project.inventorymanagementsystem.models.dto.InventoryItemDTO;
import solo.project.inventorymanagementsystem.models.dto.OrderDTO;
import solo.project.inventorymanagementsystem.models.entitites.Delivery;
import solo.project.inventorymanagementsystem.models.entitites.InventoryItem;
import solo.project.inventorymanagementsystem.models.entitites.Item;
import solo.project.inventorymanagementsystem.models.entitites.Order;
import solo.project.inventorymanagementsystem.models.enums.OrderStatus;
import solo.project.inventorymanagementsystem.repository.DeliveryRepository;
import solo.project.inventorymanagementsystem.repository.InventoryItemRepository;
import solo.project.inventorymanagementsystem.repository.ItemRepository;
import solo.project.inventorymanagementsystem.repository.OrderRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryItemService {

    private final InventoryItemRepository inventoryItemRepository;

    private final OrderRepository orderRepository;
    private final  DeliveryRepository deliveryRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public void updateInventory(OrderDTO orderDto) {
        try {
            for (Item orderItem : orderDto.getItem()) {
                String itemName = orderItem.getName();
                InventoryItem inventoryItem = findInventoryItemByItemName(itemName);

                if (inventoryItem != null && inventoryItem.getQuantity() >= orderItem.getRequestedQuantity()) {
                    inventoryItem.setQuantity(inventoryItem.getQuantity() - orderItem.getRequestedQuantity());
                    inventoryItemRepository.save(inventoryItem);
                } else {
                    log.error("Insufficient quantity in inventory for order item: {}", itemName);
                }
            }

            Order order = orderRepository.findById(orderDto.getId()).orElse(null);
            if (order != null) {
                order.setStatus(OrderStatus.UNDER_DELIVERY);
                orderRepository.save(order);

                log.info("Order status updated to UNDER_DELIVERY for order: {}", orderDto.getStatus());

                Delivery delivery = new Delivery();
                delivery.setOrder(order);
                deliveryRepository.save(delivery);

                // Log a success message for scheduling the delivery
                log.info("Delivery scheduled for order: {}", orderDto.getOrderNumber());
            }
        } catch (DataAccessException e) {
            log.error("Error updating inventory for order: {}", orderDto.getOrderNumber(), e);
        }
    }
    private InventoryItem findInventoryItemByItemName(String itemName) {
        Item item = itemRepository.findByName(itemName);
        return inventoryItemRepository.findByName(item);
    }
    public void save(InventoryItemDTO itemInventoryDTO) throws ItemAvailabilityException {
        String itemName = itemInventoryDTO.getName().getName();
        Item item = itemRepository.findByName(itemName);

        if (item == null) {
            throw new ItemAvailabilityException("Item not found: " + itemName);
        }

        InventoryItem itemInventory = new InventoryItem();
        itemInventory.setName(item);
        itemInventory.setQuantity(itemInventoryDTO.getQuantity());
        itemInventory.setUnitPrice(itemInventoryDTO.getUnitPrice());
        inventoryItemRepository.save(itemInventory);
    }

    public boolean isItemAvailable(String itemName, int requestedQuantity) {
        Item inventoryItem = itemRepository.findByName(itemName);

        if (inventoryItem != null && inventoryItem.getRequestedQuantity() >= requestedQuantity) {
            return true;
        } else {
            return false;
        }

}
}
