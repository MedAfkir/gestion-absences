package ensah.absencemanagement.exceptions.not_found;

public class AnneeUnivNotFoundException extends NotFoundException {

    public AnneeUnivNotFoundException(Long id) {
        super("Année universitaire non trouvée pour l'identifiant " + id);
    }

}
