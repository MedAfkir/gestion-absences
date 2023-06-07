package ensah.absencemanagement.models.modules;

import ensah.absencemanagement.models.matieres.Matiere;
import ensah.absencemanagement.models.niveaux.Niveau;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "modules")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Module {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String alias;

    @ManyToOne
    @ToString.Exclude
    private Niveau niveau;

    @OneToMany(mappedBy = "module")
    @ToString.Exclude
    private List<Matiere> matieres;

    private Date createdAt;

    private Date updatedAt;

}
