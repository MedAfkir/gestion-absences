package ensah.absencemanagement.exceptions.not_found;

public class EtudiantNotFoundException extends UserNotFoundException {

    public EtudiantNotFoundException(Long id) {
        super("Étudiant(e) non trouvé(e) pour l'identifiant " + id);
    }

}
