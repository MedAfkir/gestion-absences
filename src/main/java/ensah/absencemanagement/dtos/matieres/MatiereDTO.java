package ensah.absencemanagement.dtos.matieres;

import ensah.absencemanagement.dtos.modules.ModuleSummaryDTO;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class MatiereDTO {

    private Long id;

    private String name;

    private String alias;

    private ModuleSummaryDTO module;

    private Date createdAt;

    private Date updatedAt;

}
