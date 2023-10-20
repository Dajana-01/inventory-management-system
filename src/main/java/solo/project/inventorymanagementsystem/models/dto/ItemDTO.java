package solo.project.inventorymanagementsystem.models.dto;

import lombok.Data;
import solo.project.inventorymanagementsystem.models.entitites.Order;
@Data
public class ItemDTO {
    private Long id;

    private String name;
    private int requestedQuantity;

    private Order order;
}
