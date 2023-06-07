package ensah.absencemanagement.dtos.pieces_justificatives;

import ensah.absencemanagement.dtos.files.FileDTO;
import ensah.absencemanagement.models.absence.Absence;
import ensah.absencemanagement.models.images.File;
import lombok.*;

import java.util.Date;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PieceJustificativeDTO {

    private Long id;

    private String text;

    private FileDTO source;

    private Long absenceId;

    private Absence.AbsenceEtat state;

    private Date createdAt;

    private Date updatedAt;

}
