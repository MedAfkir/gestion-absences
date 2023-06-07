package ensah.absencemanagement.dtos.enseignants;

import ensah.absencemanagement.dtos.files.FileMapper;
import ensah.absencemanagement.dtos.users.UserRequest;
import ensah.absencemanagement.models.enseignants.Enseignant;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = FileMapper.class)
public interface EnseignantMapper {

    @Named(value = "ignoreAccount")
    @Mapping(target = "account", ignore = true)
    EnseignantDTO map(Enseignant enseignant);

    @IterableMapping(qualifiedByName = "ignoreAccount")
    List<EnseignantDTO> map(List<Enseignant> enseignants);

    @Named(value = "ignoreUserAccount")
    @Mapping(target = "account.user", ignore = true)
    EnseignantDTO mapWithDetails(Enseignant enseignant);

    @IterableMapping(qualifiedByName = "ignoreUserAccount")
    List<EnseignantDTO> mapWithDetails(List<Enseignant> enseignants);

    UserRequest map(EnseignantDTO enseignantDTO);

    @Mapping(target = "updatedAt", expression = "java(new java.util.Date())")
    void updateEnseignant(UserRequest request, @MappingTarget Enseignant user);

}
