package ensah.absencemanagement.dtos.demandes_autorisation;

import ensah.absencemanagement.dtos.files.FileMapper;
import ensah.absencemanagement.models.demandes_autorisation.DemandeAutorisation;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = FileMapper.class)
public interface DemandeAutorisationMapper {

    @Named("map")
    @Mapping(target = "etudiant.account", ignore = true)
    @Mapping(target = "enseignant.account", ignore = true)
    DemandeAutorisationDTO map(DemandeAutorisation anneeUniv);

    @IterableMapping(qualifiedByName = "map")
    List<DemandeAutorisationDTO> map(List<DemandeAutorisation> anneesUniv);

}