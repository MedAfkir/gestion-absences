package ensah.absencemanagement.dtos.niveaux;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NiveauUpdateRequest {

    @NotNull(message = "Nom de niveau ne doit pas être null")
    @NotEmpty(message = "Nom de niveau ne doit pas être vide")
    private String name;

    @NotNull(message = "Alias de niveau ne doit pas être null")
    @NotEmpty(message = "Alias de niveau ne doit pas être vide")
    private String alias;

    private Long filiereId;

}
