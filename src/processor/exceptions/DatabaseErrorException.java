package processor.exceptions;

public class DatabaseErrorException extends Exception {
    private static final long serialVersionUID = 1L;
    
    public DatabaseErrorException() {super();}
    public DatabaseErrorException(String s) {super(s);}
    public DatabaseErrorException(Throwable cause) {super(cause);}
    public DatabaseErrorException(String s, Throwable cause) {super(s, cause);}
}