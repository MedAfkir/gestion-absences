package ensah.absencemanagement.controllers.profil;

import ensah.absencemanagement.dtos.enseignants.EnseignantDTO;
import ensah.absencemanagement.dtos.profil.PasswordUpdateRequest;
import ensah.absencemanagement.dtos.profil.ProfilUpdateRequest;
import ensah.absencemanagement.dtos.users.UserDTO;
import ensah.absencemanagement.dtos.users.UserMapper;
import ensah.absencemanagement.exceptions.UnsupportedActionException;
import ensah.absencemanagement.exceptions.not_found.UserNotFoundException;
import ensah.absencemanagement.services.enseignants.EnseignantService;
import ensah.absencemanagement.services.users.UserService;
import ensah.absencemanagement.utils.SessionManager;
import ensah.absencemanagement.utils.SessionUser;
import ensah.absencemanagement.utils.ValidationUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/profil")
public class ProfilController {

    private final UserService userService;
    private final EnseignantService enseignantService;
    private final UserMapper mapper;

    @Autowired
    public ProfilController(UserService userService, EnseignantService enseignantService, UserMapper mapper) {
        this.userService = userService;
        this.enseignantService = enseignantService;
        this.mapper = mapper;
    }

    @GetMapping
    public String showProfil(HttpSession session, Model model) {
        SessionUser userSession = SessionManager.getUserSession(session);
        if (userSession == null) return "redirect:/logout";

        try {
            UserDTO user = userService.getUserById(userSession.getId());
            model.addAttribute("updatePassword", new PasswordUpdateRequest());
            model.addAttribute("profilRequest", mapper.toProfil(user));
            switch (user.getRole()) {
                case ETUDIANT -> {
                    return "profil/profil-etudiant";
                }
                case ENSEIGNANT -> {
                    EnseignantDTO enseignant = enseignantService.getEnseignantById(user.getId());
                    model.addAttribute("receiveAuthorisationDemands", enseignant.isReceiveAuthorisationDemands());
                    return "profil/profil-enseignant";
                }
                case CADRE_ADMINISTRATEUR -> {
                    return "profil/profil-cadre-admin";
                }
                case SUPER_ADMIN -> {
                    return "profil/profil-super-admin";
                }
                default -> {
                    return "redirect:/logout";
                }
            }
        } catch (UserNotFoundException exception) {
            return "redirect:/logout";
        }
    }

    @PostMapping
    public RedirectView updateProfil(
            HttpSession session,
            @ModelAttribute("profilRequest") ProfilUpdateRequest request,
            RedirectAttributes attributes
    ) {
        SessionUser userSession = SessionManager.getUserSession(session);

        if (ValidationUtils.hasErrors(request)) {
            attributes.addAttribute("error", ValidationUtils.getFirstError(request));
        } else {
            userService.updateUserById(userSession.getId(), request);
            attributes.addAttribute("success", "Informations modifiées avec succés");
        }

        return new RedirectView("/profil");
    }

    @PostMapping("/update-image")
    public RedirectView uploadProfilImage(
            HttpSession session,
            @RequestParam("file") MultipartFile file,
            RedirectAttributes attributes
    ) {
        SessionUser user = SessionManager.getUserSession(session);
        try {
            userService.uploadProfilImage(user.getId(), file);
            attributes.addAttribute("success", "Photo de profil modifié avec succés");
        } catch (UnsupportedActionException exception) {
            attributes.addAttribute("error", exception.getMessage());
        }
        return new RedirectView("/profil");
    }

    @PostMapping("/remove-image")
    public RedirectView removeProfilImage(HttpSession session, RedirectAttributes attributes) {
        SessionUser user = SessionManager.getUserSession(session);
        userService.removeProfilImage(user.getId());
        attributes.addAttribute("success", "Photo de profil retiré avec succés");
        return new RedirectView("/profil");
    }

    @PostMapping("/receive-authorisation-demands")
    public RedirectView updateReceivingAuthorisationDemands(HttpSession session, RedirectAttributes attributes) {
        SessionUser user = SessionManager.getUserSession(session);
        enseignantService.updateReceivingAuthorisationDemands(user.getId(), true);
        attributes.addAttribute("success", "La réception des demandes d'autorisation d'absences est activée");
        return new RedirectView("/profil");
    }

    @PostMapping("/not-receive-authorisation-demands")
    public RedirectView updateReceivingAuthorisationDemands(RedirectAttributes attributes, HttpSession session) {
        SessionUser user = SessionManager.getUserSession(session);
        enseignantService.updateReceivingAuthorisationDemands(user.getId(), false);
        attributes.addAttribute("success", "La réception des demandes d'autorisation d'absences est désactivée");
        return new RedirectView("/profil");
    }

}
