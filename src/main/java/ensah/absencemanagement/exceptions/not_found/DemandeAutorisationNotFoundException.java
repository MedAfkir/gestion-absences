package ensah.absencemanagement.exceptions.not_found;

public class DemandeAutorisationNotFoundException extends NotFoundException {

    public DemandeAutorisationNotFoundException(Long id) {
        super("Demande d'utorisation non trouvée pour l'identifiant " + id);
    }

}
