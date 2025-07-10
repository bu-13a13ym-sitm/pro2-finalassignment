package sql.exception;

public class UserAlreadyExistException extends Exception {
    public static final long serialVersionUID = 1L;

    public UserAlreadyExistException() {super();}
    public UserAlreadyExistException(String s) {super(s);}
    public UserAlreadyExistException(Throwable cause) {super(cause);}
    public UserAlreadyExistException(String s, Throwable cause) {super(s, cause);}
}
