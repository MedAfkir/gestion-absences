package ensah.absencemanagement.dtos.users;

import ensah.absencemanagement.models.users.User;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserRoleRequest {

    @NotNull(message = "Rôle ne doit pas être null")
    private User.Role role;

    /**
     * CIN de l'enseignants
     * <blockquote><pre>ssi this.role == User.Role.ENSEIGNANT </pre></blockquote>
     */
    private String cin;

    /**
     * CNE de l'étudiantETUDIANT})
     * <blockquote><pre>ssi this.role == User.Role.ETUDIANT </pre></blockquote>
     */
    private String cne;

    /**
     * Date de naissance de l'étudiant <br />
     * <blockquote><pre>ssi this.role == User.Role.ETUDIANT </pre></blockquote>
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateNaissance;

}
