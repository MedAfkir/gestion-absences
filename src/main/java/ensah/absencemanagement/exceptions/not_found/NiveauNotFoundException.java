package ensah.absencemanagement.exceptions.not_found;

public class NiveauNotFoundException extends NotFoundException {

    public NiveauNotFoundException(Long id) {
        super("Niveau non trouvé pour l'identifiant " + id);
    }

}
