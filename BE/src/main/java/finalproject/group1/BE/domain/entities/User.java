package finalproject.group1.BE.domain.entities;

import finalproject.group1.BE.domain.enums.DeleteFlag;
import finalproject.group1.BE.domain.enums.Role;
import finalproject.group1.BE.domain.enums.UserStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "User_tbl")
@Data
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    @Column(name = "login_id", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "birthday", nullable = false)
    private Date birthday;

    @Column(name = "role", nullable = false)
    @Enumerated
    private Role role;

    @Column(name = "status", nullable = false)
    @Enumerated
    private UserStatus status;

    @Column(name = "delete_flag", nullable = false)
    @Enumerated
    private DeleteFlag deleteFlag;

    @Column(name = "old_login_id")
    private Date oldLoginId;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
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