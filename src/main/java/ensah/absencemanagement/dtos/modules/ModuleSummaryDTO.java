package ensah.absencemanagement.dtos.modules;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ModuleSummaryDTO {

    private Long id;

    private String name;

    private String alias;

}
