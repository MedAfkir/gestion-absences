package ensah.absencemanagement.models.images;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "files")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(columnDefinition = "longblob")
    @ToString.Exclude
    private byte[] data;

    private String type;

    private String name;

    private Date createdAt;

}
