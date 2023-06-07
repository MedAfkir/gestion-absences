package ensah.absencemanagement.dtos.matieres;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class MatiereSummaryDTO {

    private Long id;

    private String name;

    private String alias;

}
