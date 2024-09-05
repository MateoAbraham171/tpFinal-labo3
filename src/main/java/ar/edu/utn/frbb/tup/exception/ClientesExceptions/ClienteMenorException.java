package ar.edu.utn.frbb.tup.exception.ClientesExceptions;

import ar.edu.utn.frbb.tup.exception.HttpExceptions.ConflictException;

public class ClienteMenorException extends ConflictException {
    public ClienteMenorException(String message) {
        super(message);
    }
}