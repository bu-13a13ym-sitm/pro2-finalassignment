package processor.exceptions;

public class AlreadyRegisteredException extends Exception {
    private static final long serialVersionUID = 1L;
    
    public AlreadyRegisteredException() {super();}
    public AlreadyRegisteredException(String s) {super(s);}
    public AlreadyRegisteredException(Throwable cause) {super(cause);}
    public AlreadyRegisteredException(String s, Throwable cause) {super(s, cause);}
}