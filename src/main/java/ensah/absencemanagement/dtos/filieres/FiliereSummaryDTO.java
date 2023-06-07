package ensah.absencemanagement.dtos.filieres;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FiliereSummaryDTO {

    private Long id;

    private String name;

    private String alias;

    private Date accreditation;

    private Date finAccreditation;

    private Date createdAt;

    private Date updatedAt;

}
