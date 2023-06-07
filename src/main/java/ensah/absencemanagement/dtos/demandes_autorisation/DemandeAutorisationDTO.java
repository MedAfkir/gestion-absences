package ensah.absencemanagement.dtos.demandes_autorisation;

import ensah.absencemanagement.dtos.etudiants.EtudiantDTO;
import ensah.absencemanagement.dtos.users.UserDTO;
import ensah.absencemanagement.models.demandes_autorisation.DemandeAutorisation;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DemandeAutorisationDTO {

    private Long id;

    private String object;

    private String content;

    private UserDTO enseignant;

    private EtudiantDTO etudiant;

    private DemandeAutorisation.State state;

    private Date createdAt;

    private Date updatedAt;

}
