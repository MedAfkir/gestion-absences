package ensah.absencemanagement.models.filieres;

import ensah.absencemanagement.models.niveaux.Niveau;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "filieres")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Filiere {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String alias;

    @OneToMany(mappedBy = "filiere")
    private List<Niveau> niveaux;

    private Date accreditation;

    private Date finAccreditation;

    private Date createdAt;

    private Date updatedAt;

}
