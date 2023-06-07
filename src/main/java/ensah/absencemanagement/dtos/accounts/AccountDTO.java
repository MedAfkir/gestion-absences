package ensah.absencemanagement.dtos.accounts;

import ensah.absencemanagement.dtos.users.UserDTO;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class AccountDTO {

    private Long id;

    private String username;

    private boolean active;

    private boolean notExpired;

    private boolean notLocked;

    private UserDTO user;

    private Date createdAt;

    private Date updatedAt;

}
