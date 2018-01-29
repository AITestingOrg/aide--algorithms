package exceptions;

public class UncoercibleTypeException extends Exception{
    public UncoercibleTypeException(String message){
        super(message);
    }

    public UncoercibleTypeException(String message, StackTraceElement[] st){
        super(message);
        this.setStackTrace(st);
    }
}
