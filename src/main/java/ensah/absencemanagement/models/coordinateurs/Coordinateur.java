package ensah.absencemanagement.models.coordinateurs;

import ensah.absencemanagement.models.enseignants.Enseignant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "coordinateurs")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Coordinateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer anneeAccreditation;

    private Integer anneeFinAccreditation;

    @OneToOne
    private Enseignant coordinateur;

    private Date createdAt;

    private Date updatedAt;

}
