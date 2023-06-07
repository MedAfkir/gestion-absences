package ensah.absencemanagement.models.accounts;

import ensah.absencemanagement.models.users.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

@Entity
@Table(name = "accounts")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Account implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, updatable = false)
    private String username;

    private String password;

    private boolean active;

    private int failureCount;

    @Transient
    private boolean notLocked;

    @OneToOne
    @JoinColumn(nullable = false, updatable = false)
    private User user;

    private Date createdAt;

    private Date updatedAt;

    public boolean isNotLocked() {
        // TODO update the max of failure count by reading it from the app properties
        return failureCount < 3;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(user.getRole());
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

    public boolean isEnabled() {
        return true;
    }

}
