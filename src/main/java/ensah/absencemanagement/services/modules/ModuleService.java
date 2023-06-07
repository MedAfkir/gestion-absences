package ensah.absencemanagement.services.modules;

import ensah.absencemanagement.dtos.modules.ModuleAddRequest;
import ensah.absencemanagement.dtos.modules.ModuleDTO;
import ensah.absencemanagement.dtos.modules.ModuleMapper;
import ensah.absencemanagement.dtos.modules.ModuleUpdateRequest;
import ensah.absencemanagement.exceptions.not_found.ModuleNotFoundException;
import ensah.absencemanagement.exceptions.not_found.NiveauNotFoundException;
import ensah.absencemanagement.models.matieres.Matiere;
import ensah.absencemanagement.models.modules.Module;
import ensah.absencemanagement.models.niveaux.Niveau;
import ensah.absencemanagement.repositories.MatiereRepository;
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
public class ModuleService {

    private final ModuleMapper moduleMapper;
    private final ModuleRepository moduleRepository;
    private final NiveauRepository niveauRepository;
    private final MatiereRepository matiereRepository;

    @Autowired
    public ModuleService(
            ModuleMapper moduleMapper,
            ModuleRepository moduleRepository,
            NiveauRepository niveauRepository,
            MatiereRepository matiereRepository
    ) {
        this.moduleMapper = moduleMapper;
        this.moduleRepository = moduleRepository;
        this.niveauRepository = niveauRepository;
        this.matiereRepository = matiereRepository;
    }

    public List<ModuleDTO> getAllModules() {
        return moduleMapper.map(moduleRepository.findAll());
    }

    public Page<ModuleDTO> getAllModules(int page) {
        return moduleRepository.findAll(PageRequest.of(page, 10, Sort.by(Sort.Order.desc("createdAt"))))
                .map(moduleMapper::map);
    }

    public List<ModuleDTO> getModulesByNiveau(Niveau niveau) {
        return moduleMapper.map(moduleRepository.findByNiveau(niveau));
    }

    public ModuleDTO getModuleById(Long moduleId) {
        return moduleMapper.map(findModuleById(moduleId));
    }

    public Long createModule(ModuleAddRequest request) {
        Module module = moduleMapper.createModule(request);
        Module savedModule = moduleRepository.save(module);
        return savedModule.getId();
    }

    public void updateModuleInfos(Long moduleId, ModuleUpdateRequest request) {
        Module module = findModuleById(moduleId);
        moduleMapper.updateModule(request, module);

        if (request.getNiveauId() != null && (module.getNiveau() == null || !request.getNiveauId().equals(module.getNiveau().getId()))) {
            Niveau niveau = niveauRepository.findById(request.getNiveauId())
                    .orElseThrow(() -> new NiveauNotFoundException(request.getNiveauId()));
            module.setNiveau(niveau);
        }
        moduleRepository.save(module);
    }

    public void updateModuleMatieres(Long moduleId, List<Long> matieresIds) {
        Module module = findModuleById(moduleId);

        if (module.getMatieres().size() > 0) {
            module.getMatieres().forEach(m -> m.setModule(null));
            matiereRepository.saveAll(module.getMatieres());
        }

        if (matieresIds == null) return;

        if (matieresIds.size() > 0) {
            List<Matiere> matieres = matiereRepository.findAllById(matieresIds);
            matieres.forEach(m -> m.setModule(module));
            matiereRepository.saveAll(matieres);
        }
    }

    public void deleteModuleById(Long moduleId) {
        Module module = findModuleById(moduleId);
        moduleRepository.delete(module);
    }

    private Module findModuleById(Long moduleId) {
        return moduleRepository.findById(moduleId)
                .orElseThrow(() -> new ModuleNotFoundException(moduleId));
    }
}
