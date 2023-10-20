package solo.project.inventorymanagementsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import solo.project.inventorymanagementsystem.models.dto.TruckDTO;
import solo.project.inventorymanagementsystem.models.entitites.Truck;
import solo.project.inventorymanagementsystem.repository.TruckRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TruckService {

    private final TruckRepository truckRepository;

    public List<Truck> getAllTrucks() {
        return truckRepository.findAll();
    }

    public Truck getTruckById(Long id) {
        return truckRepository.findById(id).orElse(null);
    }

    public Truck createTruck(TruckDTO truckDTO) {
        Truck truck = new Truck();
        truck.setChassisNumber(truckDTO.getChassisNumber());
        truck.setLicensePlate(truckDTO.getLicensePlate());
        truck.setAvailable(truckDTO.isAvailable());
        return truckRepository.save(truck);
    }

    public Truck updateTruck(Long id, TruckDTO truckDTO) {
        Truck truck = truckRepository.findById(id).orElse(null);
        if (truck != null) {
            truck.setChassisNumber(truckDTO.getChassisNumber());
            truck.setLicensePlate(truckDTO.getLicensePlate());
            truck.setAvailable(truckDTO.isAvailable());
            return truckRepository.save(truck);
        }
        return null;
    }

    public void deleteTruck(Long id) {
        truckRepository.deleteById(id);
    }

}
