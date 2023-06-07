package ensah.absencemanagement.dtos.modules;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ModuleAddRequest {

    @NotNull(message = "Nom de module ne doit pas être null")
    @NotEmpty(message = "Nom de module ne doit pas être vide")
    private String name;

    @NotNull(message = "Alias de module ne doit pas être null")
    @NotEmpty(message = "Alias de module ne doit pas être vide")
    private String alias;

}
