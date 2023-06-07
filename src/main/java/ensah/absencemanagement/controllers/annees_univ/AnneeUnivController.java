package ensah.absencemanagement.controllers.annees_univ;

import ensah.absencemanagement.dtos.annees_univ.AnneeUnivDTO;
import ensah.absencemanagement.dtos.annees_univ.AnneeUnivMapper;
import ensah.absencemanagement.dtos.annees_univ.AnneeUnivRequest;
import ensah.absencemanagement.services.annees_univ.AnneUnivService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/annees-univ")
public class AnneeUnivController {

    private final AnneUnivService anneUnivService;
    private final AnneeUnivMapper anneeUnivMapper;

    @Autowired
    public AnneeUnivController(AnneUnivService anneUnivService, AnneeUnivMapper anneeUnivMapper) {
        this.anneUnivService = anneUnivService;
        this.anneeUnivMapper = anneeUnivMapper;
    }

    @GetMapping
    public String getAnneesUnivListPage(Model model) {
        model.addAttribute("anneesUniv", anneUnivService.getAllAnneesUniv());
        return "annees-univ/annees-univ";
    }

    @GetMapping("/{id}")
    public String getAnneeUnivPage(Model model, @PathVariable("id") Long anneeUniv) {
        AnneeUnivDTO anneeUnivDTO = anneUnivService.getAnneeUnivById(anneeUniv);
        model.addAttribute("anneeUniv", anneeUnivDTO);
        model.addAttribute("updateRequest", anneeUnivMapper.map(anneeUnivDTO));

        return "annees-univ/annee-univ";
    }

    @GetMapping("/create")
    public String getCreateAnneeUnivPage(Model model) {
        model.addAttribute("request", new AnneeUnivRequest());
        return "annees-univ/create";
    }

    @PostMapping
    public RedirectView createAnneeUniv(
            RedirectAttributes attributes,
            @ModelAttribute("request") @Valid AnneeUnivRequest request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            attributes.addAttribute("error", bindingResult.getAllErrors().get(0).getDefaultMessage());
        } else {
            anneUnivService.createAnneesUniv(request);
            attributes.addAttribute("success", "Année universitaire crée avec succés");
        }
        return new RedirectView("/annees-univ/create");
    }

    @PostMapping("/{id}")
    public RedirectView updateAnneeUnivById(
            RedirectAttributes attributes,
            @ModelAttribute("updateRequest") @Valid AnneeUnivRequest request,
            @PathVariable("id") Long anneeUnivId,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            attributes.addAttribute("error", bindingResult.getAllErrors().get(0).getDefaultMessage());
        } else {
            anneUnivService.updateAnneeUnivById(anneeUnivId, request);
            attributes.addAttribute("success", "Année universitaire modifiée avec succés");
        }
        return new RedirectView("/annees-univ/" + anneeUnivId);
    }

    @PostMapping("/{id}/delete")
    public RedirectView deleteNiveauById(
            RedirectAttributes attributes,
            @PathVariable("id") Long anneeUnivId
    ) {
        anneUnivService.deleteAnneeUnivById(anneeUnivId);
        attributes.addAttribute("success", "Niveau supprimé avec succés");
        return new RedirectView("/annees-univ");
    }

}
