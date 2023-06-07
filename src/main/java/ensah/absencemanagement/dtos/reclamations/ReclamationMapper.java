package ensah.absencemanagement.dtos.reclamations;

import ensah.absencemanagement.dtos.messages.MessageMapper;
import ensah.absencemanagement.models.reclamations.Reclamation;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = MessageMapper.class
)
public interface ReclamationMapper {

    @Mapping(target = "absenceId", source = "absence.id")
    @Named("map")
    ReclamationDTO map(Reclamation reclamation);

    @IterableMapping(qualifiedByName = "map")
    List<ReclamationDTO> map(List<Reclamation> reclamations);

    @Mapping(target = "createdAt", expression = "java(new java.util.Date())")
    Reclamation createReclamation(ReclamationRequest request);

    @Mapping(target = "updatedAt", expression = "java(new java.util.Date())")
    void updateReclamation(ReclamationRequest request, @MappingTarget Reclamation reclamation);

}
