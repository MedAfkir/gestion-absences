package ensah.absencemanagement.controllers.files;

import ensah.absencemanagement.dtos.files.FileDTO;
import ensah.absencemanagement.services.files.FileService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/files")
public class FileController {

    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * Download File
     * <a href="https://scribe.rip/swlh/streaming-data-with-spring-boot-restful-web-service-87522511c071">Source</a>
     */
    @ResponseBody
    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadFile(
            @PathVariable Long id,
            final HttpServletResponse response
    ) {
        FileDTO file = fileService.getFileById(id);
        response.setContentType(file.getType());
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment;filename=\"%s\"", file.getName()));
        return ResponseEntity.ok().body(file.getData());
    }

}
