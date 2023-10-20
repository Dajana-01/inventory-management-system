package solo.project.inventorymanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import solo.project.inventorymanagementsystem.models.entitites.Delivery;
import solo.project.inventorymanagementsystem.models.entitites.Truck;

import java.util.List;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    List<Delivery> findByTrucks(Truck truck);
}