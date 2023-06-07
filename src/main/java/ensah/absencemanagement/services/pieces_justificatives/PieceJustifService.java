package ensah.absencemanagement.services.pieces_justificatives;

import ensah.absencemanagement.dtos.files.FileMapper;
import ensah.absencemanagement.dtos.pieces_justificatives.PieceJusitifcativeRequest;
import ensah.absencemanagement.dtos.pieces_justificatives.PieceJustifMapper;
import ensah.absencemanagement.dtos.pieces_justificatives.PieceJustificativeDTO;
import ensah.absencemanagement.exceptions.not_found.AbsenceNotFoundException;
import ensah.absencemanagement.exceptions.not_found.PieceJustificativeNotFoundException;
import ensah.absencemanagement.models.absence.Absence;
import ensah.absencemanagement.models.images.File;
import ensah.absencemanagement.models.pieces_justifications.PieceJustificative;
import ensah.absencemanagement.repositories.AbsenceRepository;
import ensah.absencemanagement.repositories.FileRepository;
import ensah.absencemanagement.repositories.PieceJustificativeRepository;
import ensah.absencemanagement.utils.SessionUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static ensah.absencemanagement.models.users.User.Role.ENSEIGNANT;
import static ensah.absencemanagement.models.users.User.Role.ETUDIANT;

@Service
@Transactional
public class PieceJustifService {

    private final AbsenceRepository absenceRepository;

    private final PieceJustificativeRepository repository;
    private final PieceJustifMapper pieceJustifMapper;

    private final FileRepository fileRepository;
    private final FileMapper fileMapper;

    @Autowired
    public PieceJustifService(
            AbsenceRepository absenceRepository,
            PieceJustificativeRepository repository,
            PieceJustifMapper pieceJustifMapper,
            FileRepository fileRepository,
            FileMapper fileMapper
    ) {
        this.absenceRepository = absenceRepository;
        this.repository = repository;
        this.pieceJustifMapper = pieceJustifMapper;
        this.fileRepository = fileRepository;
        this.fileMapper = fileMapper;
    }

    public Page<PieceJustificativeDTO> getPiecesJusitificatives(int page) {
        return repository.findAll(PageRequest.of(page, 10))
                .map(pieceJustifMapper::map);
    }

    public PieceJustificativeDTO getPieceJusitificativeById(SessionUser user, Long pieceId) {
        PieceJustificative piece = findPieceJustificativeById(pieceId);
        if (!hasAccessToReadPiece(user, piece)) {
            throw new AccessDeniedException("Vous n'avez pas accés à cette ressource");
        }

        return pieceJustifMapper.map(piece);
    }

    public Long createPieceJustificative(PieceJusitifcativeRequest request) throws IOException {
        Absence absence = findAbsenceById(request.getAbsenceId());
        File file = fileMapper.map(request.getSource());
        File savedFile = fileRepository.save(file);

        PieceJustificative piece = PieceJustificative.builder()
                .source(savedFile)
                .text(request.getText())
                .absence(absence)
                .createdAt(new Date())
                .build();

        PieceJustificative savedPiece = repository.save(piece);
        return savedPiece.getId();
    }

    public void createPiecesJustificatives(
            Long absenceId,
            String intitule,
            List<MultipartFile> files,
            Absence.AbsenceEtat state
    ) {
        Absence absence = findAbsenceById(absenceId);
        Absence.AbsenceEtat pieceState = getAbsenceState(state);
        List<File> pieces = files.stream()
                .map(f -> {
                    try {
                        return fileMapper.map(f);
                    } catch (IOException e) {
                        return null;
                    }
                }).filter(Objects::nonNull)
                .toList();

        List<File> savedPiecesFiles = fileRepository.saveAll(pieces);
        List<PieceJustificative> pieceJustificatives = savedPiecesFiles.stream()
                .map(piece -> PieceJustificative.builder()
                        .source(piece)
                        .text(intitule)
                        .state(pieceState)
                        .absence(absence)
                        .createdAt(new Date())
                        .build())
                .toList();
        repository.saveAll(pieceJustificatives);
    }

    public void updatePieceJustificativeState(Long pieceId, Absence.AbsenceEtat state) {
        PieceJustificative piece = findPieceJustificativeById(pieceId);
        piece.setState(state);
        if (state.equals(Absence.AbsenceEtat.JUSTIFIEE)) {
            piece.getAbsence().setEtat(Absence.AbsenceEtat.JUSTIFIEE);
        }
        repository.save(piece);
    }

    private Absence.AbsenceEtat getAbsenceState(Absence.AbsenceEtat state) {
        return state == null ? Absence.AbsenceEtat.NON_JUSTIFIEE : state;
    }

    private PieceJustificative findPieceJustificativeById(Long pieceId) {
        return repository.findById(pieceId)
                .orElseThrow(() -> new PieceJustificativeNotFoundException(pieceId));
    }

    private Absence findAbsenceById(Long absenceId) {
        return absenceRepository.findById(absenceId)
                .orElseThrow(() -> new AbsenceNotFoundException(absenceId));
    }

    private boolean hasAccessToReadPiece(SessionUser user, PieceJustificative piece) {
        Long etudiantId = piece.getAbsence().getEtudiant().getId();
        Long enseignantId = piece.getAbsence().getEnseignant().getId();

        return !((user.getRole().equals(ETUDIANT) && !etudiantId.equals(user.getId())) ||
                (user.getRole().equals(ENSEIGNANT) && !enseignantId.equals(user.getId())));
    }
}
