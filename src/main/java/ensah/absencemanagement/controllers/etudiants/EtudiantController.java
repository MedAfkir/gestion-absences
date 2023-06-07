package ensah.absencemanagement.controllers.etudiants;

import ensah.absencemanagement.dtos.etudiants.EtudiantDTO;
import ensah.absencemanagement.dtos.etudiants.EtudiantMapper;
import ensah.absencemanagement.dtos.users.UserRequest;
import ensah.absencemanagement.models.users.User;
import ensah.absencemanagement.services.absences.AbsenceService;
import ensah.absencemanagement.services.etudiants.EtudiantService;
import ensah.absencemanagement.services.inscriptions.InscriptionService;
import ensah.absencemanagement.services.logging_events.LoggingEventService;
import ensah.absencemanagement.utils.SessionManager;
import ensah.absencemanagement.utils.SessionUser;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/etudiants")
public class EtudiantController {

    private final EtudiantService etudiantService;
    private final EtudiantMapper etudiantMapper;
    private final InscriptionService inscriptionService;
    private final AbsenceService absenceService;
    private final LoggingEventService loggingEventService;

    @Autowired
    public EtudiantController(
            EtudiantService etudiantService,
            EtudiantMapper etudiantMapper,
            InscriptionService inscriptionService,
            AbsenceService absenceService,
            LoggingEventService loggingEventService
    ) {
        this.etudiantService = etudiantService;
        this.etudiantMapper = etudiantMapper;
        this.inscriptionService = inscriptionService;
        this.absenceService = absenceService;
        this.loggingEventService = loggingEventService;
    }

    @GetMapping
    public String showEtudiantsListPage(
            Model model,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "cne", required = false) String cne
    ) {
        if (page < 1) return "redirect:/etudiants";
        model.addAttribute("page", etudiantService.getAllEtudiants(name, cne, page - 1));
        return "etudiants/etudiants";
    }

    @GetMapping("/{id}")
    public String showEtudiantInfosPage(
            Model model,
            HttpSession session,
            @PathVariable("id") Long etudiantId,
            @RequestParam(value = "inscriptions_page", required = false, defaultValue = "1") int inscriptionPage,
            @RequestParam(value = "absences_page", required = false, defaultValue = "1") int absencesPage,
            @RequestParam(value = "historical_page", required = false, defaultValue = "1") int historicalPage,
            @RequestParam(value = "annee_univ_id", required = false) Long anneeUnivId
    ) {
        EtudiantDTO etudiantDTO = etudiantService.getEtudiantById(etudiantId);
        model.addAttribute("etudiant", etudiantDTO);
        model.addAttribute("updateRequest", etudiantMapper.map(etudiantDTO));
        model.addAttribute("inscriptions", inscriptionService.getInscriptionsByStudentId(etudiantId, inscriptionPage - 1));
        model.addAttribute("absences", absenceService.getAbsencesByEtudiantAndYear(etudiantId, anneeUnivId, absencesPage - 1));

        SessionUser user = SessionManager.getUserSession(session);
        if (user.getRole().equals(User.Role.SUPER_ADMIN)) {
            model.addAttribute("historical", loggingEventService.getVisitsByUserId(etudiantId, historicalPage - 1));
        }

        return "etudiants/etudiant";
    }

    @PostMapping("/{id}")
    public RedirectView updateEtudiantById(
            @PathVariable("id") Long etudiantId,
            @ModelAttribute("updateRequest") UserRequest request,
            RedirectAttributes attributes
    ) {
        etudiantService.updateEtudiantInfosById(etudiantId, request);

        attributes.addAttribute("success", "Étudiant modifié avec succés");
        return new RedirectView("/etudiants/" + etudiantId);
    }

    @PostMapping("/{id}/delete")
    public RedirectView deleteEtudiantById(
            @PathVariable("id") Long etudiantId,
            RedirectAttributes attributes
    ) {
        etudiantService.deleteEtudiantById(etudiantId);
        attributes.addAttribute("success", "Étudiant supprimé avec succés");
        return new RedirectView("/etudiants");
    }

}
