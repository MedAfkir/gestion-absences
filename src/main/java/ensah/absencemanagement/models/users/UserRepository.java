package ensah.absencemanagement.models.users;

import ensah.absencemanagement.models.etudiants.Etudiant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByIdAndDeleted(Long id, boolean deleted);

    List<User> findByDeleted(boolean deleted);

    Page<User> findByDeleted(boolean deleted, Pageable pageable);

    @Query("select e from User e where e.deleted = false " +
            "and (lower(e.lastnameFr) like lower(concat('%', :name, '%')) " +
            "or lower(e.firstnameFr) like lower(concat('%', :name, '%')))")
    Page<User> search(@Param("name") String name, Pageable pageable);

}
