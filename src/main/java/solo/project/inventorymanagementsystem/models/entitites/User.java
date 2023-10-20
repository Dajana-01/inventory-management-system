package solo.project.inventorymanagementsystem.models.entitites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import solo.project.inventorymanagementsystem.models.enums.Role;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    @Column(unique = true)
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Order> orders;

    @OneToMany(mappedBy = "user")
    private List<TokenModel> tokens;
    public User(String login, String encryptedPassword, Role role,String email) {
        this.username = login;
        this.password = encryptedPassword;
        this.role = role;
        this.email=email;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == Role.SYSTEM_ADMIN) return List.of(new SimpleGrantedAuthority("ROLE_SYSTEM_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"), new SimpleGrantedAuthority("ROLE_WAREHOUSE_MANAGER"));
        if (this.role == Role.CLIENT) return List.of(new SimpleGrantedAuthority("ROLE_CLIENT"));
        if (this.role == Role.WAREHOUSE_MANAGER) return List.of(new SimpleGrantedAuthority("ROLE_WAREHOUSE_MANAGER"));
        else return List.of(new SimpleGrantedAuthority("ROLE_CLIENT"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}