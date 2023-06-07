package ensah.absencemanagement.controllers.users;

import ensah.absencemanagement.dtos.accounts.AccountOverviewDTO;
import ensah.absencemanagement.dtos.users.UserDTO;
import ensah.absencemanagement.dtos.users.UserRequest;
import ensah.absencemanagement.dtos.users.UserRoleRequest;
import ensah.absencemanagement.exceptions.UnsupportedActionException;
import ensah.absencemanagement.models.users.User;
import ensah.absencemanagement.services.accounts.AccountService;
import ensah.absencemanagement.services.users.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final AccountService accountService;

    @Autowired
    public UserController(UserService userService, AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
    }

    @GetMapping
    public String showUsersListPage(
            Model model,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            RedirectAttributes attributes
    ) {
        if (page < 1) {
            attributes.addAttribute("name", name);
            return "redirect:/users";
        }
        model.addAttribute("page", userService.getAllUsers(true, name, page - 1));
        model.addAttribute("setRoleRequest", new UserRoleRequest());
        addRolesToModel(model);
        return "users/users";
    }

    @GetMapping("/{id}")
    public String showUserInfosPage(
            @PathVariable("id") Long userId,
            Model model
    ) {
        UserDTO user = userService.getUserById(userId);

        if (user.getRole() == null) {
            model.addAttribute("user", user);
            return "users/user";
        }

        switch (user.getRole()) {
            case ETUDIANT -> {
                return "forward:/etudiants/" + userId;
            }
            case ENSEIGNANT -> {
                return "forward:/enseignants/" + userId;
            }
            case CADRE_ADMINISTRATEUR -> {
                return "forward:/cadres-admins/" + userId;
            }
            default -> {
                return "redirect:/logout";
            }
        }
    }

    @GetMapping("/create")
    public String showUserCreationPage(Model model) {
        model.addAttribute("addRequest", new UserRequest());
        return "users/create";
    }

    @PostMapping
    public RedirectView createUser(
            RedirectAttributes attributes,
            @ModelAttribute("addRequest") @Valid UserRequest request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            attributes.addAttribute("error", bindingResult.getAllErrors().get(0).getDefaultMessage());
        } else {
            userService.createUser(request);
            attributes.addAttribute("success", "Utilisateur ajouté avec succées");
        }
        return new RedirectView("/users/create");
    }

    @PostMapping("/{id}/role")
    public RedirectView setUserRole(
            RedirectAttributes attributes,
            @ModelAttribute("setRoleRequest") @Valid UserRoleRequest request,
            @PathVariable("id") Long userId,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            attributes.addAttribute("error", bindingResult.getAllErrors().get(0).getDefaultMessage());
        } else {
            try {
                Long newUserId = userService.updateUserRole(userId, request);

                AccountOverviewDTO account = accountService.createAccount(newUserId);
                if (account != null) {
                    attributes.addAttribute("account-created", "");
                    attributes.addAttribute("username", account.getUsername());
                    attributes.addAttribute("password", account.getPassword());
                }

                attributes.addAttribute("success", "Utilisateur modifié avec succées");
            } catch (UnsupportedActionException exception) {
                attributes.addAttribute("error", exception.getMessage());
            }
        }

        return new RedirectView("/users");
    }

    @PostMapping("/{id}/delete")
    public RedirectView deleteUser(
            RedirectAttributes attributes,
            @PathVariable("id") Long userId
    ) {
        userService.deleteUserById(userId);
        attributes.addAttribute("success", "Utilisateur supprimé avec succées");
        return new RedirectView("/users");
    }

    private void addRolesToModel(Model model) {
        model.addAttribute("roles", User.Role.getRoles());

        model.addAttribute("ROLE_ENSEIGNANT", User.Role.ENSEIGNANT);
        model.addAttribute("ROLE_ETUDIANT", User.Role.ETUDIANT);
        model.addAttribute("ROLE_CADRE_ADMINISTRATEUR", User.Role.CADRE_ADMINISTRATEUR);
    }

}
