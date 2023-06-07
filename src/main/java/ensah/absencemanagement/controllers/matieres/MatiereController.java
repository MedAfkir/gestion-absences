package ensah.absencemanagement.controllers.matieres;

import ensah.absencemanagement.dtos.matieres.MatiereAddRequest;
import ensah.absencemanagement.dtos.matieres.MatiereDTO;
import ensah.absencemanagement.dtos.matieres.MatiereMapper;
import ensah.absencemanagement.dtos.matieres.MatiereUpdateRequest;
import ensah.absencemanagement.services.matieres.MatiereService;
import ensah.absencemanagement.services.modules.ModuleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/matieres")
public class MatiereController {

    private final ModuleService moduleService;
    private final MatiereService matiereService;
    private final MatiereMapper matiereMapper;

    @Autowired
    public MatiereController(ModuleService moduleService, MatiereService matiereService, MatiereMapper matiereMapper) {
        this.moduleService = moduleService;
        this.matiereService = matiereService;
        this.matiereMapper = matiereMapper;
    }

    @GetMapping
    public String showMatieresPage(
            Model model,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page
    ) {
        model.addAttribute("page", matiereService.getAllMatieres(page - 1));
        return "matieres/matieres";
    }

    @GetMapping("/{id}")
    public String showMatierePage(@PathVariable("id") Long matiereId, Model model) {
        MatiereDTO matiere = matiereService.getMatiereById(matiereId);
        model.addAttribute("matiere", matiere);
        model.addAttribute("updateRequest", matiereMapper.map(matiere));
        model.addAttribute("modules", moduleService.getAllModules());

        return "matieres/matiere";
    }

    @GetMapping("/create")
    public String getMatiereCreationPage(Model model) {
        model.addAttribute("addRequest", new MatiereAddRequest());
        return "matieres/create";
    }

    @PostMapping
    public RedirectView createMatiere(
            RedirectAttributes attributes,
            @ModelAttribute("addRequest") @Valid MatiereAddRequest request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            attributes.addAttribute("error", bindingResult.getAllErrors().get(0).getDefaultMessage());
        } else {
            matiereService.createMatiere(request);
            attributes.addAttribute("success", "Matière crée avec succés");
        }
        return new RedirectView("/matieres/create");
    }

    @PostMapping("/{id}")
    public RedirectView updateMatiere(
            @PathVariable("id") Long matiereId,
            @ModelAttribute("updateRequest") @Valid MatiereUpdateRequest request,
            RedirectAttributes attributes,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            attributes.addAttribute("error", bindingResult.getAllErrors().get(0).getDefaultMessage());
        } else {
            matiereService.updateMatiere(matiereId, request);
            attributes.addAttribute("success", "Matière modifiée avec succés");
        }
        return new RedirectView("/matieres/" + matiereId);
    }

    @PostMapping("/{id}/delete")
    public RedirectView deleteMatiere(@PathVariable("id") Long matiereId, RedirectAttributes attributes) {
        matiereService.deleteMatiere(matiereId);
        attributes.addAttribute("success", "Matière supprimée avec succés");
        return new RedirectView("/matieres/" + matiereId);
    }

}
