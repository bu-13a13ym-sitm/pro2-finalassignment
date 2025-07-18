package processor.exceptions;

public class IncorrectInputException extends Exception {
    private static final long serialVersionUID = 1L;
    
    public IncorrectInputException() {super();}
    public IncorrectInputException(String s) {super(s);}
    public IncorrectInputException(Throwable cause) {super(cause);}
    public IncorrectInputException(String s, Throwable cause) {super(s, cause);}
}
