package ensah.absencemanagement.exceptions.not_found;

public class AbsenceNotFoundException extends NotFoundException {

    public AbsenceNotFoundException(Long id) {
        super("Absence non trouvé pour l'identifiant " + id);
    }

}
