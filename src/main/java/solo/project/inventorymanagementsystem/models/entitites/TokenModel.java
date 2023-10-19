package solo.project.inventorymanagementsystem.models.entitites;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="tokens")
public class TokenModel {
    @Id
    private Long id;

    @NotNull
    private Date updatedDate;


    @NotNull
    private String jwtToken;
}

