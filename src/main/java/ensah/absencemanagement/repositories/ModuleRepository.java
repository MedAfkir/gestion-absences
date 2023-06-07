package ensah.absencemanagement.repositories;

import ensah.absencemanagement.models.modules.Module;
import ensah.absencemanagement.models.niveaux.Niveau;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {
    List<Module> findByNiveauIsNull();
    List<Module> findByNiveau(Niveau niveau);
}
