package ensah.absencemanagement.models.annees_univ;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "annees_universitaires")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AnneeUniv {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private boolean current;

    @Column(name = "startYear", nullable = false)
    private Date start;

    @Column(name = "endYear", nullable = false)
    private Date end;

    private Date createdAt;

    private Date updatedAt;

}
