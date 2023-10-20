package solo.project.inventorymanagementsystem.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import solo.project.inventorymanagementsystem.models.entitites.Item;
import solo.project.inventorymanagementsystem.models.entitites.User;
import solo.project.inventorymanagementsystem.models.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    private String orderNumber;
    private LocalDateTime submittedDate;
    private OrderStatus status;
    private LocalDateTime deadlineDate;
    private User user_id;
    private List<Item> item;
}
