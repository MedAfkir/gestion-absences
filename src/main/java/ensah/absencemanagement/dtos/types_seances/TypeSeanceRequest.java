package ensah.absencemanagement.dtos.types_seances;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TypeSeanceRequest {

    @NotNull(message = "Nom de type de sceance ne doit pas être null")
    @NotEmpty(message = "Nom de type de sceance ne doit pas être vide")
    private String name;

    @NotNull(message = "Alias de type de sceance ne doit pas être null")
    @NotEmpty(message = "Alias de type de sceance ne doit pas être vide")
    private String alias;

}
