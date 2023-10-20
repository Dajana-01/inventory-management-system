package solo.project.inventorymanagementsystem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import solo.project.inventorymanagementsystem.models.dto.OrderDTO;
import solo.project.inventorymanagementsystem.models.entitites.Item;
import solo.project.inventorymanagementsystem.models.entitites.Order;
import solo.project.inventorymanagementsystem.models.enums.OrderStatus;
import solo.project.inventorymanagementsystem.repository.OrderRepository;
import solo.project.inventorymanagementsystem.service.OrderService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static solo.project.inventorymanagementsystem.models.enums.OrderStatus.APPROVED;
import static solo.project.inventorymanagementsystem.models.enums.OrderStatus.DECLINED;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {


    private final OrderService orderService;
   private final OrderRepository orderRepository;

    @PostMapping("/create")
    public ResponseEntity<Order> createOrder(@RequestBody @Valid OrderDTO orderDTO) {
        Order createdOrder = orderService.createOrder(orderDTO);
        return ResponseEntity.ok(createdOrder);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<Order> updateOrder(
            @PathVariable Long id,
            @RequestBody Order updatedOrder,
            @RequestBody List<Item> newItems,  // Request to add new items
            @RequestParam List<Long> itemsToRemove  // Request to remove items by their IDs
    ) {
        try {
            Order updated = orderService.updateOrder(id, updatedOrder, newItems, itemsToRemove);
            return ResponseEntity.ok(updated);
        }  catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/cancel/{id}")
    public ResponseEntity<String> cancelOrder(@PathVariable Long id) {
        String result = orderService.cancelOrder(id);
        if (result.equals("Order successfully canceled")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }
    @PostMapping("/submit/{id}")
    public ResponseEntity<String> submitOrder(@PathVariable Long orderId) {
        String result = orderService.submitOrder(orderId);
        if (result.equals("Order successfully submitted")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }
    @GetMapping("/all")
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/byStatus")
    public List<Order> getOrdersByStatus(@RequestParam("status") OrderStatus status) {
        return orderService.getOrdersByStatus(status);
    }
    @PostMapping("/{id}/approve")
    public ResponseEntity<String> approveOrder(@PathVariable Long id) {
        Optional<Order> orderOptional = orderRepository.findByIdAndStatus(id, "AWAITING_APPROVAL");
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.setStatus(APPROVED);
            orderRepository.save(order);
            return ResponseEntity.ok("Order approved successfully");
        } else {
            return ResponseEntity.badRequest().body("Order not found or not in AWAITING_APPROVAL status");
        }
    }

    @PostMapping("/{id}/decline")
    public ResponseEntity<String> declineOrder(@PathVariable Long id, @RequestParam String reason) {
        Optional<Order> orderOptional = orderRepository.findByIdAndStatus(id, "AWAITING_APPROVAL");
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.setStatus(DECLINED);
            orderRepository.save(order);
            return ResponseEntity.ok("Order declined successfully");
        } else {
            return ResponseEntity.badRequest().body("Order not found or not in AWAITING_APPROVAL status");
        }
    }
}


