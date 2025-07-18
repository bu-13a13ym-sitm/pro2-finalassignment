package processor.exceptions;

public class NoAccountException extends Exception {
    private static final long serialVersionUID = 1L;
    
    public NoAccountException() {super();}
    public NoAccountException(String s) {super(s);}
    public NoAccountException(Throwable cause) {super(cause);}
    public NoAccountException(String s, Throwable cause) {super(s, cause);}
}