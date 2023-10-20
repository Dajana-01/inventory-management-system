package solo.project.inventorymanagementsystem.models.dto;

import lombok.Data;

@Data
public class TruckDTO {
    private Long id;
    private String chassisNumber;
    private String licensePlate;
    private boolean available;
}