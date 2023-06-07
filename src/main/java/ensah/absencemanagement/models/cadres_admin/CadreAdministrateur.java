package ensah.absencemanagement.models.cadres_admin;

import ensah.absencemanagement.models.users.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cadres_admins")
@AllArgsConstructor
@Setter
@Getter
public class CadreAdministrateur extends User {

    @Override
    public Role getRole() {
        return Role.CADRE_ADMINISTRATEUR;
    }

}
