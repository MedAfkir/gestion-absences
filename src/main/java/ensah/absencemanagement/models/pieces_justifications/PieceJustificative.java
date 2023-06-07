package ensah.absencemanagement.models.pieces_justifications;

import ensah.absencemanagement.models.absence.Absence;
import ensah.absencemanagement.models.images.File;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "pieces_justificatives")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class PieceJustificative {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String text;

    @ManyToOne
    private Absence absence;

    @OneToOne
    @ToString.Exclude
    private File source;

    private Absence.AbsenceEtat state;

    private Date createdAt;

    private Date updatedAt;

}
