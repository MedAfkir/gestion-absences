package ensah.absencemanagement.exceptions.not_found;

public class EnseignantNotFoundException extends UserNotFoundException {

    public EnseignantNotFoundException(Long id) {
        super("Enseignant(e) non trouvé(e) pour l'identifiant " + id);
    }

}
