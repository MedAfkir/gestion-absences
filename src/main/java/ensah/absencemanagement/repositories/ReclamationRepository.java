package ensah.absencemanagement.repositories;

import ensah.absencemanagement.models.reclamations.Reclamation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReclamationRepository extends JpaRepository<Reclamation, Long> {

    Page<Reclamation> findByResponsesCountGreaterThan(int size, Pageable pageable);

    Page<Reclamation> findByResponsesCountEquals(int size, Pageable pageable);

    Page<Reclamation> findByAbsenceEtudiantId(Long etudiantId, Pageable pageable);

}
