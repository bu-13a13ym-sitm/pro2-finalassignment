package sql.exception;

public class DatabaseException extends Exception {
    private static final long serialVersionUID = 1L;
    
    public DatabaseException() {super();}
    public DatabaseException(String s) {super(s);}
    public DatabaseException(Throwable cause) {super(cause);}
    public DatabaseException(String s, Throwable cause) {super(s, cause);}
}
