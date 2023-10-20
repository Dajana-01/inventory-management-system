package solo.project.inventorymanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import solo.project.inventorymanagementsystem.models.entitites.Item;
@Repository
public interface ItemRepository extends JpaRepository<Item,Long> {
    Item findByName(String itemName);
}
