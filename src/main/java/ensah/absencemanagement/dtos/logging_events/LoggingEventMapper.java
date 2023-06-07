package ensah.absencemanagement.dtos.logging_events;

import ensah.absencemanagement.dtos.files.FileMapper;
import ensah.absencemanagement.models.logging_events.LoggingEvent;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = FileMapper.class)
public interface LoggingEventMapper {

    @Named("map-visit")
    VisitEventDTO mapVisit(LoggingEvent event);

    @IterableMapping(qualifiedByName = "map-visit")
    List<VisitEventDTO> mapVisit(List<LoggingEvent> events);

}