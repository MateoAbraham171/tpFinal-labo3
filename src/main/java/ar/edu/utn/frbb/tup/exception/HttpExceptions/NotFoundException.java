package ar.edu.utn.frbb.tup.exception.HttpExceptions;

public abstract class NotFoundException extends Exception{
    public NotFoundException(String message) {
        super(message);
    }
}
