package sql.exception;

public class InvalidInputException extends Exception{
    public static final long serialVersionUID = 1L;

    public InvalidInputException() {super();}
    public InvalidInputException(String s) {super(s);}
    public InvalidInputException(Throwable cause) {super(cause);}
    public InvalidInputException(String s, Throwable cause) {super(s, cause);}
}
