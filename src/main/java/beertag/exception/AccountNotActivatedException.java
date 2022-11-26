package beertag.exception;

public class AccountNotActivatedException extends RuntimeException {

    public AccountNotActivatedException() {
        super("Account is not activated. Please check your email for an activation token.");
    }

}
