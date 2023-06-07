package ensah.absencemanagement.dtos.accounts;

import ensah.absencemanagement.dtos.files.FileMapper;
import ensah.absencemanagement.models.accounts.Account;
import ensah.absencemanagement.utils.SessionUser;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = FileMapper.class)
public interface AccountMapper {

    @Named(value = "ignoreUser")
    @Mapping(target = "user", ignore = true)
    AccountDTO map(Account account);

    @IterableMapping(qualifiedByName = "ignoreUser")
    List<AccountDTO> map(List<Account> accounts);

    @Named(value = "ignoreUserAccount")
    @Mapping(target = "user.account", ignore = true)
    AccountDTO mapWithDetails(Account account);

    @IterableMapping(qualifiedByName = "ignoreUserAccount")
    List<AccountDTO> mapWithDetails(List<Account> accounts);

    @Mapping(target = "firstname", source = "user.firstnameFr")
    @Mapping(target = "lastname", source = "user.lastnameFr")
    @Mapping(target = "role", source = "user.role")
    SessionUser maptoSessionUser(Account account);

    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "firstname", source = "user.firstnameFr")
    @Mapping(target = "lastname", source = "user.lastnameFr")
    @Mapping(target = "image", source = "user.image")
    void updateSessionUser(Account account, @MappingTarget SessionUser sessionUser);

}
