package ensah.absencemanagement.services.cadres_admins;

import ensah.absencemanagement.dtos.users.UserDTO;
import ensah.absencemanagement.dtos.users.UserMapper;
import ensah.absencemanagement.dtos.users.UserRequest;
import ensah.absencemanagement.exceptions.not_found.UserNotFoundException;
import ensah.absencemanagement.models.cadres_admin.CadreAdministrateur;
import ensah.absencemanagement.repositories.CadreAdministrateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class CadreAdminService {

    private final CadreAdministrateurRepository repository;
    private final UserMapper userMapper;

    @Autowired
    public CadreAdminService(CadreAdministrateurRepository repository, UserMapper userMapper) {
        this.repository = repository;
        this.userMapper = userMapper;
    }

    public Page<UserDTO> getAllCadresAdmins(String name, int pageIndex) {
        PageRequest pageRequest = PageRequest.of(pageIndex, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<CadreAdministrateur> page = name == null || name.isEmpty()
                ? repository.findByDeleted(false, pageRequest)
                : repository.search(name, pageRequest);
        return page.map(userMapper::mapWithDetails);
    }

    public UserDTO getCadreAdminById(Long cadreAdminId) {
        CadreAdministrateur cadreAdmin = findCadreAdminById(cadreAdminId);
        return userMapper.mapWithDetails(cadreAdmin);
    }

    public void updateCadreAdminById(Long cadreAdminId, UserRequest request) {
        CadreAdministrateur cadreAdmin = findCadreAdminById(cadreAdminId);
        userMapper.updateUser(request, cadreAdmin);
        repository.save(cadreAdmin);
    }

    public void deleteCadreAdminById(Long cadreAdminId) {
        CadreAdministrateur cadreAdmin = findCadreAdminById(cadreAdminId);
        cadreAdmin.setDeleted(true);
        cadreAdmin.setUpdatedAt(new Date());
        repository.save(cadreAdmin);
    }

    private CadreAdministrateur findCadreAdminById(Long cadreAdminId) {
        return repository.findByIdAndDeleted(cadreAdminId, false)
                .orElseThrow(() -> new UserNotFoundException(cadreAdminId));
    }

}
