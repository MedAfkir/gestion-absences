package ensah.absencemanagement.repositories;

import ensah.absencemanagement.models.etudiants.Etudiant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {

    @Query("select e from Etudiant e where e.deleted = false " +
            "and lower(e.cne) like lower(concat('%', :cne, '%')) " +
            "and (lower(e.lastnameFr) like lower(concat('%', :name, '%')) " +
            "or lower(e.firstnameFr) like lower(concat('%', :name, '%')))")
    Page<Etudiant> search(@Param("name") String name, @Param("cne") String cne, Pageable pageable);

    @Query("select e from Etudiant e where e.deleted = false " +
            "and (lower(e.lastnameFr) like lower(concat('%', :name, '%')) " +
            "or lower(e.firstnameFr) like lower(concat('%', :name, '%')))")
    Page<Etudiant> searchByName(@Param("name") String name, Pageable pageable);

    @Query("select e from Etudiant e where e.deleted = false and lower(e.cne) like lower(concat('%', :cne, '%'))")
    Page<Etudiant> searchByCne(@Param("cne") String cne, Pageable pageable);

    Page<Etudiant> findByDeleted(boolean deleted, Pageable pageable);

    List<Etudiant> findByDeleted(boolean deleted);

    Optional<Etudiant> findByIdAndDeleted(Long id, boolean deleted);

    List<Etudiant> findByIdInAndDeleted(List<Long> ids, boolean deleted);

}
