package ensah.absencemanagement.exceptions.not_found;

public class InscriptionNotFoundException extends NotFoundException {

    public InscriptionNotFoundException(Long id) {
        super("Inscription non trouvée pour l'identifiant " + id);
    }

}
