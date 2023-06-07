package ensah.absencemanagement.services.etudiants;

import ensah.absencemanagement.dtos.etudiants.EtudiantDTO;
import ensah.absencemanagement.dtos.etudiants.EtudiantMapper;
import ensah.absencemanagement.dtos.users.UserRequest;
import ensah.absencemanagement.exceptions.not_found.EtudiantNotFoundException;
import ensah.absencemanagement.models.etudiants.Etudiant;
import ensah.absencemanagement.repositories.EtudiantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EtudiantService {

    private final EtudiantRepository etudiantRepository;
    private final EtudiantMapper etudiantMapper;

    @Autowired
    public EtudiantService(EtudiantRepository etudiantRepository, EtudiantMapper etudiantMapper) {
        this.etudiantRepository = etudiantRepository;
        this.etudiantMapper = etudiantMapper;
    }

    public Page<EtudiantDTO> getAllEtudiants(String name, String cne, int page) {
        Page<Etudiant> etudiants;
        if (name == null || name.isEmpty()) {
            if (cne == null || cne.isEmpty())
                etudiants = etudiantRepository.findByDeleted(false, getPage(page));
            else
                etudiants = etudiantRepository.searchByCne(cne, getPage(page));
        } else {
            if (cne == null || cne.isEmpty())
                etudiants = etudiantRepository.searchByName(name, getPage(page));
            else
                etudiants = etudiantRepository.search(name, cne, getPage(page));
        }
        return etudiants.map(etudiantMapper::mapWithDetails);
    }

    public EtudiantDTO getEtudiantById(Long etudiantId) {
        return etudiantMapper.mapWithDetails(findEtudiantById(etudiantId));
    }

    public void updateEtudiantInfosById(Long etudiantId, UserRequest request) {
        Etudiant etudiant = findEtudiantById(etudiantId);
        etudiantMapper.updateEtudiant(request, etudiant);
        etudiantRepository.save(etudiant);
    }

    public void deleteEtudiantById(Long etudiantId) {
        Etudiant etudiant = findEtudiantById(etudiantId);
        etudiant.setDeleted(true);
        etudiantRepository.save(etudiant);
    }

    private Etudiant findEtudiantById(Long etudiantId) {
        return etudiantRepository.findByIdAndDeleted(etudiantId, false)
                .orElseThrow(() -> new EtudiantNotFoundException(etudiantId));
    }

    private Pageable getPage(int page) {
        return PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
    }

}
