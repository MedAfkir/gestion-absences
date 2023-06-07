package ensah.absencemanagement.dtos.messages;

import lombok.*;

import java.util.Date;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessageDTO {

    private Long id;

    private String object;

    private String content;

    private Date createdAt;

    private Date updatedAt;

}
