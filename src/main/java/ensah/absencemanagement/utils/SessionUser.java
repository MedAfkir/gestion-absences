package ensah.absencemanagement.utils;

import ensah.absencemanagement.dtos.files.ImageDTO;
import ensah.absencemanagement.models.users.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class SessionUser {

    private Long id;

    private String username;

    private String firstname;

    private String lastname;

    private User.Role role;

    private ImageDTO image;

}
