package solo.project.inventorymanagementsystem.models.entitites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "deliveries")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    private List<Truck> trucks;

    @ManyToOne
    private Order order;

    @Temporal(TemporalType.DATE)
    private Date deliveryDate;


}