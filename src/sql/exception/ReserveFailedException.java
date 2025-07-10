package sql.exception;

public class ReserveFailedException extends Exception {
    public static final long serialVersionUID = 1L;

    public ReserveFailedException() {super();}
    public ReserveFailedException(String s) {super(s);}
    public ReserveFailedException(Throwable cause) {super(cause);}
    public ReserveFailedException(String s, Throwable cause) {super(s, cause);}
}
