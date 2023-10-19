package solo.project.inventorymanagementsystem.models.entitites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "inventory_items")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class InventoryItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int quantity;
    private double unitPrice;
    @ManyToOne
    @JoinColumn(name = "item_name")
    private Item item;
}

