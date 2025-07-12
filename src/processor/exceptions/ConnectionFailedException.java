package processor.exceptions;

public class ConnectionFailedException extends Exception {
    public static final long serialVersionUID = 1L;

    public ConnectionFailedException() {super();}
    public ConnectionFailedException(String s) {super(s);}
    public ConnectionFailedException(Throwable cause) {super(cause);}
    public ConnectionFailedException(String s, Throwable cause) {super(s, cause);}
}
