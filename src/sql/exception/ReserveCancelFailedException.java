package sql.exception;

public class ReserveCancelFailedException extends Exception {
    public static final long serialVersionUID = 1L;

    public ReserveCancelFailedException() {super();}
    public ReserveCancelFailedException(String s) {super(s);}
    public ReserveCancelFailedException(Throwable cause) {super(cause);}
    public ReserveCancelFailedException(String s, Throwable cause) {super(s, cause);}
}
