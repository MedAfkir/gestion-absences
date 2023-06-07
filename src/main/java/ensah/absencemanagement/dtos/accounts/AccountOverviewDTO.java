package ensah.absencemanagement.dtos.accounts;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class AccountOverviewDTO {

    private String username;

    private String password;

}
