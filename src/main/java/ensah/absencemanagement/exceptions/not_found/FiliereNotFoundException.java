package ensah.absencemanagement.exceptions.not_found;

public class FiliereNotFoundException extends NotFoundException {
    public FiliereNotFoundException(Long filiereId) {
        super("Filiere non trouvée pour l'identifiant " + filiereId);
    }
}
