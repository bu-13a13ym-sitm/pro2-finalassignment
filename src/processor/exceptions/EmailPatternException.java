package processor.exceptions;

public class EmailPatternException extends Exception {
    private static final long serialVersionUID = 1L;
    
    public EmailPatternException() {super();}
    public EmailPatternException(String s) {super(s);}
    public EmailPatternException(Throwable cause) {super(cause);}
    public EmailPatternException(String s, Throwable cause) {super(s, cause);}
}