package ensah.absencemanagement.repositories;

import ensah.absencemanagement.models.demandes_autorisation.DemandeAutorisation;
import ensah.absencemanagement.models.enseignants.Enseignant;
import ensah.absencemanagement.models.etudiants.Etudiant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DemandeAutorisationRepository extends JpaRepository<DemandeAutorisation, Long> {

    Page<DemandeAutorisation> findByEtudiantId(Long etudiantId, Pageable pageable);
    Page<DemandeAutorisation> findByEtudiant(Etudiant etudiant, Pageable pageable);

    Page<DemandeAutorisation> findByEnseignantId(Long enseignantId, Pageable pageable);
    Page<DemandeAutorisation> findByEnseignant(Enseignant enseignant, Pageable pageable);

}