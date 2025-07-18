package processor.exceptions;

public class NoProductFoundException extends Exception {
    private static final long serialVersionUID = 1L;
    
    public NoProductFoundException() {super();}
    public NoProductFoundException(String s) {super(s);}
    public NoProductFoundException(Throwable cause) {super(cause);}
    public NoProductFoundException(String s, Throwable cause) {super(s, cause);}
}