package ensah.absencemanagement.controllers.filieres;

import ensah.absencemanagement.dtos.filieres.FiliereDTO;
import ensah.absencemanagement.dtos.filieres.FiliereMapper;
import ensah.absencemanagement.dtos.filieres.FiliereRequest;
import ensah.absencemanagement.services.filieres.FiliereService;
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
@RequestMapping("/filieres")
public class FiliereController {

    private final NiveauService niveauService;
    private final FiliereService filiereService;
    private final FiliereMapper filiereMapper;

    @Autowired
    public FiliereController(NiveauService niveauService, FiliereService filiereService, FiliereMapper filiereMapper) {
        this.niveauService = niveauService;
        this.filiereService = filiereService;
        this.filiereMapper = filiereMapper;
    }

    @GetMapping
    public String getFilieresListPage(
            Model model,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page
    ) {
        model.addAttribute("page", filiereService.getAllFilieres(page - 1));
        return "filieres/filieres";
    }

    @GetMapping("/{id}")
    public String getFilierePage(Model model, @PathVariable("id") Long niveauId) {
        FiliereDTO filiereDTO = filiereService.getFiliereById(niveauId);
        model.addAttribute("filiere", filiereDTO);
        model.addAttribute("updateRequest", filiereMapper.map(filiereDTO));
        model.addAttribute("availablesNiveaux", niveauService.getNiveauxByFiliere(null));

        return "filieres/filiere";
    }

    @GetMapping("/create")
    public String getCreateFilierePage(Model model) {
        model.addAttribute("request", new FiliereRequest());
        return "filieres/create";
    }

    @PostMapping
    public RedirectView createFiliere(
            RedirectAttributes attributes,
            @ModelAttribute("request") @Valid FiliereRequest request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            attributes.addAttribute("error", bindingResult.getAllErrors().get(0).getDefaultMessage());
        } else {
            filiereService.createFiliere(request);
            attributes.addAttribute("success", "Filière créee avec succés");
        }
        return new RedirectView("/filieres/create");
    }

    @PostMapping("/{id}/infos")
    public RedirectView updateFiliereInfosById(
            RedirectAttributes attributes,
            @ModelAttribute("updateRequest") @Valid FiliereRequest request,
            @PathVariable("id") Long filiereId,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            attributes.addAttribute("error", bindingResult.getAllErrors().get(0).getDefaultMessage());
        } else {
            filiereService.updateFiliereInfosById(filiereId, request);
            attributes.addAttribute("success", "Filière modifiée avec succés");
        }
        return new RedirectView("/filieres/" + filiereId);
    }

    @PostMapping("/{id}/niveaux")
    public RedirectView updateFiliereNiveauxById(
            RedirectAttributes attributes,
            @RequestParam(value = "niveaux", required = false) List<Long> niveauxIds,
            @PathVariable("id") Long filiereId
    ) {
        if (niveauxIds != null) {
            filiereService.updateFiliereNiveaux(filiereId, niveauxIds);
            attributes.addAttribute("success", "Filière modifiée avec succés");
        }
        return new RedirectView("/filieres/" + filiereId);
    }

    @PostMapping("/{id}/delete")
    public RedirectView deleteFiliereById(
            RedirectAttributes attributes,
            @PathVariable("id") Long filiereId
    ) {
        filiereService.deleteFiliereById(filiereId);
        attributes.addAttribute("success", "Filière supprimée avec succés");
        return new RedirectView("/filieres");
    }

}
