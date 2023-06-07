package ensah.absencemanagement.dtos.matieres;

import ensah.absencemanagement.models.matieres.Matiere;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MatiereMapper {

    MatiereDTO map(Matiere matiere);

    List<MatiereDTO> map(List<Matiere> matiere);

    MatiereSummaryDTO toSummary(Matiere matiere);

    List<MatiereSummaryDTO> toSummary(List<Matiere> matieres);

    MatiereUpdateRequest map(MatiereDTO matiere);

    @Mapping(target = "createdAt", expression = "java(new java.util.Date())")
    Matiere createMatiere(MatiereAddRequest request);

    @Mapping(target = "module", ignore = true)
    @Mapping(target = "updatedAt", expression = "java(new java.util.Date())")
    void updateMatiere(MatiereUpdateRequest request, @MappingTarget Matiere matiere);

}
