package ensah.absencemanagement.dtos.messages;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessageRequest {

    private String object;

    @NotNull(message = "Contenu ne doit pas être null")
    @NotEmpty(message = "Contenu ne doit pas être vide")
    private String content;

}
