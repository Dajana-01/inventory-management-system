package solo.project.inventorymanagementsystem.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import solo.project.inventorymanagementsystem.exception.ItemAvailabilityException;
import solo.project.inventorymanagementsystem.models.dto.OrderDTO;
import solo.project.inventorymanagementsystem.models.entitites.Item;
import solo.project.inventorymanagementsystem.models.entitites.Order;
import solo.project.inventorymanagementsystem.models.entitites.User;
import solo.project.inventorymanagementsystem.models.enums.OrderStatus;
import solo.project.inventorymanagementsystem.repository.OrderRepository;
import solo.project.inventorymanagementsystem.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {


    private final OrderRepository orderRepository;

    private final UserRepository userRepository;


    private final InventoryItemService itemInventoryService;
    private InventoryItemService inventoryItemRepository;

    private String generateOrderNumber() {

        return UUID.randomUUID().toString();
    }

@Transactional
    public Order createOrder(OrderDTO orderDTO) {

        boolean itemsAvailable = checkItemAvailability(orderDTO.getItem(), orderDTO);


        if (!itemsAvailable) {
            throw new ItemAvailabilityException("One or more items are not available.");

        }

        log.info("Creating order ");

        Order order = new Order();
        order.setOrderNumber(generateOrderNumber());
        order.setSubmittedDate(LocalDateTime.now());
        order.setStatus(OrderStatus.CREATED);
        order.setDeadlineDate(orderDTO.getDeadlineDate());

        User user = orderDTO.getUser_id();
        order.setUser(user);


        List<Item> itemDTOList = orderDTO.getItem();
        if (itemDTOList != null) {
            List<Item> itemList = itemDTOList.stream()
                    .map(itemDTO -> {
                        Item item = new Item();
                        item.setName(itemDTO.getName());
                        item.setRequestedQuantity(itemDTO.getRequestedQuantity());
                        return item;
                    })
                    .collect(Collectors.toList());
            order.setOrderItems(itemList);

        } else {
            log.error("ItemDTOList is null. Unable to create the order.");
        }
        orderRepository.save(order);
       // updateInventoryQuantities(order);
        return order;
    }

    private boolean checkItemAvailability(List<Item> items, OrderDTO orderDTO) {
        for (Item item : items) {
            int requestedQuantity = determineRequestedQuantity(item, orderDTO);

            if (!itemInventoryService.isItemAvailable(item.getName(), requestedQuantity)) {
                return false;
            }
        }
        return true;
    }

    private int determineRequestedQuantity(Item item, OrderDTO orderDTO) {
        List<Item> itemDTOList = orderDTO.getItem();
        for (Item itemDTO : itemDTOList) {
            if (item.getName().equals(itemDTO.getName())) {
                return itemDTO.getRequestedQuantity();
            }
        }
        return 0;
    }


    public Order updateOrder(Long orderId, Order updatedOrder, List<Item> newItems, List<Long> itemsToRemove) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);

        if (optionalOrder.isPresent()) {
            Order existingOrder = optionalOrder.get();
            OrderStatus status = existingOrder.getStatus();

            if (status == OrderStatus.CREATED || status == OrderStatus.DECLINED) {
                existingOrder.setOrderItems(updatedOrder.getOrderItems());
                existingOrder.setOrderNumber(updatedOrder.getOrderNumber());
                existingOrder.setUser(updatedOrder.getUser());
                existingOrder.setStatus(updatedOrder.getStatus());
                existingOrder.setDeadlineDate(updatedOrder.getDeadlineDate());
                existingOrder.setSubmittedDate(updatedOrder.getSubmittedDate());

                if (newItems != null) {
                    for (Item newItem : newItems) {
                        newItem.setOrder(existingOrder);
                    }
                }

                if (itemsToRemove != null) {
                    for (Long itemId : itemsToRemove) {
                        existingOrder.getOrderItems().removeIf(item -> item.getId().equals(itemId));
                    }
                }

                for (Item updatedItem : updatedOrder.getOrderItems()) {
                    for (Item existingItem : existingOrder.getOrderItems()) {
                        if (existingItem.getId().equals(updatedItem.getId())) {
                            existingItem.setRequestedQuantity(updatedItem.getRequestedQuantity());
                        }
                    }
                }

                return orderRepository.save(existingOrder);
            } else {
                throw new IllegalArgumentException("Order cannot be updated. Status is not in an updatable state (CREATED or DECLINED).");
            }
        } else {
            throw new NoSuchElementException("Order with ID " + orderId + " not found.");
        }
    }

    public String submitOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);

        if (order == null) {
            return "Order not found";
        }

        OrderStatus currentStatus = order.getStatus();

        if (currentStatus == OrderStatus.CREATED || currentStatus == OrderStatus.DECLINED) {
            order.setStatus(OrderStatus.AWAITING_APPROVAL);
            orderRepository.save(order);
            return "Order successfully submitted";
        } else {
            return "Order status not eligible for submission";
        }
    }

    public String cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);

        if (order == null) {
            return "Order not found";
        }

        OrderStatus currentStatus = order.getStatus();

        if (currentStatus != OrderStatus.FULFILLED
                && currentStatus != OrderStatus.UNDER_DELIVERY
                && currentStatus != OrderStatus.CANCELED) {
            order.setStatus(OrderStatus.CANCELED);
            orderRepository.save(order);
            return "Order successfully canceled";
        } else {
            return "Order status not eligible for cancellation";
        }
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    public Optional<Order> getOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }
}