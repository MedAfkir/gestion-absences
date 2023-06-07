package ensah.absencemanagement.services.demandes_autorisation;

import ensah.absencemanagement.dtos.demandes_autorisation.DemandeAutorisationDTO;
import ensah.absencemanagement.dtos.demandes_autorisation.DemandeAutorisationMapper;
import ensah.absencemanagement.exceptions.UnsupportedActionException;
import ensah.absencemanagement.exceptions.not_found.DemandeAutorisationNotFoundException;
import ensah.absencemanagement.exceptions.not_found.EnseignantNotFoundException;
import ensah.absencemanagement.exceptions.not_found.EtudiantNotFoundException;
import ensah.absencemanagement.models.demandes_autorisation.DemandeAutorisation;
import ensah.absencemanagement.models.enseignants.Enseignant;
import ensah.absencemanagement.models.etudiants.Etudiant;
import ensah.absencemanagement.repositories.DemandeAutorisationRepository;
import ensah.absencemanagement.repositories.EnseignantRepository;
import ensah.absencemanagement.repositories.EtudiantRepository;
import ensah.absencemanagement.utils.SessionUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class DemandeAutorisationService {

    private final EnseignantRepository enseignantRepository;
    private final EtudiantRepository etudiantRepository;
    private final DemandeAutorisationRepository repository;
    private final DemandeAutorisationMapper mapper;

    @Autowired
    public DemandeAutorisationService(EnseignantRepository enseignantRepository, EtudiantRepository etudiantRepository, DemandeAutorisationRepository repository, DemandeAutorisationMapper mapper) {
        this.enseignantRepository = enseignantRepository;
        this.etudiantRepository = etudiantRepository;
        this.repository = repository;
        this.mapper = mapper;
    }

    public Page<DemandeAutorisationDTO> getDemandesAutorisationByAuthority(SessionUser user, int page) {
        switch (user.getRole()) {
            case ETUDIANT -> {
                return getDemandesAutorisationByEtudiant(user.getId(), page);
            }
            case ENSEIGNANT -> {
                return getDemandesAutorisationByEnseignant(user.getId(), page);
            }
            default -> {
                throw new AccessDeniedException("Vous n'avez les autorities necessaires pour accéder à cette ressource");
            }
        }
    }

    private Page<DemandeAutorisationDTO> getDemandesAutorisationByEtudiant(Long etudiantId, int page) {
        return repository.findByEtudiantId(etudiantId, getPage(page))
                .map(mapper::map);
    }

    private Page<DemandeAutorisationDTO> getDemandesAutorisationByEnseignant(Long ensiengnantId, int page) {
        return repository.findByEnseignantId(ensiengnantId, getPage(page))
                .map(mapper::map);
    }

    public DemandeAutorisationDTO getDemandeById(Long demandId) {
        return mapper.map(findDemandById(demandId));
    }

    public void createDemandeAutorisation(Long etudiantId, Long enseignantId, String content) {
        Enseignant enseignant = enseignantRepository.findByIdAndDeleted(enseignantId, false)
                .orElseThrow(() -> new EnseignantNotFoundException(enseignantId));

        if (!enseignant.isReceiveAuthorisationDemands()) {
            throw new UnsupportedActionException(String.format(
                    "Enseignant %s %s n'accepte pas ce genre de demandes",
                    enseignant.getFirstnameFr(),
                    enseignant.getLastnameFr().toUpperCase()
            ));
        }

        Etudiant etudiant = etudiantRepository.findByIdAndDeleted(etudiantId, false)
                .orElseThrow(() -> new EtudiantNotFoundException(etudiantId));

        DemandeAutorisation demandeAutorisation = DemandeAutorisation.builder()
                .content(content)
                .enseignant(enseignant)
                .etudiant(etudiant)
                .state(DemandeAutorisation.State.PENDING)
                .createdAt(new Date())
                .build();
        repository.save(demandeAutorisation);
    }

    public void updateDemandeAutorisationState(Long demandId, DemandeAutorisation.State state) {
        DemandeAutorisation demand = findDemandById(demandId);
        demand.setState(state);
        demand.setCreatedAt(new Date());
        repository.save(demand);
    }

    private DemandeAutorisation findDemandById(Long demandId) {
        return repository.findById(demandId)
                .orElseThrow(() -> new DemandeAutorisationNotFoundException(demandId));
    }

    private Pageable getPage(int page) {
        return PageRequest.of(page, 10, Sort.by(Sort.Order.desc("createdAt")));
    }

}
