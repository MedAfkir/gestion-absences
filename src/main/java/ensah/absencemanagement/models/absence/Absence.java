package ensah.absencemanagement.models.absence;


import ensah.absencemanagement.models.enseignants.Enseignant;
import ensah.absencemanagement.models.etudiants.Etudiant;
import ensah.absencemanagement.models.inscriptions.Inscription;
import ensah.absencemanagement.models.matieres.Matiere;
import ensah.absencemanagement.models.pieces_justifications.PieceJustificative;
import ensah.absencemanagement.models.types_seance.TypeSeance;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "absences")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class Absence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "moment_absence")
    private Date moment;

    @ManyToOne
    @ToString.Exclude
    private Matiere matiere;

    @ManyToOne
    private TypeSeance typeSeance;

    @ManyToOne
    @JoinColumn(nullable = false, updatable = false)
    @ToString.Exclude
    private Enseignant enseignant;

    @OneToMany(mappedBy = "absence")
    @ToString.Exclude
    private List<PieceJustificative> pieces;

    @ManyToOne
    @ToString.Exclude
    private Etudiant etudiant;

    @ManyToOne
    @ToString.Exclude
    private Inscription inscription;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AbsenceEtat etat;

    private Date createdAt;

    private Date updatedAt;

    public enum AbsenceEtat {

        JUSTIFIEE("Justifiée"),
        NON_JUSTIFIEE("Non jusitifée"),
        ANNULEE("Annulée");

        private final String name;

        AbsenceEtat(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

    }

}
