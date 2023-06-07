package ensah.absencemanagement.services.users;

import ensah.absencemanagement.dtos.files.FileMapper;
import ensah.absencemanagement.dtos.profil.ProfilUpdateRequest;
import ensah.absencemanagement.dtos.users.UserDTO;
import ensah.absencemanagement.dtos.users.UserMapper;
import ensah.absencemanagement.dtos.users.UserRequest;
import ensah.absencemanagement.dtos.users.UserRoleRequest;
import ensah.absencemanagement.exceptions.UnsupportedActionException;
import ensah.absencemanagement.exceptions.not_found.UserNotFoundException;
import ensah.absencemanagement.models.cadres_admin.CadreAdministrateur;
import ensah.absencemanagement.models.enseignants.Enseignant;
import ensah.absencemanagement.models.etudiants.Etudiant;
import ensah.absencemanagement.models.images.File;
import ensah.absencemanagement.models.users.User;
import ensah.absencemanagement.models.users.UserRepository;
import ensah.absencemanagement.repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserService {

    private final FileRepository fileRepository;
    private final FileMapper fileMapper;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(FileRepository fileRepository, FileMapper fileMapper, UserRepository userRepository, UserMapper userMapper) {
        this.fileRepository = fileRepository;
        this.fileMapper = fileMapper;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public Page<UserDTO> getAllUsers(boolean detailed, String name, int page) {
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<User> users = name == null || name.isEmpty()
                ? userRepository.findByDeleted(false, pageRequest)
                : userRepository.search(name, pageRequest);

        if (detailed)
            return users.map(userMapper::mapWithDetails);

        return users.map(userMapper::map);
    }

    public UserDTO getUserById(Long userId) {
        return userMapper.map(findUserById(userId));
    }

    public Long createUser(UserRequest request) {
        User user = userMapper.createUser(request);
        User savedUser = userRepository.save(user);
        return savedUser.getId();
    }

    public void updateUserById(Long userId, UserRequest request) {
        User user = findUserById(userId);
        userMapper.updateUser(request, user);
        userRepository.save(user);
    }

    public void updateUserById(Long userId, ProfilUpdateRequest request) {
        User user = findUserById(userId);
        userMapper.updateUser(request, user);
        userRepository.save(user);
    }

    public Long updateUserRole(Long userId, UserRoleRequest request) {
        User user = findUserById(userId);

        if (user.getRole() != null) {
            throw new UnsupportedActionException(String.format(
                    "Utilisateur %s %s possède déja un role",
                    user.getFirstnameFr(),
                    user.getLastnameFr().toUpperCase()
            ));
        }

        User newUser = null;
        switch (request.getRole()) {
            case ETUDIANT -> {
                newUser = new Etudiant();
                userMapper.updateUser(user, newUser);
                ((Etudiant) newUser).setCne(request.getCne());
                ((Etudiant) newUser).setDateNaissance(request.getDateNaissance());
            }
            case ENSEIGNANT -> {
                newUser = new Enseignant();
                userMapper.updateUser(user, newUser);
                ((Enseignant) newUser).setCin(request.getCin());
                ((Enseignant) newUser).setReceiveAuthorisationDemands(true);
            }
            case CADRE_ADMINISTRATEUR -> {
                newUser = new CadreAdministrateur();
                userMapper.updateUser(user, newUser);
            }
        }

        if (newUser != null) {
            userRepository.delete(user);
            User savedUser = userRepository.save(newUser);
            return savedUser.getId();
        }

        return userId;
    }

    public void uploadProfilImage(Long userId, MultipartFile file) {
        if (file.getContentType() == null) {
            throw new UnsupportedActionException("Type de fichier séléctionné est inconnu");
        }

        if (!file.getContentType().startsWith("image/"))
            throw new UnsupportedActionException("Fichier séléctionné n'est pas une image");

        User user = findUserById(userId);
        try {
            File image = fileMapper.map(file);
            System.out.println("IMAGE " + image);
            if (image == null) return;
            File savedImage = fileRepository.save(image);
            user.setImage(savedImage);
            userRepository.save(user);
        } catch (IOException e) {
            throw new UnsupportedActionException("Une erreur est survenue !");
        }
    }

    public void removeProfilImage(Long userId) {
        User user = findUserById(userId);
        File image = user.getImage();
        user.setImage(null);
        userRepository.save(user);
        fileRepository.delete(image);
    }

    public void deleteUserById(Long userId) {
        User user = findUserById(userId);
        user.setDeleted(true);
        userRepository.save(user);
    }

    private User findUserById(Long userId) {
        return userRepository.findByIdAndDeleted(userId, false)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

}
