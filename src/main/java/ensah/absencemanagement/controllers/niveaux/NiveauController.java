package ensah.absencemanagement.controllers.niveaux;

import ensah.absencemanagement.dtos.niveaux.NiveauAddRequest;
import ensah.absencemanagement.dtos.niveaux.NiveauDTO;
import ensah.absencemanagement.dtos.niveaux.NiveauMapper;
import ensah.absencemanagement.dtos.niveaux.NiveauUpdateRequest;
import ensah.absencemanagement.models.inscriptions.Inscription;
import ensah.absencemanagement.services.annees_univ.AnneUnivService;
import ensah.absencemanagement.services.filieres.FiliereService;
import ensah.absencemanagement.services.inscriptions.InscriptionService;
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
@RequestMapping("/niveaux")
public class NiveauController {

    private final InscriptionService inscriptionService;
    private final FiliereService filiereService;
    private final ModuleService moduleService;
    private final NiveauService niveauService;
    private final NiveauMapper niveauMapper;

    @Autowired
    public NiveauController(InscriptionService inscriptionService, FiliereService filiereService, ModuleService moduleService, NiveauService niveauService, NiveauMapper niveauMapper) {
        this.inscriptionService = inscriptionService;
        this.filiereService = filiereService;
        this.moduleService = moduleService;
        this.niveauService = niveauService;
        this.niveauMapper = niveauMapper;
    }

    @GetMapping
    public String getNiveauxListPage(
            Model model,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page
    ) {
        model.addAttribute("niveaux", niveauService.getAllNiveaux(page - 1));
        return "niveaux/niveaux";
    }

    @GetMapping("/{id}")
    public String getNiveauPage(Model model, @PathVariable("id") Long niveauId) {
        model.addAttribute("filieres", filiereService.getAllFilieres());
        model.addAttribute("availablesModules", moduleService.getModulesByNiveau(null));

        NiveauDTO niveau = niveauService.getNiveauById(niveauId);
        model.addAttribute("niveau", niveau);
        model.addAttribute("request", niveauMapper.toNiveauUpdateRequest(niveau));
        model.addAttribute("inscriptions", inscriptionService.getInscriptionsByNiveauIdAndState(niveauId, Inscription.InscriptionState.ACCEPTED));

        return "niveaux/niveau";
    }

    @GetMapping("/create")
    public String getCreateNiveauPage(Model model) {
        model.addAttribute("request", new NiveauAddRequest());
        return "niveaux/create";
    }

    @PostMapping
    public RedirectView createNiveau(
            RedirectAttributes attributes,
            @ModelAttribute("request") @Valid NiveauAddRequest request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            attributes.addAttribute("error", bindingResult.getAllErrors().get(0).getDefaultMessage());
        } else {
            niveauService.createNiveau(request);
            attributes.addAttribute("success", "Niveau crée avec succés");
        }
        return new RedirectView("/niveaux/create");
    }

    @PostMapping("/{id}/infos")
    public RedirectView updateNiveauInfosById(
            RedirectAttributes attributes,
            @ModelAttribute("request") @Valid NiveauUpdateRequest request,
            @PathVariable("id") Long niveauId,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            attributes.addAttribute("error", bindingResult.getAllErrors().get(0).getDefaultMessage());
        } else {
            niveauService.updateNiveauById(niveauId, request);
            attributes.addAttribute("success", "Niveau modifié avec succés");
        }
        return new RedirectView("/niveaux/" + niveauId);
    }

    @PostMapping("/{id}/modules")
    public RedirectView updateNiveauModulesById(
            RedirectAttributes attributes,
            @RequestParam(value = "modules", required = false) List<Long> modulesIds,
            @PathVariable("id") Long niveauId
    ) {
        if (modulesIds != null) {
            niveauService.updateNiveauModulesById(niveauId, modulesIds);
            attributes.addAttribute("success", "Niveau modifié avec succés");
        }
        return new RedirectView("/niveaux/" + niveauId);
    }

    @PostMapping("/{id}/delete")
    public RedirectView deleteNiveauById(RedirectAttributes attributes, @PathVariable("id") Long niveauId) {
        niveauService.deleteNiveauById(niveauId);
        attributes.addAttribute("success", "Niveau supprimé avec succés");
        return new RedirectView("/niveaux");
    }

}
