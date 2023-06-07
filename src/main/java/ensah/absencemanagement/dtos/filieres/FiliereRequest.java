package ensah.absencemanagement.dtos.filieres;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FiliereRequest {

    @NotNull(message = "Nom de filiere ne doit pas être null")
    @NotEmpty(message = "Nom de filiere ne doit pas être vide")
    private String name;

    @NotNull(message = "Alias de filiere ne doit pas être null")
    @NotEmpty(message = "Alias de filiere ne doit pas être vide")
    private String alias;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Date d'accréditation ne doit pas être nulle")
    private Date accreditation;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date finAccreditation;

}
