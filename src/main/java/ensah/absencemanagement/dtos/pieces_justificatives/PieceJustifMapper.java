package ensah.absencemanagement.dtos.pieces_justificatives;

import ensah.absencemanagement.models.pieces_justifications.PieceJustificative;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PieceJustifMapper {

    @Mapping(target = "absenceId", source = "absence.id")
    @Named("mapToDTO")
    PieceJustificativeDTO map(PieceJustificative piece);

    @IterableMapping(qualifiedByName = "mapToDTO")
    List<PieceJustificativeDTO> map(List<PieceJustificative> pieces);

}
