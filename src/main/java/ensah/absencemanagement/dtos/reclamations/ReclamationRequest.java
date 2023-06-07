package ensah.absencemanagement.dtos.reclamations;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReclamationRequest {

    @NotNull(message = "Objet ne doit pas être null")
    @NotEmpty(message = "Objet ne doit pas être vide")
    private String object;

    @NotNull(message = "Contenu ne doit pas être null")
    @NotEmpty(message = "Contenu ne doit pas être vide")
    private String content;

    @NotNull(message = "Identifiant de l'absence ne doit pas être different de null")
    private Long absenceId;

}
