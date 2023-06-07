package ensah.absencemanagement.dtos.pieces_justificatives;

import ensah.absencemanagement.models.absence.Absence;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class PieceJusitifcativeRequest {

    private String text;

    private Absence.AbsenceEtat state;

    private MultipartFile source;

    private Long absenceId;

}
