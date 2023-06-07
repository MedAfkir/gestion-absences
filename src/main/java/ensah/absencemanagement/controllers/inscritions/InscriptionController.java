package ensah.absencemanagement.controllers.inscritions;

import ensah.absencemanagement.exceptions.UnsupportedActionException;
import ensah.absencemanagement.exceptions.not_found.EtudiantNotFoundException;
import ensah.absencemanagement.exceptions.not_found.NiveauNotFoundException;
import ensah.absencemanagement.models.inscriptions.Inscription;
import ensah.absencemanagement.models.users.User;
import ensah.absencemanagement.services.inscriptions.InscriptionService;
import ensah.absencemanagement.services.niveaux.NiveauService;
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
@RequestMapping("/inscriptions")
public class InscriptionController {

    private final InscriptionService service;
    private final NiveauService niveauService;

    @Autowired
    public InscriptionController(InscriptionService service, NiveauService niveauService) {
        this.service = service;
        this.niveauService = niveauService;
    }

    @GetMapping
    public String showInscriptionsListPage(
            Model model,
            HttpSession session,
            @RequestParam(value = "page", defaultValue = "1", required = false) int page
    ) {
        SessionUser user = SessionManager.getUserSession(session);
        if (user.getRole().equals(User.Role.ETUDIANT)) {
            model.addAttribute("canRegistrate", service.canRegistrate(user.getId()));
            model.addAttribute("niveaux", niveauService.getAllNiveaux());
            model.addAttribute("page", service.getInscriptionsByStudentId(user.getId(), page - 1));
        } else {
            model.addAttribute("page", service.getAllInscriptions(page - 1));
        }

        return "inscriptions/inscriptions";
    }

    @PostMapping
    public RedirectView registrate(
            RedirectAttributes attributes,
            @RequestParam("niveauId") Long niveauId,
            HttpSession session
    ) {
        SessionUser user = SessionManager.getUserSession(session);
        try {
            service.createInscription(user.getId(), niveauId);
            attributes.addAttribute("success", "Demande d'inscription envoyées avec succés");
        } catch (UnsupportedActionException exception) {
            attributes.addAttribute("error", exception.getMessage());
        } catch (NiveauNotFoundException exception) {
            attributes.addAttribute("error", "Niveau séléctionné est introuvable");
        } catch (EtudiantNotFoundException exception) {
            return new RedirectView("/logout");
        }

        return new RedirectView("/inscriptions");
    }

    @PostMapping("/{id}/state")
    public RedirectView updateInscriptionState(
            RedirectAttributes attributes,
            @PathVariable("id") Long inscriptionId,
            @RequestParam("state") Inscription.InscriptionState state
    ) {
        try {
            service.updateInscriptionState(inscriptionId, state);
            attributes.addAttribute("success", "Inscription modifiée avec succés");
        } catch (UnsupportedActionException exception) {
            attributes.addAttribute("error", exception.getMessage());
        }

        return new RedirectView("/inscriptions/" + inscriptionId);
    }

    @GetMapping("/{id}")
    public String showInscriptionInfosPage(
            Model model,
            @PathVariable("id") Long inscriptionId
    ) {
        model.addAttribute("inscription", service.getInscriptionById(inscriptionId));
        model.addAttribute("states", Inscription.InscriptionState.values());
        return "inscriptions/inscription";
    }

}
