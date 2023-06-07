package ensah.absencemanagement.exceptions.not_found;

public class PieceJustificativeNotFoundException extends NotFoundException {

    public PieceJustificativeNotFoundException(Long id) {
        super("Pièce justificative non trouvée pour l'identifiant " + id);
    }

}
