package solo.project.inventorymanagementsystem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import solo.project.inventorymanagementsystem.models.dto.AuthenticationDTO;
import solo.project.inventorymanagementsystem.models.dto.LoginResponseDTO;
import solo.project.inventorymanagementsystem.models.dto.RegisterDTO;
import solo.project.inventorymanagementsystem.models.dto.UserDto;
import solo.project.inventorymanagementsystem.service.AuthenticationService;
import solo.project.inventorymanagementsystem.service.UserService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {


    private AuthenticationService authenticationService;

    private final UserService userService;


    @PostMapping("/login")
   // @Secured({"ROLE_SYSTEM_ADMIN", "ROLE_CLIENT","ROLE_WAREHOUSE_MANAGER"})
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data) {
        var auth = authenticationService.login(data);
        return ResponseEntity.ok(new LoginResponseDTO(userService.getToken(auth)));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO data) throws Exception {
        this.userService.create(data);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity findById(@RequestBody UserDto userDto){
        this.userService.findById(userDto.getId());
        return ResponseEntity.ok().build();
    }


}
