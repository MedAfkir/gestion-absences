package ensah.absencemanagement.dtos.etudiants;

import ensah.absencemanagement.dtos.files.FileMapper;
import ensah.absencemanagement.dtos.users.UserRequest;
import ensah.absencemanagement.models.etudiants.Etudiant;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = FileMapper.class)
public interface EtudiantMapper {

    @Named(value = "ignoreAccount")
    @Mapping(target = "account", ignore = true)
    EtudiantDTO map(Etudiant etudiant);

    @IterableMapping(qualifiedByName = "ignoreAccount")
    List<EtudiantDTO> map(List<Etudiant> etudiants);

    @Named(value = "ignoreUserAccount")
    @Mapping(target = "account.user", ignore = true)
    EtudiantDTO mapWithDetails(Etudiant etudiant);

    @IterableMapping(qualifiedByName = "ignoreUserAccount")
    List<EtudiantDTO> mapWithDetails(List<Etudiant> etudiants);

    UserRequest map(EtudiantDTO etudiantDTO);

    @Mapping(target = "updatedAt", expression = "java(new java.util.Date())")
    void updateEtudiant(UserRequest request, @MappingTarget Etudiant user);

}
