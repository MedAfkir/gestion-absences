package ensah.absencemanagement.models.niveaux;

import ensah.absencemanagement.models.filieres.Filiere;
import ensah.absencemanagement.models.modules.Module;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "niveaux")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Niveau {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String alias;

    @ManyToOne
    @JoinColumn(name = "filiere_id")
    @ToString.Exclude
    private Filiere filiere;

    @OneToMany(mappedBy = "niveau")
    @ToString.Exclude
    private List<Module> modules;

    private Date createdAt;

    private Date updatedAt;

}
