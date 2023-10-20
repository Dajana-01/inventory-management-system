package solo.project.inventorymanagementsystem.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import solo.project.inventorymanagementsystem.models.dto.AuthenticationDTO;
import solo.project.inventorymanagementsystem.models.entitites.TokenModel;
import solo.project.inventorymanagementsystem.models.entitites.User;
import solo.project.inventorymanagementsystem.repository.TokenRepository;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private AuthenticationManager authenticationManager;

    private final TokenRepository tokenRepo;

    public void saveUserToken(User user, String jwtToken) {
        var token = TokenModel.builder()
                .user(user)
                .token(jwtToken)
                .isExpired(false)
                .isRevoked(false)
                .build();
        tokenRepo.save(token);
    }

    @Transactional
    public Authentication login(AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
        var auth =authenticationManager.authenticate(usernamePassword);
        return auth;
    }

}
