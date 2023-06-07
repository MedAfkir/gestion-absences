package ensah.absencemanagement.dtos.absences;

import ensah.absencemanagement.dtos.files.FileMapper;
import ensah.absencemanagement.models.absence.Absence;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = FileMapper.class)
public interface AbsenceMapper {

    @Named(value = "toAbsenceDTO")
    AbsenceDTO map(Absence absence);

    @IterableMapping(qualifiedByName = "toAbsenceDTO")
    List<AbsenceDTO> map(List<Absence> absences);

    @Mapping(target = "etudiantId", source = "etudiant.id")
    @Mapping(target = "typeSeanceId", source = "typeSeance.id")
    @Mapping(target = "matiereId", source = "matiere.id")
    AbsenceRequest map(AbsenceDTO absenceDTO);

    @Mapping(target = "createdAt", expression = "java(new java.util.Date())")
    Absence createAbsence(AbsenceRequest request);

    @Mapping(target = "createdAt", expression = "java(new java.util.Date())")
    Absence createAbsence(MultipleAbsenceRequest<List<Long>> request);

    @Mapping(target = "updatedAt", expression = "java(new java.util.Date())")
    void updateAbsence(AbsenceRequest request, @MappingTarget Absence absence);

    @Mapping(target = "etudiants", ignore = true)
    MultipleAbsenceRequest<List<Long>> map(MultipleAbsenceRequest<String> request);

    default Page<AbsenceDTO> map(Page<Absence> page) {
        return page.map(this::map);
    }

}
