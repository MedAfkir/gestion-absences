package ensah.absencemanagement.dtos.files;

import lombok.*;

import java.util.Date;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileDTO {

    private Long id;

    private byte[] data;

    private String type;

    private String name;

    private Date createdAt;

}
