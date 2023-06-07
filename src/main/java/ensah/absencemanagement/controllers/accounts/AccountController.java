package ensah.absencemanagement.controllers.accounts;

import ensah.absencemanagement.dtos.profil.PasswordUpdateRequest;
import ensah.absencemanagement.exceptions.UnsupportedActionException;
import ensah.absencemanagement.services.accounts.AccountService;
import ensah.absencemanagement.utils.SessionManager;
import ensah.absencemanagement.utils.SessionUser;
import ensah.absencemanagement.utils.ValidationUtils;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/change-password")
    public RedirectView changePassword(
            HttpSession session,
            @ModelAttribute("updatePassword") @Valid PasswordUpdateRequest request,
            RedirectAttributes attributes,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            attributes.addAttribute("error", bindingResult.getAllErrors().get(0).getDefaultMessage());
        } else {
            SessionUser user = SessionManager.getUserSession(session);

            if (ValidationUtils.hasErrors(request)) {
                attributes.addAttribute("error", ValidationUtils.getFirstError(request));
            } else {
                try {
                    accountService.changePassword(user.getId(), request);
                    attributes.addAttribute("success", "Mot de passe modifié avec succés");
                } catch (UnsupportedActionException exception) {
                    attributes.addAttribute("error", exception.getMessage());
                }
            }
        }

        return new RedirectView("/profil");
    }

    @PostMapping("/users/{id}/change-password")
    public RedirectView changePassword(
            @PathVariable("id") Long userId,
            @RequestParam("password") String password,
            RedirectAttributes attributes
    ) {
        try {
            accountService.changePassword(userId, password);
            attributes.addAttribute("success", "Mot de passe modifié avec succés");
        } catch (UnsupportedActionException exception) {
            attributes.addAttribute("error", exception.getMessage());
        }

        return new RedirectView("/users/" + userId);
    }

    @PostMapping("/users/{id}/enable")
    public RedirectView enableAccount(
            @PathVariable("id") Long userId,
            RedirectAttributes attributes
    ) {
        try {
            accountService.setActivationAccountState(userId, true);
            attributes.addAttribute("success", "Compte activé avec succés");
        } catch (UnsupportedActionException exception) {
            attributes.addAttribute("error", exception.getMessage());
        }

        return new RedirectView("/users/" + userId);
    }

    @PostMapping("/users/{id}/disable")
    public RedirectView disableAccount(@PathVariable("id") Long userId, RedirectAttributes attributes) {
        try {
            accountService.setActivationAccountState(userId, false);
            attributes.addAttribute("success", "Compte désactivé avec succés");
        } catch (UnsupportedActionException exception) {
            attributes.addAttribute("error", exception.getMessage());
        }

        return new RedirectView("/users/" + userId);
    }

    @PostMapping("/users/{id}/lock")
    public RedirectView lockAccount(@PathVariable("id") Long userId, RedirectAttributes attributes) {
        try {
            accountService.setAccountLockoutState(userId, true);
            attributes.addAttribute("success", "Compte verrouillé avec succés");
        } catch (UnsupportedActionException exception) {
            attributes.addAttribute("error", exception.getMessage());
        }

        return new RedirectView("/users/" + userId);
    }

    @PostMapping("/users/{id}/unlock")
    public RedirectView unlockAccount(@PathVariable("id") Long userId, RedirectAttributes attributes) {
        try {
            accountService.setAccountLockoutState(userId, false);
            attributes.addAttribute("success", "Compte déverrouillé avec succés");
        } catch (UnsupportedActionException exception) {
            attributes.addAttribute("error", exception.getMessage());
        }
        return new RedirectView("/users/" + userId);
    }


}
