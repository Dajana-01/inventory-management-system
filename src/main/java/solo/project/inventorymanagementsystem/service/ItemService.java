package solo.project.inventorymanagementsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import solo.project.inventorymanagementsystem.models.dto.ItemDTO;
import solo.project.inventorymanagementsystem.models.entitites.Item;
import solo.project.inventorymanagementsystem.models.entitites.Order;
import solo.project.inventorymanagementsystem.repository.ItemRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    private final OrderService orderService;

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public String updateItem(Long itemId, ItemDTO itemDTO) {
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        if (optionalItem.isPresent()) {
            Item item = optionalItem.get();
            item.setName(itemDTO.getName());
            item.setRequestedQuantity(itemDTO.getRequestedQuantity());
            itemRepository.save(item);
            return "Item updated successfully";
        }

        return "Item not found";
    }
    public String deleteItem(Long itemId) {
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        if (optionalItem.isPresent()) {
            itemRepository.deleteById(itemId);
            return "Item deleted successfully";
        }
        return "Item not found. Deletion failed";
    }
    public Item getItem(Long itemId) {
        return itemRepository.findById(itemId).orElse(null);
    }

    public String createItem(ItemDTO itemDTO) {
        if (itemDTO != null && itemDTO.getOrder() != null) {
            Optional<Order> order = orderService.getOrderById(itemDTO.getOrder().getId());

            if (order.isPresent()) {
                Item item = new Item();
                item.setName(itemDTO.getName());
                item.setRequestedQuantity(itemDTO.getRequestedQuantity());
                item.setOrder(order.get());

                itemRepository.save(item);
                return "Item created successfully";
            }
        }

        return "Order not found. Item not created";
    }


}
