package ensah.absencemanagement.dtos.inscriptions;

import ensah.absencemanagement.dtos.files.FileMapper;
import ensah.absencemanagement.dtos.filieres.FiliereDTO;
import ensah.absencemanagement.dtos.filieres.FiliereMapper;
import ensah.absencemanagement.dtos.filieres.FiliereRequest;
import ensah.absencemanagement.models.inscriptions.Inscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = FileMapper.class)
public interface InscriptionMapper {

    InscriptionDTO map(Inscription inscription);

    List<InscriptionDTO> map(List<Inscription> inscriptions);

    FiliereRequest map(FiliereDTO filiereDTO);

    @Mapping(target = "createdAt", expression = "java(new java.util.Date())")
    Inscription createInscription(FiliereRequest request);

    @Mapping(target = "updatedAt", expression = "java(new java.util.Date())")
    void updateInscription(FiliereRequest request, @MappingTarget Inscription inscription);

}