package ensah.absencemanagement.repositories;

import ensah.absencemanagement.models.logging_events.LoggingEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoggingEventRepository extends JpaRepository<LoggingEvent, Long> {

    Page<LoggingEvent> findByUserIdAndType(Long id, LoggingEvent.Type type, Pageable pageable);

    Page<LoggingEvent> findByType(LoggingEvent.Type type, Pageable pageable);

}
