package sql.exception;

public class WrongPasswordException extends Exception {
    public static final long serialVersionUID = 1L;

    public WrongPasswordException() {super();}
    public WrongPasswordException(String s) {super(s);}
    public WrongPasswordException(Throwable cause) {super(cause);}
    public WrongPasswordException(String s, Throwable cause) {super(s, cause);}
}
