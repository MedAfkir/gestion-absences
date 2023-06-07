package ensah.absencemanagement.dtos.niveaux;

import ensah.absencemanagement.dtos.filieres.FiliereSummaryDTO;
import ensah.absencemanagement.dtos.modules.ModuleSummaryDTO;
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
public class NiveauDTO {

    private Long id;

    private String name;

    private String alias;

    private FiliereSummaryDTO filiere;

    private List<ModuleSummaryDTO> modules;

    private Date createdAt;

    private Date updatedAt;

}
