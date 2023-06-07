package ensah.absencemanagement.controllers.absences;

import ensah.absencemanagement.dtos.absences.AbsenceMapper;
import ensah.absencemanagement.dtos.absences.AbsenceRequest;
import ensah.absencemanagement.dtos.absences.MultipleAbsenceRequest;
import ensah.absencemanagement.dtos.etudiants.EtudiantDTO;
import ensah.absencemanagement.dtos.reclamations.ReclamationRequest;
import ensah.absencemanagement.exceptions.UnsupportedActionException;
import ensah.absencemanagement.exceptions.not_found.MatiereNotFoundException;
import ensah.absencemanagement.exceptions.not_found.NotFoundException;
import ensah.absencemanagement.models.absence.Absence;
import ensah.absencemanagement.models.users.User;
import ensah.absencemanagement.services.absences.AbsenceService;
import ensah.absencemanagement.services.enseignants.EnseignantService;
import ensah.absencemanagement.services.matieres.MatiereService;
import ensah.absencemanagement.services.types_seances.TypeSeanceService;
import ensah.absencemanagement.utils.SessionManager;
import ensah.absencemanagement.utils.SessionUser;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("absences")
public class AbsenceController {

    private final AbsenceMapper absenceMapper;
    private final AbsenceService absenceService;
    private final MatiereService matiereService;
    private final TypeSeanceService typeSeanceService;
    private final EnseignantService enseignantService;

    @Autowired
    public AbsenceController(
            AbsenceMapper absenceMapper,
            AbsenceService absenceService,
            MatiereService matiereService,
            TypeSeanceService typeSeanceService,
            EnseignantService enseignantService
    ) {
        this.absenceMapper = absenceMapper;
        this.absenceService = absenceService;
        this.matiereService = matiereService;
        this.typeSeanceService = typeSeanceService;
        this.enseignantService = enseignantService;
    }

    @GetMapping
    public String showAbsencesList(
            Model model,
            HttpSession session,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page
    ) {
        SessionUser authenticatedUser = SessionManager.getUserSession(session);
        model.addAttribute(
                "page",
                absenceService.getAbsencesByAuthorities(authenticatedUser, page - 1)
        );
        return "abscences/abscences";
    }

    @GetMapping("/create")
    public String showCreationAbsencePage(
            Model model,
            HttpSession session,
            @RequestParam(value = "matiereId", required = false) Long matiereId
    ) {
        model.addAttribute("matieres", matiereService.getAllMatieres());
        model.addAttribute("typesSeances", typeSeanceService.getAllTypesSeances());
        model.addAttribute("enseignants", enseignantService.getAllEnseignants());

        SessionUser user = SessionManager.getUserSession(session);
        if (user.getRole().equals(User.Role.CADRE_ADMINISTRATEUR)) {
            model.addAttribute("multipleAbsenceRequest", new MultipleAbsenceRequest<String>());
        } else if (user.getRole().equals(User.Role.ENSEIGNANT)) {
            if (matiereId != null) {
                model.addAttribute("etudiants", matiereService.getMatiereEtudiants(matiereId));
                model.addAttribute("selectedMatiereId", matiereId);
            }

            model.addAttribute("absenceRequest", new AbsenceRequest());
        }


        return "abscences/create";
    }

    @PostMapping
    public RedirectView createAbsence(
            HttpSession session,
            RedirectAttributes attributes,
            @RequestParam("matiereId") Long matiereId,
            @RequestParam("typeSeanceId") Long typeSeanceId,
            @RequestParam("etudiants") List<Long> etudiants,
            @RequestParam("moment") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date moment,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            attributes.addAttribute("error", bindingResult.getAllErrors().get(0).getDefaultMessage());
        } else {
            try {
                SessionUser user = SessionManager.getUserSession(session);
                absenceService.createAbsence(matiereId, typeSeanceId, etudiants, user.getId(), moment);
                attributes.addAttribute("success", "Absences crées avec succés");
            } catch (NotFoundException | UnsupportedActionException exception) {
                attributes.addAttribute("error", exception.getMessage());
            }
        }

        return new RedirectView("/absences/create");
    }

    @PostMapping("/multiple")
    public String createAbsence(
            Model model,
            @ModelAttribute("multipleAbsenceRequest") MultipleAbsenceRequest<String> request
    ) {
        try {
            List<String> etudiantsCne = Arrays.stream(request.getEtudiants().trim().split(","))
                    .map(String::trim)
                    .toList();
            List<EtudiantDTO> etudiants = matiereService.getMatiereEtudiants(request.getMatiereId(), etudiantsCne);

            if (etudiants == null || etudiants.isEmpty()) {
                return "redirect:/absences/create?error=" + URLEncoder.encode("Étudiants non trouvés", StandardCharsets.UTF_8);
            }

            model.addAttribute("etudiants", etudiants);
            model.addAttribute("matieres", matiereService.getAllMatieres());
            model.addAttribute("typesSeances", typeSeanceService.getAllTypesSeances());
            model.addAttribute("enseignants", enseignantService.getAllEnseignants());
            model.addAttribute("request", request);

            return "abscences/creation-validation";
        } catch (MatiereNotFoundException exception) {
            return "redirect:/absences/create?error=" + URLEncoder.encode(exception.getMessage(), StandardCharsets.UTF_8);
        }
    }

    @PostMapping("/multiple/validate")
    public RedirectView validatecreationAbsence(
            RedirectAttributes attributes,
            @RequestParam("matiereId") Long matiereId,
            @RequestParam("enseignantId") Long enseignantId,
            @RequestParam("typeSeanceId") Long typeSeanceId,
            @RequestParam("etudiants") List<Long> etudiants,
            @RequestParam("moment") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date moment
    ) {
        try {
            absenceService.createAbsence(matiereId, typeSeanceId, etudiants, enseignantId, moment);
            attributes.addAttribute("success", "Absences crées avec succés");
        } catch (UnsupportedActionException exception) {
            attributes.addAttribute("error", exception.getMessage());
        }
        return new RedirectView("/absences/create");
    }

    @GetMapping("/{id}")
    public String showAbsenceInfos(
            @PathVariable("id") Long absenceId,
            HttpSession session,
            Model model
    ) {
        SessionUser authenticatedUser = SessionManager.getUserSession(session);
        model.addAttribute("states", Absence.AbsenceEtat.values());
        model.addAttribute("absence", absenceService.getAbsenceByAuthority(authenticatedUser, absenceId));
        model.addAttribute("sendComplaint", new ReclamationRequest());
        return "abscences/abscence";
    }

    @PostMapping("/{id}/state")
    public RedirectView updateAbsenceState(
            HttpSession session,
            @PathVariable("id") Long absenceId,
            @RequestParam("state") Absence.AbsenceEtat etat,
            RedirectAttributes attributes
    ) {
        SessionUser user = SessionManager.getUserSession(session);
        try {
            absenceService.updateAbsenceState(user, absenceId, etat);
            attributes.addAttribute("success", "État de l'absence a été modifié avec succés");
        } catch (UnsupportedActionException exception) {
            attributes.addAttribute("success", exception.getMessage());
        }

        return new RedirectView("/absences/" + absenceId);
    }

    @PostMapping("/{id}/cancel")
    public RedirectView showAbsenceInfos(
            HttpSession session,
            @PathVariable("id") Long absenceId,
            RedirectAttributes attributes
    ) {
        SessionUser user = SessionManager.getUserSession(session);
        try {
            absenceService.updateAbsenceState(user, absenceId, Absence.AbsenceEtat.ANNULEE);
            attributes.addAttribute("success", "État de l'absence a été modifié avec succés");
        } catch (UnsupportedActionException exception) {
            attributes.addAttribute("error", exception.getMessage());
        }

        return new RedirectView("/absences/" + absenceId);
    }

}
