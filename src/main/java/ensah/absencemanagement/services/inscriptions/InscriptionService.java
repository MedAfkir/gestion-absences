package ensah.absencemanagement.services.inscriptions;

import ensah.absencemanagement.dtos.inscriptions.InscriptionDTO;
import ensah.absencemanagement.dtos.inscriptions.InscriptionMapper;
import ensah.absencemanagement.exceptions.UnsupportedActionException;
import ensah.absencemanagement.exceptions.not_found.EtudiantNotFoundException;
import ensah.absencemanagement.exceptions.not_found.InscriptionNotFoundException;
import ensah.absencemanagement.exceptions.not_found.NiveauNotFoundException;
import ensah.absencemanagement.models.annees_univ.AnneeUniv;
import ensah.absencemanagement.models.etudiants.Etudiant;
import ensah.absencemanagement.models.inscriptions.Inscription;
import ensah.absencemanagement.models.niveaux.Niveau;
import ensah.absencemanagement.repositories.AnneeUnivRepository;
import ensah.absencemanagement.repositories.EtudiantRepository;
import ensah.absencemanagement.repositories.InscriptionRepository;
import ensah.absencemanagement.repositories.NiveauRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static ensah.absencemanagement.models.inscriptions.Inscription.InscriptionState.*;

@Service
@Transactional
public class InscriptionService {

    private final NiveauRepository niveauRepository;
    private final EtudiantRepository etudiantRepository;
    private final AnneeUnivRepository anneeUnivRepository;
    private final InscriptionRepository inscriptionRepository;
    private final InscriptionMapper inscriptionMapper;

    @Autowired
    public InscriptionService(NiveauRepository niveauRepository, EtudiantRepository etudiantRepository, AnneeUnivRepository anneeUnivRepository, InscriptionRepository inscriptionRepository, InscriptionMapper inscriptionMapper) {
        this.niveauRepository = niveauRepository;
        this.etudiantRepository = etudiantRepository;
        this.anneeUnivRepository = anneeUnivRepository;
        this.inscriptionRepository = inscriptionRepository;
        this.inscriptionMapper = inscriptionMapper;
    }

    public Page<InscriptionDTO> getAllInscriptions(int page) {
        return inscriptionRepository
                .findAll(PageRequest.of(page, 10, Sort.by(Sort.Order.desc("createdAt"))))
                .map(inscriptionMapper::map);
    }

    public InscriptionDTO getInscriptionById(Long inscriptionId) {
        return inscriptionMapper.map(findInscriptionById(inscriptionId));
    }

    public Page<InscriptionDTO> getInscriptionsByStudentId(Long etudiantId, int page) {
        Page<Inscription> inscriptions = inscriptionRepository.findByEtudiantId(
                etudiantId, PageRequest.of(page, 10, Sort.by(Sort.Order.desc("createdAt")))
        );
        return inscriptions.map(inscriptionMapper::map);
    }

    public List<InscriptionDTO> getInscriptionsByNiveauIdAndState(Long niveauId, Inscription.InscriptionState state) {
        List<Inscription> inscriptions = inscriptionRepository.findByNiveauIdAndEtat(niveauId, state);
        return inscriptionMapper.map(inscriptions);
    }

    public void updateInscriptionState(Long inscriptionId, Inscription.InscriptionState state) {
        Inscription inscription = findInscriptionById(inscriptionId);

        if (state.equals(ACCEPTED) && !canRegistrate(inscription.getEtudiant().getId())) {
            throw new UnsupportedActionException("Cet(te) Étudiant(e) est déja inscrit(e)");
        }

        inscription.setEtat(state);
        inscription.setUpdatedAt(new Date());
        inscriptionRepository.save(inscription);
    }

    public void createInscription(Long etudiantId, Long niveauId) {
        if (!canRegistrate(etudiantId)) {
            throw new UnsupportedActionException("Vous êtes déja inscrit");
        }

        AnneeUniv anneeUniv = anneeUnivRepository.findByCurrent(true)
                .orElseThrow(() -> new UnsupportedActionException("Il n'existe pas une année universiaire disponible pour l'inscription"));
        Etudiant etudiant = etudiantRepository.findByIdAndDeleted(etudiantId, false)
                .orElseThrow(() -> new EtudiantNotFoundException(etudiantId));
        Niveau niveau = niveauRepository.findById(niveauId)
                .orElseThrow(() -> new NiveauNotFoundException(niveauId));

        Inscription inscription = Inscription.builder()
                .niveau(niveau)
                .etudiant(etudiant)
                .anneeUniv(anneeUniv)
                .etat(PENDING)
                .createdAt(new Date())
                .build();
        inscriptionRepository.save(inscription);
    }

    public boolean canRegistrate(Long etudiantId) {
        Optional<Inscription> optional = inscriptionRepository.findByEtatAndAnneeUnivCurrentAndEtudiantId(
                ACCEPTED,
                true,
                etudiantId
        );
        return optional.isEmpty();
    }

    public Inscription findInscriptionById(Long inscriptionId) {
        return inscriptionRepository.findById(inscriptionId)
                .orElseThrow(() -> new InscriptionNotFoundException(inscriptionId));
    }

}
