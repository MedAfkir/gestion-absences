package ensah.absencemanagement.models.types_seance;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "types_seances")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class TypeSeance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String alias;

    private Date createdAt;

    private Date updatedAt;

}
