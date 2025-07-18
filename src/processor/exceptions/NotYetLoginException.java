package processor.exceptions;

public class NotYetLoginException extends Exception {
    private static final long serialVersionUID = 1L;
    
    public NotYetLoginException() {super();}
    public NotYetLoginException(String s) {super(s);}
    public NotYetLoginException(Throwable cause) {super(cause);}
    public NotYetLoginException(String s, Throwable cause) {super(s, cause);}
}