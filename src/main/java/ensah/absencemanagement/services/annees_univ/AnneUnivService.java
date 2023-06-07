package ensah.absencemanagement.services.annees_univ;

import ensah.absencemanagement.dtos.annees_univ.AnneeUnivDTO;
import ensah.absencemanagement.dtos.annees_univ.AnneeUnivMapper;
import ensah.absencemanagement.dtos.annees_univ.AnneeUnivRequest;
import ensah.absencemanagement.exceptions.not_found.AnneeUnivNotFoundException;
import ensah.absencemanagement.models.annees_univ.AnneeUniv;
import ensah.absencemanagement.repositories.AnneeUnivRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AnneUnivService {

    private final AnneeUnivRepository anneeUnivRepository;
    private final AnneeUnivMapper anneeUnivMapper;

    @Autowired
    public AnneUnivService(AnneeUnivRepository anneeUnivRepository, AnneeUnivMapper anneeUnivMapper) {
        this.anneeUnivRepository = anneeUnivRepository;
        this.anneeUnivMapper = anneeUnivMapper;
    }

    public List<AnneeUnivDTO> getAllAnneesUniv() {
        return anneeUnivMapper.map(anneeUnivRepository.findAll());
    }

    public AnneeUnivDTO getAnneeUnivById(Long anneeUnivId) {
        return anneeUnivMapper.map(findAnneeUnivById(anneeUnivId));
    }

    public Long createAnneesUniv(AnneeUnivRequest request) {
        AnneeUniv anneeUniv = anneeUnivMapper.createAnneeUniv(request);
        AnneeUniv savedAnneeUniv = anneeUnivRepository.save(anneeUniv);
        return savedAnneeUniv.getId();
    }

    public void updateAnneeUnivById(Long anneUnivId, AnneeUnivRequest request) {
        AnneeUniv anneeUniv = findAnneeUnivById(anneUnivId);

        if (request.isCurrent()) {
            List<AnneeUniv> annees = anneeUnivRepository.findAll();
            annees.forEach(annee -> annee.setCurrent(false));
            anneeUnivRepository.saveAll(annees);
        }

        anneeUnivMapper.updateAnneeUniv(request, anneeUniv);
        anneeUnivRepository.save(anneeUniv);
    }

    public void deleteAnneeUnivById(Long niveauId) {
        AnneeUniv anneeUniv = findAnneeUnivById(niveauId);
        anneeUnivRepository.delete(anneeUniv);
    }

    private AnneeUniv findAnneeUnivById(Long niveauId) {
        return anneeUnivRepository.findById(niveauId)
                .orElseThrow(() -> new AnneeUnivNotFoundException(niveauId));
    }

}
