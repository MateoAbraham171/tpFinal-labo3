package ar.edu.utn.frbb.tup.exception.ClientesExceptions;

import ar.edu.utn.frbb.tup.exception.HttpExceptions.NotFoundException;

public class NoExistenClientesException extends NotFoundException {
    public NoExistenClientesException() { super("No hay clientes registrados"); }
}
