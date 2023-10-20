package solo.project.inventorymanagementsystem.models.dto;

import solo.project.inventorymanagementsystem.models.enums.Role;

public record RegisterDTO(String username, String password, String email, Role role) {
}