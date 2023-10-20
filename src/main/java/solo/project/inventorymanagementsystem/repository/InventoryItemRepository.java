package solo.project.inventorymanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import solo.project.inventorymanagementsystem.models.entitites.InventoryItem;
import solo.project.inventorymanagementsystem.models.entitites.Item;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {
    InventoryItem findByName(Item name);

}