package ensah.absencemanagement.dtos.files;

import lombok.*;

import java.util.Date;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImageDTO {

    private Long id;

    private String data;

    private String type;

    private String name;

    private Date createdAt;

}
