package solo.project.inventorymanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import solo.project.inventorymanagementsystem.models.dto.UserDto;
import solo.project.inventorymanagementsystem.models.entitites.Order;
import solo.project.inventorymanagementsystem.models.enums.OrderStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    Optional<Order> findByUser(UserDto user);

    List<Order> findByStatus(OrderStatus status);

    Optional<Order> findByIdAndStatus(Long id, String awaitingApproval);
}
