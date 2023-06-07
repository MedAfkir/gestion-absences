package ensah.absencemanagement.services.absences;

import ensah.absencemanagement.dtos.absences.AbsenceDTO;
import ensah.absencemanagement.dtos.absences.AbsenceMapper;
import ensah.absencemanagement.dtos.annees_univ.AnneeUnivDTO;
import ensah.absencemanagement.dtos.annees_univ.AnneeUnivMapper;
import ensah.absencemanagement.exceptions.UnsupportedActionException;
import ensah.absencemanagement.exceptions.not_found.*;
import ensah.absencemanagement.models.absence.Absence;
import ensah.absencemanagement.models.annees_univ.AnneeUniv;
import ensah.absencemanagement.models.enseignants.Enseignant;
import ensah.absencemanagement.models.etudiants.Etudiant;
import ensah.absencemanagement.models.inscriptions.Inscription;
import ensah.absencemanagement.models.matieres.Matiere;
import ensah.absencemanagement.models.types_seance.TypeSeance;
import ensah.absencemanagement.models.users.User;
import ensah.absencemanagement.repositories.*;
import ensah.absencemanagement.utils.SessionUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Pair;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static ensah.absencemanagement.models.absence.Absence.AbsenceEtat.*;

@Service
@Transactional
public class AbsenceService {

    @Autowired
    public AbsenceService(
            TypeSeanceRepostiory typeSeanceRepostiory,
            EnseignantRepository enseignantRepository,
            EtudiantRepository etudiantRepository,
            MatiereRepository matiereRepository,
            AbsenceRepository absenceRepository,
            AbsenceMapper absenceMapper,
            AnneeUnivRepository anneeUnivRepository,
            AnneeUnivMapper anneeUnivMapper
    ) {
        this.typeSeanceRepostiory = typeSeanceRepostiory;
        this.enseignantRepository = enseignantRepository;
        this.etudiantRepository = etudiantRepository;
        this.matiereRepository = matiereRepository;
        this.absenceRepository = absenceRepository;
        this.absenceMapper = absenceMapper;
        this.anneeUnivRepository = anneeUnivRepository;
        this.anneeUnivMapper = anneeUnivMapper;
    }

    private final TypeSeanceRepostiory typeSeanceRepostiory;
    private final EnseignantRepository enseignantRepository;
    private final EtudiantRepository etudiantRepository;
    private final MatiereRepository matiereRepository;
    private final AbsenceRepository absenceRepository;
    private final AbsenceMapper absenceMapper;
    private final AnneeUnivRepository anneeUnivRepository;
    private final AnneeUnivMapper anneeUnivMapper;

    @Value("${settings.absence.max-day-threshold-cancellation}")
    private Integer maxDaysThresholdCancellation;

    @Value("${settings.absence.max-absences-per-element}")
    private Integer maxAbsencesPerElement;

    public AbsenceDTO getAbsenceByAuthority(SessionUser user, Long absenceId) {
        Absence absence = findAbsenceById(absenceId);

        if (!hasAccessToRead(user, absence))
            throw new AccessDeniedException("Vous n'avez pas accés à cette ressource");
        return absenceMapper.map(absence);
    }

    public Page<AbsenceDTO> getAbsencesByAuthorities(SessionUser user, int page) {
        switch (user.getRole()) {
            case ETUDIANT -> {
                return getAbsencesByEtudiant(user.getId(), page);
            }
            case ENSEIGNANT -> {
                return getAbsencesByEnseignant(user.getId(), page);
            }
            default -> {
                return getAllAbsences(page);
            }
        }
    }

    public Page<AbsenceDTO> getAllAbsences(int page) {
        return absenceRepository.findAll(getPage(page))
                .map(absenceMapper::map);
    }

    public Page<AbsenceDTO> getAbsencesByEnseignant(Long enseignantId, int page) {
        return getAbsencesByEnseignant(enseignantId, null, page);
    }

    public Page<AbsenceDTO> getAbsencesByEnseignant(
            Long enseignantId,
            Absence.AbsenceEtat etat,
            int pageIndex
    ) {
        Enseignant enseignant = enseignantRepository.findById(enseignantId)
                .orElseThrow(() -> new EtudiantNotFoundException(enseignantId));

        Page<Absence> page = etat == null
                ? absenceRepository
                    .findByEnseignant(enseignant, getPage(pageIndex))
                : absenceRepository
                    .findByEnseignantAndEtat(enseignant, etat, getPage(pageIndex));

        return page.map(absenceMapper::map);
    }

    public Pair<AnneeUnivDTO, Page<AbsenceDTO>> getAbsencesByEtudiantAndYear(Long etudiantId, Long anneeUnivId, int page) {
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new EtudiantNotFoundException(etudiantId));

        AnneeUniv anneeUniv;
        if (anneeUnivId == null) {
            Inscription lastInscription = getStudentLastInscription(etudiant);
            if (lastInscription == null)
                return null;
            anneeUniv = lastInscription.getAnneeUniv();
        } else {
            anneeUniv = anneeUnivRepository.findById(anneeUnivId).orElse(null);
        }

        if (anneeUniv == null)
            return null;

        return getAbsencesByEtudiantAndYear(etudiant, anneeUniv, page);
    }

    public Pair<AnneeUnivDTO, Page<AbsenceDTO>> getAbsencesByEtudiantAndYear(Etudiant etudiant, AnneeUniv anneeUniv, int page) {
        Page<AbsenceDTO> absences = absenceRepository.findByEtudiantAndInscriptionAnneeUniv(etudiant, anneeUniv, getPage(page))
                .map(absenceMapper::map);

        return Pair.of(anneeUnivMapper.map(anneeUniv), absences);
    }

    public Page<AbsenceDTO> getAbsencesByEtudiant(Long etudiantId, int pageIndex) {
        return getAbsencesByEtudiant(etudiantId, null, pageIndex);
    }

    public Page<AbsenceDTO> getAbsencesByEtudiant(
            Long etudiantId,
            Absence.AbsenceEtat etat,
            int pageIndex
    ) {
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new EtudiantNotFoundException(etudiantId));
        Page<Absence> page = etat == null
                ? absenceRepository.findByEtudiant(etudiant, getPage(pageIndex))
                : absenceRepository.findByEtudiantAndEtat(etudiant, etat, getPage(pageIndex));

        return page.map(absenceMapper::map);
    }

    public AbsenceDTO getAbsenceById(Long absenceId) {
        return absenceMapper.map(findAbsenceById(absenceId));
    }

    public void createAbsence(Long matiereId, Long typeSeanceId, List<Long> etudiantsIds, Long enseignantId, Date moment) {
        List<Etudiant> etudiants = etudiantRepository.findByIdInAndDeleted(etudiantsIds, false);

        Enseignant enseignant = enseignantRepository.findByIdAndDeleted(enseignantId, false)
                .orElseThrow(() -> new EnseignantNotFoundException(enseignantId));

        TypeSeance typeSeance = typeSeanceRepostiory.findById(typeSeanceId)
                .orElseThrow(() -> new TypeSeanceNotFoundException(typeSeanceId));

        Matiere matiere = matiereRepository.findById(matiereId)
                .orElseThrow(() -> new MatiereNotFoundException(matiereId));

        List<Absence> absences = etudiants.stream()
                .map(etudiant -> Absence.builder()
                        .etudiant(etudiant)
                        .inscription(getStudentLastInscription(etudiant))
                        .matiere(matiere)
                        .typeSeance(typeSeance)
                        .enseignant(enseignant)
                        .moment(moment)
                        .etat(NON_JUSTIFIEE)
                        .createdAt(new Date())
                        .build())
                .toList();

        absenceRepository.saveAll(absences);

        etudiants.forEach(etudiant -> sendWarning(etudiant, matiere));
    }

    public void updateAbsenceState(SessionUser user, Long absenceId, Absence.AbsenceEtat etat) {
        Absence absence = findAbsenceById(absenceId);

        if (etat.equals(ANNULEE) && user.getRole().equals(User.Role.ENSEIGNANT)) {
            Duration duration = Duration.between(
                    absence.getCreatedAt().toInstant(),
                    new Date().toInstant()
            );

            if (duration.toDays() > maxDaysThresholdCancellation) {
                throw new UnsupportedActionException("Le seuil autorisé pour annuler l'absence est dépassé");
            }
        }

        absence.setEtat(etat);
        absence.setUpdatedAt(new Date());
        absenceRepository.save(absence);
    }

    public void sendWarning(Etudiant etudiant, Matiere matiere) {
        Page<Absence> absences = absenceRepository.findByEtudiantAndMatiere(etudiant, matiere, Pageable.unpaged());
        if (absences.hasContent() && absences.getContent().size() >= maxAbsencesPerElement) {
            // TODO send notification
        }
    }

    private Absence findAbsenceById(Long absenceId) {
        return absenceRepository.findById(absenceId)
                .orElseThrow(() -> new AbsenceNotFoundException(absenceId));
    }

    private Pageable getPage(int page) {
        return PageRequest.of(page, 10, Sort.by(Sort.Order.desc("createdAt")));
    }

    private boolean hasAccessToRead(SessionUser user, Absence absence) {
        switch (user.getRole()) {
            case ETUDIANT -> {
                return absence.getEtudiant().getId().equals(user.getId());
            }
            case ENSEIGNANT -> {
                return absence.getEnseignant().getId().equals(user.getId());
            }
            default -> {
                return true;
            }
        }
    }

    private Inscription getStudentLastInscription(Etudiant etudiant) {
        if (etudiant.getNiveau() == null) return null;

        List<Inscription> acceptedInscriptions = etudiant.getInscriptions().stream()
                .filter(i -> i.getEtat().equals(Inscription.InscriptionState.ACCEPTED))
                .toList();

        if (acceptedInscriptions.isEmpty()) return null;

        Optional<Inscription> max = acceptedInscriptions.stream()
                .max(Comparator.comparing(i -> i.getAnneeUniv().getStart()));
        return max.orElse(null);
    }

}
