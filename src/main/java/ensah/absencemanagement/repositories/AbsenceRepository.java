package ensah.absencemanagement.repositories;

import ensah.absencemanagement.models.absence.Absence;
import ensah.absencemanagement.models.annees_univ.AnneeUniv;
import ensah.absencemanagement.models.enseignants.Enseignant;
import ensah.absencemanagement.models.etudiants.Etudiant;
import ensah.absencemanagement.models.matieres.Matiere;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AbsenceRepository extends JpaRepository<Absence, Long> {

    Page<Absence> findByEnseignant(Enseignant enseignant, Pageable pageable);

    Page<Absence> findByEnseignantAndEtat(Enseignant enseignant, Absence.AbsenceEtat etat, Pageable pageable);

    Page<Absence> findByEnseignantId(Long enseignantId, Pageable pageable);

    Page<Absence> findByEtudiant(Etudiant etudiant, Pageable pageable);

    Page<Absence> findByEtudiantAndMatiere(Etudiant etudiant, Matiere matiere, Pageable pageable);

    Page<Absence> findByEtudiantAndInscriptionAnneeUniv(Etudiant etudiant, AnneeUniv anneeUniv, Pageable pageable);

    Page<Absence> findByEtudiantAndEtat(Etudiant etudiant, Absence.AbsenceEtat etat, Pageable pageable);

    Page<Absence> findByEtudiantId(Long etudiantId, Pageable pageable);

    Page<Absence> findByEtat(Absence.AbsenceEtat etat, Pageable pageable);

}
