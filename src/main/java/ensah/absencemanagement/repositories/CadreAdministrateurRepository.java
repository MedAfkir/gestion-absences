package ensah.absencemanagement.repositories;

import ensah.absencemanagement.models.cadres_admin.CadreAdministrateur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CadreAdministrateurRepository extends JpaRepository<CadreAdministrateur, Long> {

    @Query("select e from CadreAdministrateur e where e.deleted = false " +
            "and (lower(e.lastnameFr) like lower(concat('%', :name, '%')) " +
            "or lower(e.firstnameFr) like lower(concat('%', :name, '%')))")
    Page<CadreAdministrateur> search(@Param("name") String name, Pageable pageable);

    Page<CadreAdministrateur> findByDeleted(boolean deleted, Pageable pageable);

    Optional<CadreAdministrateur> findByIdAndDeleted(Long id, boolean deleted);

}
