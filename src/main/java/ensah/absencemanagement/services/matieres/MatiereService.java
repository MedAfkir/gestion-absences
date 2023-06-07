package ensah.absencemanagement.services.matieres;

import ensah.absencemanagement.dtos.etudiants.EtudiantDTO;
import ensah.absencemanagement.dtos.etudiants.EtudiantMapper;
import ensah.absencemanagement.dtos.matieres.MatiereAddRequest;
import ensah.absencemanagement.dtos.matieres.MatiereDTO;
import ensah.absencemanagement.dtos.matieres.MatiereMapper;
import ensah.absencemanagement.dtos.matieres.MatiereUpdateRequest;
import ensah.absencemanagement.exceptions.not_found.MatiereNotFoundException;
import ensah.absencemanagement.exceptions.not_found.ModuleNotFoundException;
import ensah.absencemanagement.models.etudiants.Etudiant;
import ensah.absencemanagement.models.matieres.Matiere;
import ensah.absencemanagement.models.modules.Module;
import ensah.absencemanagement.models.niveaux.Niveau;
import ensah.absencemanagement.repositories.EtudiantRepository;
import ensah.absencemanagement.repositories.MatiereRepository;
import ensah.absencemanagement.repositories.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class MatiereService {

    private final MatiereRepository matiereRepository;
    private final MatiereMapper matiereMapper;

    private final EtudiantRepository etudiantRepository;
    private final EtudiantMapper etudiantMapper;

    private final ModuleRepository moduleRepository;

    @Autowired
    public MatiereService(
            MatiereRepository matiereRepository,
            MatiereMapper matiereMapper,
            EtudiantRepository etudiantRepository,
            EtudiantMapper etudiantMapper,
            ModuleRepository moduleRepository
    ) {
        this.matiereRepository = matiereRepository;
        this.matiereMapper = matiereMapper;
        this.etudiantRepository = etudiantRepository;
        this.etudiantMapper = etudiantMapper;
        this.moduleRepository = moduleRepository;
    }

    public List<MatiereDTO> getAllMatieres() {
        return matiereMapper.map(matiereRepository.findAll());
    }

    public Page<MatiereDTO> getAllMatieres(int page) {
        return matiereRepository.findAll(PageRequest.of(page, 10, Sort.by(Sort.Order.desc("createdAt"))))
                .map(matiereMapper::map);
    }

    public List<MatiereDTO> getMatieresByModule(Module module) {
        return matiereMapper.map((matiereRepository.findByModule(module)));
    }

    public MatiereDTO getMatiereById(Long matiereId) {
        return matiereMapper.map(findMatiereById(matiereId));
    }

    public Long createMatiere(MatiereAddRequest request) {
        Matiere matiere = matiereMapper.createMatiere(request);
        Matiere savedMatiere = matiereRepository.save(matiere);
        return savedMatiere.getId();
    }

    public void updateMatiere(Long matiereId, MatiereUpdateRequest request) {
        Matiere matiere = findMatiereById(matiereId);
        matiereMapper.updateMatiere(request, matiere);

        // Modifier le module si le nouveau module different du module ancien
        if (request.getModuleId() != null && (matiere.getModule() == null || !request.getModuleId().equals(matiere.getModule().getId()))) {
            Module module = moduleRepository.findById(request.getModuleId())
                    .orElseThrow(() -> new ModuleNotFoundException(request.getModuleId()));
            matiere.setModule(module);
        }

        matiereRepository.save(matiere);
    }

    /**
     * @author Mohamed AFKIR
     */
    public List<EtudiantDTO> getMatiereEtudiants(Long matiereId) {
        Matiere matiere = findMatiereById(matiereId);
        Niveau niveau = matiere.getModule() != null
                ? matiere.getModule().getNiveau()
                : null;

        if (niveau == null) return null;

        List<Etudiant> etudiants = etudiantRepository.findByDeleted(false)
                .stream()
                .filter(etudiant -> Objects.equals(niveau, etudiant.getNiveau()))
                .toList();
        return etudiantMapper.map(etudiants);
    }

    /**
     * @author Mohamed AFKIR
     */
    public List<EtudiantDTO> getMatiereEtudiants(Long matiereId, List<String> codesMassar) {
        List<EtudiantDTO> etudiants = getMatiereEtudiants(matiereId);

        if (etudiants == null) return null;

        return etudiants.stream()
                .filter(etudiant -> codesMassar.contains(etudiant.getCne()))
                .toList();
    }

    private Matiere findMatiereById(Long matiereId) {
        return matiereRepository.findById(matiereId)
                .orElseThrow(() -> new MatiereNotFoundException(matiereId));
    }

    public void deleteMatiere(Long matiereId) {
        Matiere matiere = findMatiereById(matiereId);
        matiereRepository.delete(matiere);
    }
}
