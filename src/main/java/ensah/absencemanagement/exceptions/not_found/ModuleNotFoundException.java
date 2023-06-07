package ensah.absencemanagement.exceptions.not_found;

public class ModuleNotFoundException extends NotFoundException {

    public ModuleNotFoundException(Long id) {
        super("Module non trouvé pour l'identifiant " + id);
    }

}
