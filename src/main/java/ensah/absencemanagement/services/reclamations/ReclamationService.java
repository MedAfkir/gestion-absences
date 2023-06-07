package ensah.absencemanagement.services.reclamations;

import ensah.absencemanagement.dtos.messages.MessageDTO;
import ensah.absencemanagement.dtos.messages.MessageMapper;
import ensah.absencemanagement.dtos.messages.MessageRequest;
import ensah.absencemanagement.dtos.reclamations.ReclamationDTO;
import ensah.absencemanagement.dtos.reclamations.ReclamationMapper;
import ensah.absencemanagement.dtos.reclamations.ReclamationRequest;
import ensah.absencemanagement.exceptions.UnsupportedActionException;
import ensah.absencemanagement.exceptions.not_found.AbsenceNotFoundException;
import ensah.absencemanagement.models.absence.Absence;
import ensah.absencemanagement.repositories.AbsenceRepository;
import ensah.absencemanagement.models.messages.Message;
import ensah.absencemanagement.repositories.MessageRepository;
import ensah.absencemanagement.models.reclamations.Reclamation;
import ensah.absencemanagement.repositories.ReclamationRepository;
import ensah.absencemanagement.utils.SessionUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static ensah.absencemanagement.models.users.User.Role.*;

@Service
@Transactional
public class ReclamationService {

    private final ReclamationRepository repository;
    private final ReclamationMapper mapper;
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final AbsenceRepository absenceRepository;

    @Autowired
    public ReclamationService(
            ReclamationRepository repository,
            ReclamationMapper mapper,
            MessageRepository messageRepository,
            MessageMapper messageMapper,
            AbsenceRepository absenceRepository
    ) {
        this.repository = repository;
        this.mapper = mapper;
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
        this.absenceRepository = absenceRepository;
    }

    public Page<ReclamationDTO> getReclamations(SessionUser user, int page) {
        switch (user.getRole()) {
            case ETUDIANT -> {
                return getReclamationsByStudent(user.getId(), page);
            }
            case CADRE_ADMINISTRATEUR -> {
                return getAllReclamations(page);
            }
            default -> throw new UnsupportedActionException(String.format(
                    "Utilisateur d'id %s n'a pas accès à cette resource",
                    user.getId()
            ));
        }
    }

    public Page<ReclamationDTO> getAllReclamations(int page) {
        return repository.findAll(PageRequest.of(
                page,
                10,
                Sort.by(Sort.Direction.DESC, "createdAt")
        )).map(mapper::map);
    }

    public Page<ReclamationDTO> getReclamationsByStudent(Long studentId, int pageIndex) {
        return repository.findByAbsenceEtudiantId(
                studentId,
                PageRequest.of(pageIndex, 10, Sort.by(Sort.Direction.DESC, "createdAt"))
        ).map(mapper::map);
    }

    public ReclamationDTO getReclamationById(SessionUser user, Long reclamationId) {
        Reclamation reclamation = findReclamationById(reclamationId);

        if (hasNotAccessToRead(user, reclamation)) {
            throw new UnsupportedActionException(String.format(
                    "Utilisateur d'id %s n'a pas accès à cette action",
                    user.getId()
            ));
        }

        return mapper.map(reclamation);
    }

    public List<MessageDTO> getReclamationReponses(SessionUser user, Long reclamationId) {
        Reclamation reclamation = findReclamationById(reclamationId);

        if (hasNotAccessToRead(user, reclamation)) {
            throw new UnsupportedActionException(String.format(
                    "Utilisateur d'id %s n'a pas accès à cette action",
                    user.getId()
            ));
        }

        List<Message> sortedResponses = reclamation.getResponses()
                .stream()
                .sorted((r1, r2) -> (int) (r2.getCreatedAt().getTime() - r1.getCreatedAt().getTime()))
                .toList();

        return messageMapper.map(sortedResponses);
    }

    public void createReclamation(Long authenticatedUserId, ReclamationRequest request) {
        Reclamation reclamation = mapper.createReclamation(request);

        Absence absence = absenceRepository.findById(request.getAbsenceId())
                .orElseThrow(() -> new AbsenceNotFoundException(request.getAbsenceId()));

        if (!authenticatedUserId.equals(absence.getEtudiant().getId())) {
            throw new UnsupportedActionException(String.format(
                    "Utilisateur d'id %s n'a pas accès à cette action",
                    authenticatedUserId
            ));
        }

        reclamation.setAbsence(absence);

        repository.save(reclamation);
    }

    public void answerReclamation(Long reclamationId, MessageRequest request) {
        Message message = messageRepository.save(messageMapper.createMessage(request));
        Reclamation reclamation = findReclamationById(reclamationId);
        reclamation.getResponses().add(message);
        repository.save(reclamation);
    }

    private Reclamation findReclamationById(Long reclamationId) {
        return repository.findById(reclamationId)
                .orElseThrow(RuntimeException::new);
    }

    private boolean hasNotAccessToRead(SessionUser user, Reclamation reclamation) {
        return List.of(ENSEIGNANT, SUPER_ADMIN).contains(user.getRole())
                || (user.getRole().equals(ETUDIANT)
                && !reclamation.getAbsence().getEtudiant().getId().equals(user.getId()));
    }
}
