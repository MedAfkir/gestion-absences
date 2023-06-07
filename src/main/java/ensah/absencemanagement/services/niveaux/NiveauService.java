package ensah.absencemanagement.services.niveaux;

import ensah.absencemanagement.dtos.niveaux.NiveauAddRequest;
import ensah.absencemanagement.dtos.niveaux.NiveauDTO;
import ensah.absencemanagement.dtos.niveaux.NiveauMapper;
import ensah.absencemanagement.dtos.niveaux.NiveauUpdateRequest;
import ensah.absencemanagement.exceptions.not_found.FiliereNotFoundException;
import ensah.absencemanagement.exceptions.not_found.NiveauNotFoundException;
import ensah.absencemanagement.models.filieres.Filiere;
import ensah.absencemanagement.models.modules.Module;
import ensah.absencemanagement.models.niveaux.Niveau;
import ensah.absencemanagement.repositories.FiliereRepository;
import ensah.absencemanagement.repositories.ModuleRepository;
import ensah.absencemanagement.repositories.NiveauRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class NiveauService {

    private final NiveauRepository niveauRepository;
    private final NiveauMapper niveauMapper;

    private final FiliereRepository filiereRepository;
    private final ModuleRepository moduleRepository;

    @Autowired
    public NiveauService(NiveauRepository niveauRepository, NiveauMapper niveauMapper, FiliereRepository filiereRepository, ModuleRepository moduleRepository) {
        this.niveauRepository = niveauRepository;
        this.niveauMapper = niveauMapper;
        this.filiereRepository = filiereRepository;
        this.moduleRepository = moduleRepository;
    }

    public List<NiveauDTO> getAllNiveaux() {
        return niveauMapper.map(niveauRepository.findAll());
    }

    public Page<NiveauDTO> getAllNiveaux(int page) {
        return niveauRepository
                .findAll(PageRequest.of(page, 10, Sort.by(Sort.Order.desc("createdAt"))))
                .map(niveauMapper::map);
    }

    public NiveauDTO getNiveauById(Long niveauId) {
        return niveauMapper.map(findNiveauById(niveauId));
    }

    public List<NiveauDTO> getNiveauxByFiliere(Filiere filiere) {
        return niveauMapper.map(niveauRepository.findByFiliere(filiere));
    }

    public Long createNiveau(NiveauAddRequest request) {
        Niveau niveau = niveauMapper.createNiveau(request);
        Niveau savedNiveau = niveauRepository.save(niveau);
        return savedNiveau.getId();
    }

    public void updateNiveauById(Long niveauId, NiveauUpdateRequest request) {
        Niveau niveau = findNiveauById(niveauId);
        niveauMapper.updateNiveau(request, niveau);

        if (request.getFiliereId() != null && (niveau.getFiliere() == null || !request.getFiliereId().equals(niveau.getFiliere().getId()))) {
            // TODO define FiliereNotFoundException
            Filiere filiere = filiereRepository.findById(request.getFiliereId())
                    .orElseThrow(() -> new FiliereNotFoundException(request.getFiliereId()));
            niveau.setFiliere(filiere);
        }

        niveauRepository.save(niveau);
    }

    public void updateNiveauModulesById(Long niveauId, List<Long> modulesIds) {
        Niveau niveau = findNiveauById(niveauId);

        if (niveau.getModules().size() > 0) {
            List<Module> niveauModules = niveau.getModules();
            niveauModules.forEach(m -> m.setNiveau(null));
            moduleRepository.saveAll(niveauModules);
        }

        if (modulesIds == null) return;

        if (modulesIds.size() > 0) {
            List<Module> modules = moduleRepository.findAllById(modulesIds);
            modules.forEach(m -> m.setNiveau(niveau));
            moduleRepository.saveAll(modules);
        }
    }

    public void deleteNiveauById(Long niveauId) {
        Niveau niveau = findNiveauById(niveauId);
        niveauRepository.delete(niveau);
    }

    private Niveau findNiveauById(Long niveauId) {
        return niveauRepository.findById(niveauId)
                .orElseThrow(() -> new NiveauNotFoundException(niveauId));
    }
}
