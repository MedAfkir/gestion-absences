package ensah.absencemanagement.models.enseignants;

import ensah.absencemanagement.models.users.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "enseignants")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Enseignant extends User {

    @Column(nullable = false)
    private String cin;

    private boolean receiveAuthorisationDemands;

    @Override
    public Role getRole() {
        return Role.ENSEIGNANT;
    }

}
