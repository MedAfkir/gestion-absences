package ensah.absencemanagement.dtos.niveaux;

import ensah.absencemanagement.models.niveaux.Niveau;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NiveauMapper {

    NiveauDTO map(Niveau niveau);
    List<NiveauDTO> map(List<Niveau> niveaux);

    NiveauUpdateRequest toNiveauUpdateRequest(NiveauDTO niveau);

    NiveauSummaryDTO toSummary(Niveau niveau);
    List<NiveauSummaryDTO> toSummary(List<Niveau> niveaux);

    @Mapping(target = "createdAt", expression = "java(new java.util.Date())")
    Niveau createNiveau(NiveauAddRequest request);

    @Mapping(target = "updatedAt", expression = "java(new java.util.Date())")
    void updateNiveau(NiveauUpdateRequest request, @MappingTarget Niveau niveau);

}
