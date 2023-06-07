package ensah.absencemanagement.controllers.cadres_admins;

import ensah.absencemanagement.dtos.users.UserDTO;
import ensah.absencemanagement.dtos.users.UserMapper;
import ensah.absencemanagement.dtos.users.UserRequest;
import ensah.absencemanagement.services.cadres_admins.CadreAdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/cadres-admins")
@Slf4j
public class CadreAdminController {

    private final CadreAdminService cadreAdminService;
    private final UserMapper userMapper;

    @Autowired
    public CadreAdminController(CadreAdminService cadreAdminService, UserMapper userMapper) {
        this.cadreAdminService = cadreAdminService;
        this.userMapper = userMapper;
    }

    @GetMapping
    public String showCadresAdminsList(
            Model model,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "name", required = false) String name
    ) {
        model.addAttribute("page", cadreAdminService.getAllCadresAdmins(name, page - 1));
        return "cadres-admins/cadres-admins";
    }

    @GetMapping("/{id}")
    public String showCadreAdminInfosPage(
            Model model,
            @PathVariable("id") Long cadreAdminId
    ) {
        UserDTO cadreAdminDTO = cadreAdminService.getCadreAdminById(cadreAdminId);

        model.addAttribute("cadreAdmin", cadreAdminDTO);
        model.addAttribute("updateRequest", userMapper.map(cadreAdminDTO));
        return "cadres-admins/cadre-admin";
    }

    @PostMapping("/{id}")
    public RedirectView updateCadreAdminById(
            @ModelAttribute("updateRequest") UserRequest request,
            @PathVariable("id") Long cadreAdminId,
            RedirectAttributes attributes
    ) {
        cadreAdminService.updateCadreAdminById(cadreAdminId, request);

        attributes.addAttribute("success", "Enseignant modifié avec succés");
        return new RedirectView("/cadres-admins/" + cadreAdminId);
    }

    @PostMapping("/{id}/delete")
    public RedirectView deleteCadreAdminById(
            @PathVariable("id") Long cadreAdminId,
            RedirectAttributes attributes
    ) {
        cadreAdminService.deleteCadreAdminById(cadreAdminId);

        attributes.addAttribute("success", "Enseignant modifié avec succés");
        return new RedirectView("/cadres-admins/" + cadreAdminId);
    }


}
