package ensah.absencemanagement.dtos.profil;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfilUpdateRequest {

    @NotNull(message = "Email doit être different de null")
    @Email(message = "Email invalide")
    private String email;

    @NotNull
    @NotEmpty(message = "Numéro de téléphone ne doit pas être vide")
    private String phoneNumber;

}
