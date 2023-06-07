package ensah.absencemanagement.services.files;

import ensah.absencemanagement.dtos.files.FileDTO;
import ensah.absencemanagement.dtos.files.FileMapper;
import ensah.absencemanagement.exceptions.not_found.FileNotFoundException;
import ensah.absencemanagement.models.images.File;
import ensah.absencemanagement.repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FileService {

    private final FileRepository fileRepository;
    private final FileMapper fileMapper;

    @Autowired
    public FileService(FileRepository fileRepository, FileMapper fileMapper) {
        this.fileRepository = fileRepository;
        this.fileMapper = fileMapper;
    }

    public FileDTO getFileById(Long fileId) {
        return fileMapper.map(findFileById(fileId));
    }

    private File findFileById(Long fileId) {
        return fileRepository.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException(fileId));
    }

}
