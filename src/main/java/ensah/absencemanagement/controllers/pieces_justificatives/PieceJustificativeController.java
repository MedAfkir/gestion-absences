package ensah.absencemanagement.controllers.pieces_justificatives;

import ensah.absencemanagement.models.absence.Absence;
import ensah.absencemanagement.models.users.User;
import ensah.absencemanagement.services.pieces_justificatives.PieceJustifService;
import ensah.absencemanagement.utils.SessionManager;
import ensah.absencemanagement.utils.SessionUser;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("/pieces-justificatives")
public class PieceJustificativeController {

    private final PieceJustifService pieceJustifService;

    @Autowired
    public PieceJustificativeController(PieceJustifService pieceJustifService) {
        this.pieceJustifService = pieceJustifService;
    }

    @GetMapping("/{id}")
    public String showPieceInfo(
            @PathVariable("id") Long pieceId,
            Model model,
            HttpSession session
    ) throws Exception {
        SessionUser user = SessionManager.getUserSession(session);
        model.addAttribute("piece", pieceJustifService.getPieceJusitificativeById(user, pieceId));
        model.addAttribute("states", Absence.AbsenceEtat.values());
        return "pieces-justifs/piece-justificative";
    }

    @PostMapping
    public RedirectView createPieceJustificative(
            RedirectAttributes attributes,
            @RequestParam("absenceId") Long absenceId,
            @RequestParam("intitule") String intitule,
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam(value = "etat", required = false) Absence.AbsenceEtat state
    ) {
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities();

        if (authorities.contains(User.Role.ETUDIANT))
            state = null;

        pieceJustifService.createPiecesJustificatives(absenceId, intitule, files, state);
        attributes.addAttribute("success", "Pièces jusitifiées enregistrées avec succés");
        return new RedirectView("/absences/" + absenceId);
    }

    @PostMapping("/{id}/state")
    public RedirectView updatePieceJustificativeState(
            RedirectAttributes attributes,
            @PathVariable("id") Long pieceId,
            @RequestParam("state") Absence.AbsenceEtat state
    ) {
        pieceJustifService.updatePieceJustificativeState(pieceId, state);
        attributes.addAttribute("success", "État de la pièce a été modifié avec succés");
        return new RedirectView("/pieces-justificatives/" + pieceId);
    }

}
