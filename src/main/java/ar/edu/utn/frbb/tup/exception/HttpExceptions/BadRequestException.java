package ar.edu.utn.frbb.tup.exception.HttpExceptions;

public abstract class BadRequestException extends Exception{
    public BadRequestException(String mensaje) { super(mensaje); }
}