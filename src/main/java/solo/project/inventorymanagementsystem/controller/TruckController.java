package solo.project.inventorymanagementsystem.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import solo.project.inventorymanagementsystem.models.dto.TruckDTO;
import solo.project.inventorymanagementsystem.models.entitites.Truck;
import solo.project.inventorymanagementsystem.service.TruckService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/trucks")
public class TruckController {

    private final TruckService truckService;

    @GetMapping("/{id}")
    public Truck getTruckById(@PathVariable Long id) {
        return truckService.getTruckById(id);
    }

    @PostMapping("/create")
    public Truck createTruck(@RequestBody TruckDTO truckDTO) {
        return truckService.createTruck(truckDTO);
    }

    @PutMapping("/{id}")
    public Truck updateTruck(@PathVariable Long id, @RequestBody TruckDTO truckDTO) {
        return truckService.updateTruck(id, truckDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteTruck(@PathVariable Long id) {
        truckService.deleteTruck(id);
    }
}