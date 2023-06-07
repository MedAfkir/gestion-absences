package ensah.absencemanagement.repositories;

import ensah.absencemanagement.models.filieres.Filiere;
import ensah.absencemanagement.models.niveaux.Niveau;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NiveauRepository extends JpaRepository<Niveau, Long> {
    List<Niveau> findByFiliere(Filiere filiere);
}
