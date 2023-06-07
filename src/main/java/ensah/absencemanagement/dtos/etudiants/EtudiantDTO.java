package ensah.absencemanagement.dtos.etudiants;

import ensah.absencemanagement.dtos.accounts.AccountDTO;
import ensah.absencemanagement.dtos.files.ImageDTO;
import ensah.absencemanagement.dtos.niveaux.NiveauSummaryDTO;
import ensah.absencemanagement.models.users.User;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class EtudiantDTO {

    private Long id;

    private String lastnameAr;

    private String firstnameAr;

    private String lastnameFr;

    private String firstnameFr;

    private String email;

    private String phoneNumber;

    private ImageDTO image;

    private NiveauSummaryDTO niveau;

    private String cne;

    private AccountDTO account;

    private User.Role role;

    private Date createdAt;

    private Date updatedAt;

}
