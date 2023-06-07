package ensah.absencemanagement.dtos.matieres;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MatiereUpdateRequest {

    @NotNull(message = "Nom de matière ne doit pas être null")
    @NotEmpty(message = "Nom de matière ne doit pas être vide")
    private String name;

    @NotNull(message = "Alias de matière ne doit pas être null")
    @NotEmpty(message = "Alias de matière ne doit pas être vide")
    private String alias;

    private Long moduleId;

}
