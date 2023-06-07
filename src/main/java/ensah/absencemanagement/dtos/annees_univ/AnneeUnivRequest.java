package ensah.absencemanagement.dtos.annees_univ;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnneeUnivRequest {

    @NotNull(message = "Nom d'année universitaire ne doit pas être null")
    @NotEmpty(message = "Nom d'année universitaire ne doit pas être vide")
    private String name;

    private boolean current;

    @NotNull(message = "Date de début ne doit pas être null")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date start;

    @NotNull(message = "Date de fin ne doit pas être null")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date end;

}
