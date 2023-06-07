package ensah.absencemanagement.dtos.users;

import ensah.absencemanagement.dtos.files.ImageDTO;
import ensah.absencemanagement.models.users.User;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class UserSummaryDTO {

    private Long id;

    private String lastnameFr;

    private String firstnameFr;

    private ImageDTO image;

    private User.Role role;

}
