package ensah.absencemanagement.repositories;

import ensah.absencemanagement.models.types_seance.TypeSeance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeSeanceRepostiory extends JpaRepository<TypeSeance, Long> {
}
