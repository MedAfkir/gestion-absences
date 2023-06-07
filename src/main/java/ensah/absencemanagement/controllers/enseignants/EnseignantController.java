package ensah.absencemanagement.controllers.enseignants;

import ensah.absencemanagement.dtos.enseignants.EnseignantDTO;
import ensah.absencemanagement.dtos.enseignants.EnseignantMapper;
import ensah.absencemanagement.dtos.users.UserRequest;
import ensah.absencemanagement.models.users.User;
import ensah.absencemanagement.services.absences.AbsenceService;
import ensah.absencemanagement.services.enseignants.EnseignantService;
import ensah.absencemanagement.services.logging_events.LoggingEventService;
import ensah.absencemanagement.utils.SessionManager;
import ensah.absencemanagement.utils.SessionUser;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/enseignants")
public class EnseignantController {

    private final EnseignantService enseignantService;
    private final EnseignantMapper enseignantMapper;
    private final AbsenceService absenceService;
    private final LoggingEventService loggingEventService;

    @Autowired
    public EnseignantController(EnseignantService enseignantService, AbsenceService absenceService, EnseignantMapper enseignantMapper, LoggingEventService loggingEventService) {
        this.enseignantService = enseignantService;
        this.enseignantMapper = enseignantMapper;
        this.loggingEventService = loggingEventService;
        this.absenceService = absenceService;
    }

    @GetMapping
    public String showEnseignantsListPage(
            Model model,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "cin", required = false) String cin
    ) {
        model.addAttribute("page", enseignantService.getAllEnseignants(name, cin, page - 1));
        return "enseignants/enseignants";
    }

    @GetMapping("/{id}")
    public String showEnseignantInfosPage(
            Model model,
            HttpSession session,
            @PathVariable("id") Long enseignantId,
            @RequestParam(value = "historical_page", required = false, defaultValue = "1") int historicalPage,
            @RequestParam(value = "absence_page", required = false, defaultValue = "1") int absencePage
    ) {
        EnseignantDTO enseignantDTO = enseignantService.getEnseignantById(enseignantId);

        model.addAttribute("enseignant", enseignantDTO);
        model.addAttribute("updateRequest", enseignantMapper.map(enseignantDTO));
        SessionUser user = SessionManager.getUserSession(session);

        model.addAttribute("absences", absenceService.getAbsencesByEnseignant(enseignantId, absencePage - 1""));

        if (user.getRole().equals(User.Role.SUPER_ADMIN)) {
            model.addAttribute("historical", loggingEventService.getVisitsByUserId(enseignantId, historicalPage - 1));
        }

        return "enseignants/enseignant";
    }

    @PostMapping("/{id}")
    public RedirectView updateEnseignantById(
            @ModelAttribute("updateRequest") UserRequest request,
            @PathVariable("id") Long enseignantId,
            RedirectAttributes attributes
    ) {
        enseignantService.updateEnseignant(enseignantId, request);

        attributes.addAttribute("success", "Enseignant modifié avec succés");
        return new RedirectView("/enseignants/" + enseignantId);
    }

    @PostMapping("/{id}/delete")
    public RedirectView deleteEnseignantById(
            @PathVariable("id") Long enseignantId,
            RedirectAttributes attributes
    ) {
        enseignantService.deleteEnseignant(enseignantId);

        attributes.addAttribute("success", "Enseignant modifié avec succés");
        return new RedirectView("/enseignants/" + enseignantId);
    }

}
