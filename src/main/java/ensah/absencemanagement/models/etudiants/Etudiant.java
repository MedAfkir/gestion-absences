package ensah.absencemanagement.models.etudiants;

import ensah.absencemanagement.models.absence.Absence;
import ensah.absencemanagement.models.inscriptions.Inscription;
import ensah.absencemanagement.models.niveaux.Niveau;
import ensah.absencemanagement.models.users.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Entity
@Table(name = "etudiants")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Etudiant extends User {

    @OneToMany(mappedBy = "etudiant")
    private List<Inscription> inscriptions;

    @OneToMany(mappedBy = "etudiant")
    private List<Absence> absences;

    @Transient
    private Niveau niveau;

    @Column(nullable = false)
    private String cne;

    @Column(nullable = false)
    private Date dateNaissance;

    @Override
    public Role getRole() {
        return Role.ETUDIANT;
    }

    public Niveau getNiveau() {
        List<Inscription> acceptedInscriptions = inscriptions.stream()
                .filter(i -> i.getEtat().equals(Inscription.InscriptionState.ACCEPTED))
                .toList();

        if (acceptedInscriptions.isEmpty()) return null;

        Optional<Inscription> max = acceptedInscriptions.stream()
                .max(Comparator.comparing(inscription -> inscription.getAnneeUniv().getStart()));
        return max.map(Inscription::getNiveau).orElse(null);
    }

}
