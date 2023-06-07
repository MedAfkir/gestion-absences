package ensah.absencemanagement.controllers.types_seances;

import ensah.absencemanagement.dtos.types_seances.TypeSeanceDTO;
import ensah.absencemanagement.dtos.types_seances.TypeSeanceMapper;
import ensah.absencemanagement.dtos.types_seances.TypeSeanceRequest;
import ensah.absencemanagement.services.types_seances.TypeSeanceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/types-seances")
public class TypeSeanceController {

    private final TypeSeanceService typeSeanceService;
    private final TypeSeanceMapper typeSeanceMapper;

    @Autowired
    public TypeSeanceController(TypeSeanceService typeSeanceService, TypeSeanceMapper typeSeanceMapper) {
        this.typeSeanceService = typeSeanceService;
        this.typeSeanceMapper = typeSeanceMapper;
    }

    @GetMapping
    public String getTypesSeanceListPage(Model model) {
        model.addAttribute("typesSeances", typeSeanceService.getAllTypesSeances());
        return "type-seance/types-seances";
    }

    @GetMapping("/{id}")
    public String getTypesSeanceInfosPage(
            Model model,
            @PathVariable("id") Long seanceTypeId
    ) {
        TypeSeanceDTO seanceDTO = typeSeanceService.getTypeSeanceById(seanceTypeId);
        model.addAttribute("typeSeance", seanceDTO);
        model.addAttribute("request", typeSeanceMapper.map(seanceDTO));

        return "type-seance/type-seance";
    }

    @GetMapping("/create")
    public String getTypeSeanceCreationPage(Model model) {
        model.addAttribute("request", new TypeSeanceRequest());
        return "type-seance/create";
    }

    @PostMapping
    public RedirectView createTypeSeance(
            RedirectAttributes attributes,
            @ModelAttribute("request") @Valid TypeSeanceRequest request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            attributes.addAttribute("error", bindingResult.getAllErrors().get(0).getDefaultMessage());
        } else {
            typeSeanceService.createTypeSeance(request);
            attributes.addAttribute("success", "Type de seance crée avec succés");
        }
        return new RedirectView("/types-seances/create");
    }

    @PostMapping("/{id}")
    public RedirectView updateTypeSeance(
            RedirectAttributes attributes,
            @ModelAttribute("request") @Valid TypeSeanceRequest request,
            @PathVariable("id") Long seanceTypeId,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            attributes.addAttribute("error", bindingResult.getAllErrors().get(0).getDefaultMessage());
        } else {
            typeSeanceService.updateTypeSeanceById(seanceTypeId, request);
            attributes.addAttribute("success", "Type de seance modifié avec succés");
        }
        return new RedirectView("/types-seances/" + seanceTypeId);
    }

    @PostMapping("/{id}/delete")
    public RedirectView deleteTypeSeance(
            RedirectAttributes attributes,
            @PathVariable("id") Long seanceTypeId
    ) {
        typeSeanceService.deleteTypeSeanceById(seanceTypeId);
        attributes.addAttribute("success", "Type de seance supprimé avec succés");
        return new RedirectView("/types-seances");
    }

}
