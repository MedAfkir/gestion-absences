package ensah.absencemanagement.dtos.absences;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class MultipleAbsenceRequest<T> {

    @NotNull(message = "Identifiant de la matière ne doit pas être different de null")
    private Long matiereId;

    @NotNull(message = "Identifiant de l'enseignant ne doit pas être different de null")
    private Long enseignantId;

    private T etudiants;

    @NotNull(message = "Identifiant du type de sceance ne doit pas être different de null")
    private Long typeSeanceId;

    @NotNull(message = "Date d'absence ne doit pas être null")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date moment;

}
