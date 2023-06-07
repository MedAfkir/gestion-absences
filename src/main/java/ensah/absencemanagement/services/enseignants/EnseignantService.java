package ensah.absencemanagement.services.enseignants;

import ensah.absencemanagement.dtos.enseignants.EnseignantDTO;
import ensah.absencemanagement.dtos.enseignants.EnseignantMapper;
import ensah.absencemanagement.dtos.users.UserRequest;
import ensah.absencemanagement.exceptions.not_found.EnseignantNotFoundException;
import ensah.absencemanagement.models.enseignants.Enseignant;
import ensah.absencemanagement.repositories.EnseignantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class EnseignantService {

    private final EnseignantRepository enseignantRepository;
    private final EnseignantMapper enseignantMapper;

    @Autowired
    public EnseignantService(EnseignantRepository enseignantRepository, EnseignantMapper enseignantMapper) {
        this.enseignantRepository = enseignantRepository;
        this.enseignantMapper = enseignantMapper;
    }

    public Page<EnseignantDTO> getAllEnseignants(String name, String cin, int page) {
        Page<Enseignant> enseignants;
        if (name == null || name.isEmpty()) {
            if (cin == null || cin.isEmpty())
                enseignants = enseignantRepository.findByDeleted(false, getPage(page));
            else
                enseignants = enseignantRepository.searchByCin(cin, getPage(page));
        } else {
            if (cin == null || cin.isEmpty())
                enseignants = enseignantRepository.searchByName(name, getPage(page));
            else
                enseignants = enseignantRepository.search(name, cin, getPage(page));
        }
        return enseignants.map(enseignantMapper::mapWithDetails);
    }

    public Page<EnseignantDTO> getAllEnseignants(int page) {
        return enseignantRepository.findByDeleted(false, getPage(page))
                .map(enseignantMapper::mapWithDetails);
    }

    public List<EnseignantDTO> getAllEnseignants() {
        return enseignantMapper.mapWithDetails(enseignantRepository.findByDeleted(false));
    }

    public EnseignantDTO getEnseignantById(Long enseignantId) {
        return enseignantMapper.mapWithDetails(findEnseignantById(enseignantId));
    }

    public void updateReceivingAuthorisationDemands(Long enseignantId, boolean isReceiving) {
        Enseignant enseignant = findEnseignantById(enseignantId);
        enseignant.setReceiveAuthorisationDemands(isReceiving);
        enseignant.setUpdatedAt(new Date());
        enseignantRepository.save(enseignant);
    }

    public void updateEnseignant(Long enseignantId, UserRequest userRequest) {
        Enseignant enseignant = findEnseignantById(enseignantId);
        enseignantMapper.updateEnseignant(userRequest, enseignant);
        enseignantRepository.save(enseignant);
    }

    public void deleteEnseignant(Long enseignantId) {
        Enseignant enseignant = findEnseignantById(enseignantId);
        enseignant.setDeleted(true);
        enseignantRepository.save(enseignant);
    }

    private Enseignant findEnseignantById(Long enseignantId) {
        return enseignantRepository.findByIdAndDeleted(enseignantId, false)
                .orElseThrow(() -> new EnseignantNotFoundException(enseignantId));
    }

    private Pageable getPage(int page) {
        return PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
    }

}
