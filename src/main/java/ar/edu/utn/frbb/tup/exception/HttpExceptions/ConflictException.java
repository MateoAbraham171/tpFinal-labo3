package ar.edu.utn.frbb.tup.exception.HttpExceptions;

public abstract class ConflictException extends Exception{
    public ConflictException(String message) {
        super(message);
    }
}
