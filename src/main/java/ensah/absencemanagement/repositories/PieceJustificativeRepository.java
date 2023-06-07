package ensah.absencemanagement.repositories;

import ensah.absencemanagement.models.pieces_justifications.PieceJustificative;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PieceJustificativeRepository extends JpaRepository<PieceJustificative, Long> {
}
