package sql.exception;

public class RentalFailedException extends Exception {
    public static final long serialVersionUID = 1L;
    
    public RentalFailedException() {super();}
    public RentalFailedException(String s) {super(s);}
    public RentalFailedException(Throwable cause) {super(cause);}
    public RentalFailedException(String s, Throwable cause) {super(s, cause);}
}
