package ensah.absencemanagement.dtos.inscriptions;

import ensah.absencemanagement.dtos.annees_univ.AnneeUnivDTO;
import ensah.absencemanagement.dtos.niveaux.NiveauSummaryDTO;
import ensah.absencemanagement.dtos.users.UserSummaryDTO;
import ensah.absencemanagement.models.inscriptions.Inscription;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class InscriptionDTO {

    private Long id;

    private AnneeUnivDTO anneeUniv;

    private Inscription.InscriptionState etat;

    private UserSummaryDTO etudiant;

    private NiveauSummaryDTO niveau;

    private Date createdAt;

    private Date updatedAt;

}
