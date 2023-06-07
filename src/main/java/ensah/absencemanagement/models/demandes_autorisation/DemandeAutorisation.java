package ensah.absencemanagement.models.demandes_autorisation;

import ensah.absencemanagement.models.enseignants.Enseignant;
import ensah.absencemanagement.models.etudiants.Etudiant;
import ensah.absencemanagement.models.messages.Message;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "demandes_autorisation")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
public class DemandeAutorisation extends Message {

    @ManyToOne
    private Etudiant etudiant;

    @ManyToOne
    private Enseignant enseignant;

    @Column(nullable = false)
    private State state;

    public enum State {

        ACCEPTED("Acceptée"),
        REFUSED("Réfusée"),
        PENDING("En attente");

        private final String name;

        State(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

    }

}