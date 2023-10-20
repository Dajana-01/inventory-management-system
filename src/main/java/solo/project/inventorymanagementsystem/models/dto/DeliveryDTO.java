package solo.project.inventorymanagementsystem.models.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class DeliveryDTO {
    private Date deliveryDate;
    private List<Long> truckIds;
    private Long orderId;
}