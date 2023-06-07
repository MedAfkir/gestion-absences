package ensah.absencemanagement.dtos.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ensah.absencemanagement.models.etudiants.Etudiant;
import ensah.absencemanagement.models.enseignants.Enseignant;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserRequest {

    @NotNull(message = "Nom en arabe doit être different de null")
    @NotEmpty(message = "Nom en arabe ne doit pas être vide")
    private String lastnameAr;

    @NotNull(message = "Prénom en arabe doit être different de null")
    @NotEmpty(message = "Prénom en arabe ne doit pas être vide")
    private String firstnameAr;

    @NotNull(message = "Nom doit être different de null")
    @NotEmpty(message = "Nom ne doit pas être vide")
    private String lastnameFr;

    @NotNull(message = "Prénom doit être different de null")
    @NotEmpty(message = "Prénom ne doit pas être vide")
    private String firstnameFr;

    @Email(message = "Email invalide")
    private String email;

    private String phoneNumber;

    /** Attribut réservé pour {@link Enseignant} */
    private String cin;

    /** Attribut réservé pour {@link Etudiant} */
    private String cne;

    /** Attribut réservé pour {@link Etudiant} */
    private String dateNaissance;

}
