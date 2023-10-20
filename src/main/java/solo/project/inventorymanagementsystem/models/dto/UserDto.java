package solo.project.inventorymanagementsystem.models.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import solo.project.inventorymanagementsystem.models.entitites.Order;
import solo.project.inventorymanagementsystem.models.enums.Role;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String password;
    private Role role;

}
