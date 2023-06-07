package ensah.absencemanagement.dtos.absences;

import ensah.absencemanagement.dtos.matieres.MatiereSummaryDTO;
import ensah.absencemanagement.dtos.pieces_justificatives.PieceJustificativeDTO;
import ensah.absencemanagement.dtos.types_seances.TypeSeanceDTO;
import ensah.absencemanagement.dtos.users.UserSummaryDTO;
import ensah.absencemanagement.models.absence.Absence;
import lombok.*;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class AbsenceDTO {

    private Long id;

    private Date moment;

    private Absence.AbsenceEtat etat;

    private TypeSeanceDTO typeSeance;

    private UserSummaryDTO enseignant;

    private UserSummaryDTO etudiant;

    private MatiereSummaryDTO matiere;

    private List<PieceJustificativeDTO> pieces;

    private Date createdAt;

    private Date updatedAt;

}
