package ensah.absencemanagement.dtos.users;

import ensah.absencemanagement.dtos.files.FileMapper;
import ensah.absencemanagement.dtos.profil.ProfilUpdateRequest;
import ensah.absencemanagement.models.users.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = FileMapper.class)
public interface UserMapper {

    @Named(value = "ignoreAccount")
    @Mapping(target = "account", ignore = true)
    UserDTO map(User user);

    @IterableMapping(qualifiedByName = "ignoreAccount")
    List<UserDTO> map(List<User> users);

    @Named(value = "ignoreAccountUser")
    @Mapping(target = "account.user", ignore = true)
    UserDTO mapWithDetails(User user);

    @IterableMapping(qualifiedByName = "ignoreAccountUser")
    List<UserDTO> mapWithDetails(List<? extends User> users);

    UserRequest map(UserDTO userDTO);

    ProfilUpdateRequest toProfil(UserDTO user);

    @Mapping(target = "createdAt", expression = "java(new java.util.Date())")
    User createUser(UserRequest request);

    @Mapping(target = "updatedAt", expression = "java(new java.util.Date())")
    void updateUser(UserRequest request, @MappingTarget User user);

    @Mapping(target = "updatedAt", expression = "java(new java.util.Date())")
    void updateUser(ProfilUpdateRequest request, @MappingTarget User user);

    @Mapping(target = "updatedAt", expression = "java(new java.util.Date())")
    void updateUser(User source, @MappingTarget User target);

    void copyUser(User source, @MappingTarget User target);

}
