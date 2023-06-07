package ensah.absencemanagement.repositories;

import ensah.absencemanagement.models.enseignants.Enseignant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnseignantRepository extends JpaRepository<Enseignant, Long> {

    @Query("select e from Enseignant e where e.deleted = false " +
            "and lower(e.cin) like lower(concat('%', :cin, '%')) " +
            "and (lower(e.lastnameFr) like lower(concat('%', :name, '%')) " +
            "or lower(e.firstnameFr) like lower(concat('%', :name, '%')))")
    Page<Enseignant> search(@Param("name") String name, @Param("cin") String cin, Pageable pageable);

    @Query("select e from Enseignant e where e.deleted = false " +
            "and (lower(e.lastnameFr) like lower(concat('%', :name, '%')) " +
            "or lower(e.firstnameFr) like lower(concat('%', :name, '%')))")
    Page<Enseignant> searchByName(@Param("name") String name, Pageable pageable);

    @Query("select e from Enseignant e where e.deleted = false and lower(e.cin) like lower(concat('%', :cin, '%'))")
    Page<Enseignant> searchByCin(@Param("cin") String cin, Pageable pageable);

    Page<Enseignant> findByDeleted(boolean deleted, Pageable pageable);

    List<Enseignant> findByDeleted(boolean deleted);

    Optional<Enseignant> findByIdAndDeleted(Long id, boolean deleted);

}
