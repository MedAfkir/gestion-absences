package ensah.absencemanagement.controllers.reclamations;

import ensah.absencemanagement.dtos.messages.MessageRequest;
import ensah.absencemanagement.dtos.reclamations.ReclamationRequest;
import ensah.absencemanagement.services.reclamations.ReclamationService;
import ensah.absencemanagement.utils.SessionManager;
import ensah.absencemanagement.utils.SessionUser;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/reclamations")
public class ReclamationController {

    private final ReclamationService service;

    @Autowired
    public ReclamationController(ReclamationService service) {
        this.service = service;
    }

    @GetMapping
    public String showReclamationsList(
            Model model,
            HttpSession session,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page
    ) {
        if (page < 1)
            return "redirect:/reclamtions";

        SessionUser user = SessionManager.getUserSession(session);
        model.addAttribute("page", service.getReclamations(user, page - 1));
        return "reclamations/reclamations";
    }

    @GetMapping("/{id}")
    public String showReclamationInfos(
            Model model,
            HttpSession session,
            @PathVariable("id") Long reclamationId
    ) {
        SessionUser user = SessionManager.getUserSession(session);
        model.addAttribute("reclamation", service.getReclamationById(user, reclamationId));
        model.addAttribute("reponses", service.getReclamationReponses(user, reclamationId));
        model.addAttribute("responseRequest", new MessageRequest());
        return "reclamations/reclamation";
    }

    @PostMapping
    public RedirectView createReclamation(
            HttpSession session,
            RedirectAttributes attributes,
            @ModelAttribute("sendComplaint") @Valid ReclamationRequest request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            attributes.addAttribute("error", bindingResult.getAllErrors().get(0).getDefaultMessage());
        } else {
            SessionUser user = SessionManager.getUserSession(session);
            service.createReclamation(user.getId(), request);
            attributes.addAttribute("success", "Réclamation envoyée avec succés");
        }
        return new RedirectView("/absences/" + request.getAbsenceId());
    }

    @PostMapping("/{id}/answer")
    public RedirectView answer(
            RedirectAttributes attributes,
            @ModelAttribute("responseRequest") @Valid MessageRequest request,
            @PathVariable("id") Long reclamationId,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            attributes.addAttribute("error", bindingResult.getAllErrors().get(0).getDefaultMessage());
        } else {
            service.answerReclamation(reclamationId, request);
            attributes.addAttribute("success", "Réponse enregistrée avec succés");
        }
        return new RedirectView("/reclamations/" + reclamationId);
    }

}
