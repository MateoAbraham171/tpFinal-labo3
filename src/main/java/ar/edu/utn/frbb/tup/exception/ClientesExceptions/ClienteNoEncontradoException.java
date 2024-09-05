package ar.edu.utn.frbb.tup.exception.ClientesExceptions;

import ar.edu.utn.frbb.tup.exception.HttpExceptions.NotFoundException;

public class ClienteNoEncontradoException extends NotFoundException {
    public ClienteNoEncontradoException(long dni) { super("Error: No se ha encontrado un cliente con dni: " + dni); }
}
