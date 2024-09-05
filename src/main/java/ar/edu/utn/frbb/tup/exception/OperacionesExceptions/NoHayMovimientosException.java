package ar.edu.utn.frbb.tup.exception.OperacionesExceptions;

import ar.edu.utn.frbb.tup.exception.HttpExceptions.NotFoundException;

public class NoHayMovimientosException extends NotFoundException {
    public NoHayMovimientosException() {
        super("La cuenta no posee movimientos");
    }
}
