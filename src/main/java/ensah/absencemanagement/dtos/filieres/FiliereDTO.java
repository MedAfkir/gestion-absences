package ensah.absencemanagement.dtos.filieres;

import ensah.absencemanagement.dtos.niveaux.NiveauSummaryDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FiliereDTO {

    private Long id;

    private String name;

    private String alias;

     private List<NiveauSummaryDTO> niveaux;

     // TODO List<Coordinateur> coordinateurs;

    private Date accreditation;

    private Date finAccreditation;

    private Date createdAt;

    private Date updatedAt;

}
