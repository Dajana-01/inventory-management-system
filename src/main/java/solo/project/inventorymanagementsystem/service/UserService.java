package solo.project.inventorymanagementsystem.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import solo.project.inventorymanagementsystem.config.JwtService;
import solo.project.inventorymanagementsystem.models.dto.RegisterDTO;
import solo.project.inventorymanagementsystem.models.entitites.User;
import solo.project.inventorymanagementsystem.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;


    private final JwtService jwtService;

    @Transactional
    public User create(RegisterDTO user) throws Exception {
        String encryptedPassword = new BCryptPasswordEncoder().encode(user.password());
        User newUser = new User(user.username(), encryptedPassword, user.role(), user.email());
        return this.userRepository.save(newUser);
    }



    public String getToken(Authentication authentication) {
        return this.jwtService.generateToken(authentication.getPrincipal().toString());
    }

    public void findById(Long id) {
    }

}