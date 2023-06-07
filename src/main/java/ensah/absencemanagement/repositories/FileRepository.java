package ensah.absencemanagement.repositories;

import ensah.absencemanagement.models.images.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
}
