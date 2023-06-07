package ensah.absencemanagement.controllers.modules;

import ensah.absencemanagement.dtos.modules.ModuleAddRequest;
import ensah.absencemanagement.dtos.modules.ModuleDTO;
import ensah.absencemanagement.dtos.modules.ModuleMapper;
import ensah.absencemanagement.dtos.modules.ModuleUpdateRequest;
import ensah.absencemanagement.services.matieres.MatiereService;
import ensah.absencemanagement.services.modules.ModuleService;
import ensah.absencemanagement.services.niveaux.NiveauService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
@RequestMapping("/modules")
public class ModuleController {

    private final ModuleMapper moduleMapper;
    private final ModuleService moduleService;
    private final MatiereService matiereService;
    private final NiveauService niveauService;

    @Autowired
    public ModuleController(ModuleMapper moduleMapper, ModuleService moduleService, MatiereService matiereService, NiveauService niveauService) {
        this.moduleMapper = moduleMapper;
        this.moduleService = moduleService;
        this.matiereService = matiereService;
        this.niveauService = niveauService;
    }

    @GetMapping
    public String getModulesPage(
            Model model,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page
    ) {
        model.addAttribute("page", moduleService.getAllModules(page - 1));
        return "modules/modules";
    }

    @GetMapping("/{id}")
    public String getModulePage(@PathVariable("id") Long moduleId, Model model) {
        model.addAttribute("availableMatieres", matiereService.getMatieresByModule(null));
        model.addAttribute("niveaux", niveauService.getAllNiveaux());


        ModuleDTO module = moduleService.getModuleById(moduleId);
        model.addAttribute("vmodule", module);
        model.addAttribute("updateRequest", moduleMapper.toModuleUpdateRequest(module));

        return "modules/module";
    }

    @GetMapping("/create")
    public String getModuleCreationPage(Model model) {
        model.addAttribute("request", new ModuleAddRequest());
        return "modules/create";
    }

    @PostMapping
    public RedirectView createModule(
            RedirectAttributes attributes,
            @ModelAttribute("request") @Valid ModuleAddRequest request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            attributes.addAttribute("error", bindingResult.getAllErrors().get(0).getDefaultMessage());
        } else {
            moduleService.createModule(request);
            attributes.addAttribute("success", "Module crée avec succés");
        }
        return new RedirectView("/modules/create");
    }

    @PostMapping("/{id}/infos")
    public RedirectView updateModule(
            @ModelAttribute("updateRequest") @Valid ModuleUpdateRequest request,
            @PathVariable("id") Long moduleId,
            RedirectAttributes attributes,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            attributes.addAttribute("error", bindingResult.getAllErrors().get(0).getDefaultMessage());
        } else {
            moduleService.updateModuleInfos(moduleId, request);
            attributes.addAttribute("success", "Module modifié avec succés");
        }
        return new RedirectView("/modules/" + moduleId);
    }

    @PostMapping("/{id}/matieres")
    public RedirectView updateModule(
            @PathVariable("id") Long moduleId,
            @RequestParam(value = "matieres", required = false) List<Long> matieresIds,
            RedirectAttributes attributes
    ) {
        if (matieresIds != null) {
            moduleService.updateModuleMatieres(moduleId, matieresIds);
            attributes.addAttribute("success", "Module modifié avec succés");
        }
        return new RedirectView("/modules/" + moduleId);
    }

    @PostMapping("/{id}/delete")
    public RedirectView deleteModule(@PathVariable("id") Long moduleId, RedirectAttributes attributes) {
        moduleService.deleteModuleById(moduleId);
        attributes.addAttribute("success", "Module supprimé avec succés");
        return new RedirectView("/modules/" + moduleId);
    }

}
