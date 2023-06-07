package ensah.absencemanagement.dtos.annees_univ;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnneeUnivDTO {

    private Long id;

    private String name;

    private boolean current;

    private Date start;

    private Date end;

    private Date createdAt;

    private Date updatedAt;

}
