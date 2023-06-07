package ensah.absencemanagement.dtos.messages;

import ensah.absencemanagement.dtos.reclamations.ReclamationRequest;
import ensah.absencemanagement.models.messages.Message;
import ensah.absencemanagement.models.reclamations.Reclamation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MessageMapper {

    MessageDTO map(Message message);

    List<MessageDTO> map(List<Message> messages);

    @Mapping(target = "createdAt", expression = "java(new java.util.Date())")
    Message createMessage(MessageRequest request);

    @Mapping(target = "updatedAt", expression = "java(new java.util.Date())")
    void updateMessage(MessageRequest request, @MappingTarget Message message);

}
