package ensah.absencemanagement.models.logging_events;

import ensah.absencemanagement.models.users.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "logging_events")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class LoggingEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String details;

    @Column(nullable = false)
    private String ipAddress;

    private Date createdAt;

    @Column(nullable = false)
    private Type type;

    @ManyToOne
    @ToString.Exclude
    private User user;

    public enum Type {
        CHANGE("Changement de données"),
        VISIT("Visite d'une page");

        private final String name;

        Type(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

}
