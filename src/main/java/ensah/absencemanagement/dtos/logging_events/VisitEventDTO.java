package ensah.absencemanagement.dtos.logging_events;

import ensah.absencemanagement.dtos.users.UserSummaryDTO;
import ensah.absencemanagement.models.logging_events.LoggingEvent;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class VisitEventDTO {

    private Long id;

    private String details;

    private String ipAddress;

    private LoggingEvent.Type type;

    private Date createdAt;

    private UserSummaryDTO user;

}
