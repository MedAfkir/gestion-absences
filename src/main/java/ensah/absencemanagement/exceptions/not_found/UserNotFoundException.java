package ensah.absencemanagement.exceptions.not_found;

public class UserNotFoundException extends NotFoundException {

    public UserNotFoundException(Long id) {
        super("Utilisateur non trouvé pour l'identifiant " + id);
    }

    public UserNotFoundException(String message) {
        super(message);
    }

}
