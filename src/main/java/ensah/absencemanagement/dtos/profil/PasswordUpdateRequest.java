package ensah.absencemanagement.dtos.profil;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PasswordUpdateRequest {

    @NotNull(message = "Mot de passe actuel doit être different de null")
    @NotEmpty(message = "Mot de passe actuel ne doit pas être vide")
    private String currentPassword;

    @NotNull(message = "Nouveau mot de passe doit être different de null")
    @NotEmpty(message = "Nouveau mot de passe ne doit pas être vide")
    private String newPassword;

}
