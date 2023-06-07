package ensah.absencemanagement.dtos.annees_univ;

import ensah.absencemanagement.models.annees_univ.AnneeUniv;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnneeUnivMapper {

    AnneeUnivDTO map(AnneeUniv anneeUniv);

    List<AnneeUnivDTO> map(List<AnneeUniv> anneesUniv);

    AnneeUnivRequest map(AnneeUnivDTO anneeUnivDTO);

    @Mapping(target = "createdAt", expression = "java(new java.util.Date())")
    AnneeUniv createAnneeUniv(AnneeUnivRequest request);

    @Mapping(target = "updatedAt", expression = "java(new java.util.Date())")
    void updateAnneeUniv(AnneeUnivRequest request, @MappingTarget AnneeUniv anneeUniv);

}