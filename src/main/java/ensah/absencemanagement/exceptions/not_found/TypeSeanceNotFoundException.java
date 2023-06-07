package ensah.absencemanagement.exceptions.not_found;

public class TypeSeanceNotFoundException extends NotFoundException {

    public TypeSeanceNotFoundException(Long id) {
        super("Type de sèance non trouvé pour l'identifiant " + id);
    }

}
