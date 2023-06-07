package ensah.absencemanagement.controllers.demandes_autorisation;

import ensah.absencemanagement.exceptions.UnsupportedActionException;
import ensah.absencemanagement.services.demandes_autorisation.DemandeAutorisationService;
import ensah.absencemanagement.services.enseignants.EnseignantService;
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
@RequestMapping("/demandes-autorisation")
public class DemandeAutorisationController {

    private final DemandeAutorisationService service;
    private final EnseignantService enseignantService;

    @Autowired
    public DemandeAutorisationController(DemandeAutorisationService service, EnseignantService enseignantService) {
        this.service = service;
        this.enseignantService = enseignantService;
    }

    @GetMapping
    public String showDemandsList(
            HttpSession session,
            Model model,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page
    ) {
        SessionUser user = SessionManager.getUserSession(session);
        model.addAttribute("demands", service.getDemandesAutorisationByAuthority(user, page - 1));
        return "demandes-autorisation/demands";
    }

    @GetMapping("/{id}")
    public String showDemandInfos(Model model, @PathVariable Long id) {
        model.addAttribute("demand", service.getDemandeById(id));
        return "demandes-autorisation/demande";
    }

    @GetMapping("/create")
    public String createDemndPage(Model model) {
        model.addAttribute("enseignants", enseignantService.getAllEnseignants());
        return "demandes-autorisation/create";
    }

    @PostMapping
    public RedirectView createDemand(
            HttpSession session,
            RedirectAttributes attributes,
            @RequestParam("enseignantId") Long enseignantId,
            @RequestParam("content") String content
    ) {
        SessionUser user = SessionManager.getUserSession(session);
        try {
            service.createDemandeAutorisation(user.getId(), enseignantId, content);
            attributes.addAttribute("success", "Votre demande est enregistré avec succés");
        } catch (UnsupportedActionException exception) {
            attributes.addAttribute("error", exception.getMessage());
        }
        return new RedirectView("/demandes-autorisation/create");
    }

}
