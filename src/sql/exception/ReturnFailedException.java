package sql.exception;

public class ReturnFailedException extends Exception {
    public static final long serialVersionUID = 1L;

    public ReturnFailedException() {super();}
    public ReturnFailedException(String s) {super(s);}
    public ReturnFailedException(Throwable cause) {super(cause);}
    public ReturnFailedException(String s, Throwable cause) {super(s, cause);}
}
