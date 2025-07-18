package processor.exceptions;

public class EmptyInputException extends Exception {
    private static final long serialVersionUID = 1L;
    
    public EmptyInputException() {super();}
    public EmptyInputException(String s) {super(s);}
    public EmptyInputException(Throwable cause) {super(cause);}
    public EmptyInputException(String s, Throwable cause) {super(s, cause);}
}
