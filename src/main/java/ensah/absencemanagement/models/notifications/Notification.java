package ensah.absencemanagement.models.notifications;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "notifications")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String titre;

    @Column(nullable = false)
    private String texte;

    private Date createdAt;

    private Date updatedAt;

    @OneToMany(mappedBy = "notification")
    private Collection<NotificationTarget> targets;

}


