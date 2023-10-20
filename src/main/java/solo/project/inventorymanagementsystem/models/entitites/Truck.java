package solo.project.inventorymanagementsystem.models.entitites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "trucks")
public class Truck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String chassisNumber;

    @Column(unique = true)
    private String licensePlate;

    private boolean available;


}
