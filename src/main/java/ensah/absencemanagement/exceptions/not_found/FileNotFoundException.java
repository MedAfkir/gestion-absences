package ensah.absencemanagement.exceptions.not_found;

public class FileNotFoundException extends NotFoundException {

    public FileNotFoundException(Long id) {
        super("Fichier non trouvé pour l'identifiant " + id);
    }

}
