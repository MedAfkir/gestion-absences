package ensah.absencemanagement.dtos.types_seances;

import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class TypeSeanceDTO {

    private Long id;

    private String name;

    private String alias;

    private Date createdAt;

    private Date updatedAt;

}
