package solo.project.inventorymanagementsystem.service;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import solo.project.inventorymanagementsystem.models.dto.DeliveryDTO;
import solo.project.inventorymanagementsystem.models.entitites.Delivery;
import solo.project.inventorymanagementsystem.models.entitites.Item;
import solo.project.inventorymanagementsystem.models.entitites.Order;
import solo.project.inventorymanagementsystem.models.entitites.Truck;
import solo.project.inventorymanagementsystem.repository.DeliveryRepository;
import solo.project.inventorymanagementsystem.repository.OrderRepository;
import solo.project.inventorymanagementsystem.repository.TruckRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static solo.project.inventorymanagementsystem.models.enums.OrderStatus.UNDER_DELIVERY;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;


    private final OrderRepository orderRepository;


    private final TruckRepository truckRepository;
    private DeliveryDTO scheduledDeliveryData;

    @Scheduled(cron = "0 0 8 * * MON-SAT")
    public void scheduledDeliveryTask() {
        if (scheduledDeliveryData != null) {
            scheduleDelivery(scheduledDeliveryData);
        } else {
            log.warn("No scheduled delivery data available for the task.");
        }
    }
    public void scheduleDelivery(DeliveryDTO deliveryDTO) {
        log.info("Schedule start");
        validateDeliveryDate(deliveryDTO.getDeliveryDate());
        Order order = validateOrder(deliveryDTO.getOrderId());
        List<Truck> selectedTrucks = validateTrucks(deliveryDTO.getTruckIds());

        checkTrucksCanCompleteDelivery(selectedTrucks, deliveryDTO.getDeliveryDate());
        checkItemQuantities(order);

        createAndSaveDelivery(deliveryDTO, order, selectedTrucks);
        log.info("Schedule end");
    }

    private void validateDeliveryDate(Date deliveryDate) {
        // Check if delivery date is not on a Sunday
        LocalDate localDate = deliveryDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if (localDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new ValidationException("Delivery on Sunday is not allowed.");
        }
    }

    private Order validateOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .filter(o -> o.getStatus().equals("AWAITING_DELIVERY"))
                .orElseThrow(() -> new ValidationException("Invalid order or order status."));
        return order;
    }
    private List<Truck> validateTrucks(List<Long> truckIds) {
        List<Truck> selectedTrucks = truckRepository.findAllById(truckIds);
        if (selectedTrucks.isEmpty()) {
            throw new ValidationException("At least one truck must be selected for delivery.");
        }
        return selectedTrucks;
    }
    private void checkTrucksCanCompleteDelivery(List<Truck> selectedTrucks, Date deliveryDate) {
        for (Truck truck : selectedTrucks) {
            LocalDate lastDeliveryDate = getLastDeliveryDate(truck);
            if (lastDeliveryDate != null && lastDeliveryDate.equals(deliveryDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate())) {
                throw new ValidationException("Truck " + truck.getId() + " has already completed a delivery on this date.");
            }
        }
    }
    private void checkItemQuantities(Order order) {
        List<Item> orderItems = order.getOrderItems(); //  items associated with the order
        int totalItemsToDeliver = 0;

        for (Item item : orderItems) {
            totalItemsToDeliver += item.getRequestedQuantity();
        }

        if (totalItemsToDeliver > 10) {
            throw new ValidationException("The selected trucks cannot carry more than 10 items.");
        }
    }

    private LocalDate getLastDeliveryDate(Truck truck) {
        List<Delivery> truckDeliveries = deliveryRepository.findByTrucks(truck);

        if (truckDeliveries.isEmpty()) {
            return null;
        }

        Date lastDeliveryDate = truckDeliveries.stream()
                .map(Delivery::getDeliveryDate)
                .max(Date::compareTo)
                .orElse(null);

        if (lastDeliveryDate != null) {
            return lastDeliveryDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }

        return null;
    }


    private void createAndSaveDelivery(DeliveryDTO deliveryDTO, Order order, List<Truck> selectedTrucks) {
        Delivery delivery = new Delivery();
        delivery.setDeliveryDate(deliveryDTO.getDeliveryDate());
        delivery.setTrucks(selectedTrucks);
        delivery.setOrder(order);

        order.setStatus(UNDER_DELIVERY);

        deliveryRepository.save(delivery);
    }



}
