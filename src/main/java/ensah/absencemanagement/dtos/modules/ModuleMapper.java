package ensah.absencemanagement.dtos.modules;

import ensah.absencemanagement.models.modules.Module;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ModuleMapper {

    ModuleDTO map(Module module);
    List<ModuleDTO> map(List<Module> modules);

    ModuleSummaryDTO toSummary(Module module);
    List<ModuleSummaryDTO> toSummary(List<Module> modules);

    ModuleUpdateRequest toModuleUpdateRequest(ModuleDTO module);

    @Mapping(target = "createdAt", expression = "java(new java.util.Date())")
    Module createModule(ModuleAddRequest request);

    @Mapping(target = "updatedAt", expression = "java(new java.util.Date())")
    void updateModule(ModuleUpdateRequest request, @MappingTarget Module module);

}
