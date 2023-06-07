package ensah.absencemanagement.models.users;

import ensah.absencemanagement.models.accounts.Account;
import ensah.absencemanagement.models.images.File;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.*;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String lastnameAr;

    @Column(nullable = false)
    private String firstnameAr;

    @Column(nullable = false)
    private String lastnameFr;

    @Column(nullable = false)
    private String firstnameFr;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phoneNumber;

    @OneToOne
    private File image;

    @OneToOne
    private Account account;

    @Transient
    private Role role;

    private boolean deleted = false;

    private Date createdAt;

    private Date updatedAt;

    public enum Role  implements GrantedAuthority {
        CADRE_ADMINISTRATEUR("Cadre Administrateur"),
        ENSEIGNANT("Enseignant"),
        ETUDIANT("Étudiant"),
        SUPER_ADMIN("Administrateur Système");

        private final String name;

        Role(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String getAuthority() {
            return this.name();
        }

        public static List<Role> getRoles() {
            return Arrays.stream(Role.values())
                    .filter(r -> !r.equals(SUPER_ADMIN))
                    .toList();
        }
    }

}
