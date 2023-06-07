package ensah.absencemanagement.exceptions.not_found;

public class MatiereNotFoundException extends NotFoundException {

    public MatiereNotFoundException(Long id) {
        super("Matière non trouvée pour l'identifiant " + id);
    }

}
