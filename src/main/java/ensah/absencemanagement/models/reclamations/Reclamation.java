package ensah.absencemanagement.models.reclamations;

import ensah.absencemanagement.models.absence.Absence;
import ensah.absencemanagement.models.messages.Message;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "reclamations")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
@ToString
public class Reclamation extends Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @ToString.Exclude
    private Absence absence;

    @OneToMany
    @ToString.Exclude
    private List<Message> responses;

    @Transient
    private int responsesCount;

    public int getResponsesCount() {
        return getResponses().size();
    }

}
