package sql.exception;

public class UserNotFoundException extends Exception {
    public static final long serialVersionUID = 1L;

    public UserNotFoundException() {super();}
    public UserNotFoundException(String s) {super(s);}
    public UserNotFoundException(Throwable cause) {super(cause);}
    public UserNotFoundException(String s, Throwable cause) {super(s, cause);}
}
