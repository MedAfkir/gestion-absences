package ensah.absencemanagement.services.filieres;

import ensah.absencemanagement.dtos.filieres.FiliereDTO;
import ensah.absencemanagement.dtos.filieres.FiliereMapper;
import ensah.absencemanagement.dtos.filieres.FiliereRequest;
import ensah.absencemanagement.exceptions.not_found.NiveauNotFoundException;
import ensah.absencemanagement.models.filieres.Filiere;
import ensah.absencemanagement.models.niveaux.Niveau;
import ensah.absencemanagement.repositories.FiliereRepository;
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
public class FiliereService {

    private final FiliereRepository filiereRepository;
    private final FiliereMapper filiereMapper;
    private final NiveauRepository niveauRepository;

    @Autowired
    public FiliereService(FiliereRepository filiereRepository, FiliereMapper filiereMapper, NiveauRepository niveauRepository) {
        this.filiereRepository = filiereRepository;
        this.filiereMapper = filiereMapper;
        this.niveauRepository = niveauRepository;
    }

    public List<FiliereDTO> getAllFilieres() {
        return filiereMapper.map(filiereRepository.findAll());
    }

    public Page<FiliereDTO> getAllFilieres(int page) {
        return filiereRepository.findAll(PageRequest.of(page, 10, Sort.by(Sort.Order.desc("createdAt"))))
                .map(filiereMapper::map);
    }

    public FiliereDTO getFiliereById(Long filiereId) {
        return filiereMapper.map(findFiliereById(filiereId));
    }

    public Long createFiliere(FiliereRequest request) {
        Filiere filiere = filiereMapper.createFiliere(request);
        Filiere savedFiliere = filiereRepository.save(filiere);
        return savedFiliere.getId();
    }

    public void updateFiliereInfosById(Long filiereId, FiliereRequest request) {
        Filiere filiere = findFiliereById(filiereId);
        filiereMapper.updateFiliere(request, filiere);
        filiereRepository.save(filiere);
    }

    public void updateFiliereNiveaux(Long filiereId, List<Long> niveauxIds) {
        Filiere filiere = findFiliereById(filiereId);

        if (filiere.getNiveaux().size() > 0) {
            List<Niveau> filiereNiveaux = filiere.getNiveaux();
            filiereNiveaux.forEach(n -> n.setFiliere(null));
            niveauRepository.saveAll(filiereNiveaux);
        }

        if (niveauxIds == null) return;

        if (niveauxIds.size() > 0) {
            List<Niveau> niveaux = niveauRepository.findAllById(niveauxIds);
            niveaux.forEach(n -> n.setFiliere(filiere));
            niveauRepository.saveAll(niveaux);
        }
    }

    public void deleteFiliereById(Long niveauId) {
        Filiere filiere = findFiliereById(niveauId);
        filiereRepository.delete(filiere);
    }

    private Filiere findFiliereById(Long niveauId) {
        return filiereRepository.findById(niveauId)
                .orElseThrow(() -> new NiveauNotFoundException(niveauId));
    }

}
