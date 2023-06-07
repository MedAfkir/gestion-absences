package ensah.absencemanagement.dtos.types_seances;

import ensah.absencemanagement.models.types_seance.TypeSeance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TypeSeanceMapper {

    TypeSeanceDTO map(TypeSeance typesSeance);

    List<TypeSeanceDTO> map(List<TypeSeance> typesSeances);

    TypeSeance map(TypeSeanceDTO typeSeanceDTO);

    @Mapping(target = "createdAt", expression = "java(new java.util.Date())")
    TypeSeance createTypeSeance(TypeSeanceRequest request);

    @Mapping(target = "updatedAt", expression = "java(new java.util.Date())")
    void updateTypeSeance(TypeSeanceRequest request, @MappingTarget TypeSeance typesSeance);

}
