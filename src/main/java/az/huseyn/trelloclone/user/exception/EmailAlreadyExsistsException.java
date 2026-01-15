package az.huseyn.trelloclone.user.exception;

public class EmailAlreadyExsistsException extends RuntimeException {
    public EmailAlreadyExsistsException(String message) {
        super(message);
    }
}
