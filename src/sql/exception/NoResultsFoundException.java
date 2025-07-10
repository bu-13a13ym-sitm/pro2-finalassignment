package sql.exception;

public class NoResultsFoundException extends Exception {
    public static final long serialVersionUID = 1L;

    public NoResultsFoundException() {super();}
    public NoResultsFoundException(String s) {super(s);}
    public NoResultsFoundException(Throwable cause) {super(cause);}
    public NoResultsFoundException(String s, Throwable cause) {super(s, cause);}
}
