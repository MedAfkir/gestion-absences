package ensah.absencemanagement.models.matieres;

import ensah.absencemanagement.models.modules.Module;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "matieres")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Matiere {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String alias;

    @ManyToOne
    @ToString.Exclude
    private Module module;

    private Date createdAt;

    private Date updatedAt;

}
