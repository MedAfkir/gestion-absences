package ensah.absencemanagement.repositories;

import ensah.absencemanagement.models.annees_univ.AnneeUniv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnneeUnivRepository extends JpaRepository<AnneeUniv, Long> {
    Optional<AnneeUniv> findByCurrent(boolean current);
}
