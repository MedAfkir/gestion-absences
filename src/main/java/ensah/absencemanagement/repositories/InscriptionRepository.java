package ensah.absencemanagement.repositories;

import ensah.absencemanagement.models.inscriptions.Inscription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InscriptionRepository extends JpaRepository<Inscription, Long> {

    Optional<Inscription> findByEtatAndAnneeUnivCurrentAndEtudiantId(Inscription.InscriptionState etat, boolean current, Long etudiantId);

    Page<Inscription> findByEtudiantId(Long etudiantId, Pageable pageable);

    List<Inscription> findByNiveauIdAndEtat(Long niveauId, Inscription.InscriptionState etat);

}
