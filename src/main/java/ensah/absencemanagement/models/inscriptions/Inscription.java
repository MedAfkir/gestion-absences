package ensah.absencemanagement.models.inscriptions;

import ensah.absencemanagement.models.annees_univ.AnneeUniv;
import ensah.absencemanagement.models.niveaux.Niveau;
import ensah.absencemanagement.models.etudiants.Etudiant;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "inscriptions")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Inscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private AnneeUniv anneeUniv;

    @Enumerated(value = EnumType.STRING)
    private InscriptionState etat;

    @ManyToOne
    private Etudiant etudiant;

    @ManyToOne
    private Niveau niveau;

    private Date createdAt;

    private Date updatedAt;

    public enum InscriptionState {
        ACCEPTED("Acceptée"),
        REFUSED("Réfusée"),
        PENDING("En attente");

        private final String name;

        InscriptionState(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

}
