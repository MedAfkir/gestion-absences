package ensah.absencemanagement.dtos.filieres;

import ensah.absencemanagement.models.filieres.Filiere;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FiliereMapper {

    FiliereDTO map(Filiere filiere);

    List<FiliereDTO> map(List<Filiere> filieres);

    FiliereSummaryDTO toSummary(Filiere filiere);

    List<FiliereSummaryDTO> toSummary(List<Filiere> filieres);

    FiliereRequest map(FiliereDTO filiereDTO);

    @Mapping(target = "createdAt", expression = "java(new java.util.Date())")
    Filiere createFiliere(FiliereRequest request);

    @Mapping(target = "updatedAt", expression = "java(new java.util.Date())")
    void updateFiliere(FiliereRequest request, @MappingTarget Filiere filiere);

}