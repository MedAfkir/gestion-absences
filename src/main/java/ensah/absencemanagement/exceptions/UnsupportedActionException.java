package ensah.absencemanagement.exceptions;

public class UnsupportedActionException extends RuntimeException {

    public UnsupportedActionException(String message) {
        super(message);
    }

    public UnsupportedActionException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
