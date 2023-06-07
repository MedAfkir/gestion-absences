package ensah.absencemanagement.models.admin_system;

import ensah.absencemanagement.models.users.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "super_admin")
@AllArgsConstructor
@Setter
@Getter
public class SuperAdmin extends User {

    @Override
    public Role getRole() {
        return Role.SUPER_ADMIN;
    }

}
