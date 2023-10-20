package solo.project.inventorymanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import solo.project.inventorymanagementsystem.models.entitites.TokenModel;

import java.util.Optional;
@Repository
public interface TokenRepository extends JpaRepository<TokenModel,Long> {

    Optional<TokenModel> findByToken(String token);

}
