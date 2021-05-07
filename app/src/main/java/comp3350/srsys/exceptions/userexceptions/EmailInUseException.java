package comp3350.srsys.exceptions.userexceptions;

public class EmailInUseException extends AbstractInvalidUserException {
    public EmailInUseException(String message) {
        super(message);
    }
}
