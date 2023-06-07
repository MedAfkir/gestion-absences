package ensah.absencemanagement.repositories;

import ensah.absencemanagement.models.matieres.Matiere;
import ensah.absencemanagement.models.modules.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatiereRepository extends JpaRepository<Matiere, Long> {
    List<Matiere> findByModule(Module module);
}
