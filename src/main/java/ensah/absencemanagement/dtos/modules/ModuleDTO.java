package ensah.absencemanagement.dtos.modules;

import ensah.absencemanagement.dtos.matieres.MatiereSummaryDTO;
import ensah.absencemanagement.dtos.niveaux.NiveauSummaryDTO;
import lombok.*;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ModuleDTO {

    private Long id;

    private String name;

    private String alias;

    private NiveauSummaryDTO niveau;

    private List<MatiereSummaryDTO> matieres;

    private Date createdAt;

    private Date updatedAt;

}
