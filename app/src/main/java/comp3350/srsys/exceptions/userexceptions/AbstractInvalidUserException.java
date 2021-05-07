package comp3350.srsys.exceptions.userexceptions;

public abstract class AbstractInvalidUserException extends Exception {
    public AbstractInvalidUserException(String message) {
        super(message);
    }
}
