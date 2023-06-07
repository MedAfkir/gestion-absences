package ensah.absencemanagement.controllers.authentication;

import ensah.absencemanagement.dtos.accounts.AccountMapper;
import ensah.absencemanagement.models.accounts.Account;
import ensah.absencemanagement.repositories.AccountRepository;
import ensah.absencemanagement.utils.SessionManager;
import ensah.absencemanagement.utils.SessionUser;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;

@Controller
@RequestMapping("/login")
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, AccountRepository accountRepository, AccountMapper accountMapper) {
        this.authenticationManager = authenticationManager;
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
    }

    @GetMapping
    public String showLoginPage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            return "login";
        }
        return "redirect:/";
    }

    @PostMapping
    public RedirectView login(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            RedirectAttributes attributes,
            HttpSession session
    ) {
        Optional<Account> account = accountRepository.findByUsernameAndUserDeleted(username, false);

        if (account.isPresent()) {
            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(username, password)
                );

                if (!account.get().isNotLocked()) {
                    attributes.addAttribute("error", "Votre compte est verrouillé, Veuiller contacter l'administration");
                } else if (!account.get().isActive()) {
                    attributes.addAttribute("error", "Votre compte est désactivé, Veuiller contacter l'administration");
                } else {
                    if (account.get().getFailureCount() != 0) {
                        account.get().setFailureCount(0);
                        accountRepository.save(account.get());
                    }

                    SessionUser sessionUser = accountMapper.maptoSessionUser(account.get());
                    SessionManager.addUserToSession(session, sessionUser);
                    return new RedirectView("/");
                }
            } catch (AuthenticationException e) {
                if (account.get().isNotLocked()) {
                    account.get().setFailureCount(account.get().getFailureCount() + 1);
                    accountRepository.save(account.get());
                }
                attributes.addAttribute("error", "Identifiant ou mot de passe incorrecte");
            }
        } else {
            attributes.addAttribute("error", "Identifiant ou mot de passe incorrecte");
        }

        attributes.addAttribute("username", username);
        return new RedirectView("/login");
    }

}
