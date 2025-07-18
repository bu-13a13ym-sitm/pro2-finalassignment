package processor.exceptions;

public class DisconnectionFailedException extends Exception {
    private static final long serialVersionUID = 1L;
    
    public DisconnectionFailedException() {super();}
    public DisconnectionFailedException(String s) {super(s);}
    public DisconnectionFailedException(Throwable cause) {super(cause);}
    public DisconnectionFailedException(String s, Throwable cause) {super(s, cause);}
}