package ensah.absencemanagement.dtos.reclamations;

import ensah.absencemanagement.dtos.messages.MessageDTO;
import lombok.*;

import java.util.Date;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReclamationDTO {

    private Long id;

    private String object;

    private String content;

    private Long absenceId;

    private int responsesCount;

    private List<MessageDTO> responses;

    private Date createdAt;

    private Date updatedAt;

}
