package ar.edu.utn.frbb.tup.exception.ClientesExceptions;

import ar.edu.utn.frbb.tup.exception.HttpExceptions.ConflictException;

public class ClienteAlreadyExistsException extends ConflictException {
    public ClienteAlreadyExistsException() {
        super("Error: Ya existe un cliente con ese DNI");
    }
}