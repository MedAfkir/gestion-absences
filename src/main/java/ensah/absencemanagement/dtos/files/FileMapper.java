package ensah.absencemanagement.dtos.files;

import ensah.absencemanagement.models.images.File;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Date;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FileMapper {

    FileDTO map(File file);

    ImageDTO toImage(File file);

    default File map(MultipartFile file) throws IOException {
        if (file.getContentType() == null
                || file.getContentType().equals("application/octet-stream")) {
            return null;
        }

        return File.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .data(file.getBytes())
                .createdAt(new Date())
                .build();
    }

    default String mapImageRessourceToString(byte[] src) {
        return Base64.getEncoder().encodeToString(src);
    }

}
