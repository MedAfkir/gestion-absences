package ensah.absencemanagement.dtos.niveaux;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class NiveauSummaryDTO {

    private Long id;

    private String name;

    private String alias;

    private Date createdAt;

    private Date updatedAt;

}
